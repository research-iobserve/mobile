/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.mobile.server.rest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.iobserve.shared.callback.data.MobileCallbackData;
import org.iobserve.shared.callback.data.MobileDefaultData;
import org.iobserve.shared.callback.data.SessionCreationRequest;
import org.iobserve.shared.callback.data.SessionCreationResponse;
import org.iobserve.shared.callback.kieker.IKiekerCompatible;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.filesystem.SyncFsWriter;
import rocks.fasterxml.jackson.databind.ObjectMapper;

/**
 * REST interface which is used from the agent to communicate with the
 * monitoring server. This REST interface basically receives monitoring records
 * and if it is possible to convert them into Kieker records the server does so
 * and saves them into Kieker logs.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
@Path("/rest/mobile")
public class RestService {
	/**
	 * Specifies the path where the Kieker logs should be saved.
	 */
	private static final String BASE_PATH = new File("kieker-logs/").getAbsolutePath();

	/**
	 * The maximum size of a single Kieker log-file.
	 */
	private static final int MAXLOG = 100000;

	/**
	 * Jackson object mapper for handling JSON objects and strings.
	 */
	private static ObjectMapper objectMapper;

	/**
	 * Session storage which is used to associate session ids with string
	 * values.
	 */
	private static ISessionStorage<String> sessionStorage;

	/**
	 * Map which maps the name of the mobile application to a certain
	 * {@link MonitoringController}.
	 */
	private static Map<String, IMonitoringController> controllerMap;

	/**
	 * Initializes the static attributes
	 */
	static {
		objectMapper = new ObjectMapper();
		sessionStorage = new SimpleSessionStorage<>();
		controllerMap = new HashMap<>();
	}

	/**
	 * Creates a new instance.
	 */
	public RestService() {
	}

	/**
	 * Method of the REST interface which is responsible for session creation
	 * requests.
	 * 
	 * @param hello
	 *            the session creation request as JSON string
	 * @return generated response which either contains a session id or an error
	 *         message
	 */
	@POST
	@Path("hello")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String postHello(final String hello) {
		final MobileCallbackData cbData = preprocessBeacon(hello);
		if (cbData != null) {
			return processHello(cbData);
		}
		return String.valueOf(false);
	}

	/**
	 * Method of the REST interface which is responsible for processing incoming
	 * beacons.
	 * 
	 * @param beacon
	 *            beacon to process
	 * @return result of the processing
	 */
	@POST
	@Path("beacon")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String postBeacon(final String beacon) {
		final MobileCallbackData cbData = preprocessBeacon(beacon);
		if (cbData != null) {
			return processBeacon(cbData);
		}
		return String.valueOf(false);
	}

	/**
	 * Prepares a beacon.
	 * 
	 * @param data
	 *            beacon as a string
	 * @return parsed beacon
	 */
	private MobileCallbackData preprocessBeacon(final String data) {
		try {
			return objectMapper.readValue(data, MobileCallbackData.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Processes a message which only contains a {@link SessionCreationRequest}.
	 * 
	 * @param data
	 *            the message
	 * @return generated result - either a JSON encoded
	 *         {@link SessionCreationResponse} or an error message
	 */
	private String processHello(final MobileCallbackData data) {
		if (data.getSessionId() == null) {
			if (data.getChildData().size() != 1 || !(data.getChildData().get(0) instanceof SessionCreationRequest)) {
				return "";
			}

			final String appName = ((SessionCreationRequest) data.getChildData().get(0)).getAppName();

			if (!controllerMap.containsKey(appName)) {
				createController(appName);
			}

			final SessionCreationResponse resp = new SessionCreationResponse();
			resp.setSessionId(sessionStorage.create(appName));

			try {
				return objectMapper.writeValueAsString(resp);
			} catch (IOException e) {
				return "";
			}
		}
		return null;
	}

	/**
	 * Processes a container which contains several beacons.
	 * 
	 * @param data
	 *            container with beacons
	 * @return processing result
	 */
	private String processBeacon(final MobileCallbackData data) {
		if (data.getSessionId() != null && sessionStorage.exists(data.getSessionId())) {
			final String appId = sessionStorage.get(data.getSessionId());
			for (MobileDefaultData child : data.getChildData()) {
				if (child instanceof IKiekerCompatible) {
					final IMonitoringRecord genRecord = ((IKiekerCompatible) child).generateRecord();
					// PASS
					controllerMap.get(appId).newMonitoringRecord(genRecord);
				}
			}
		}
		return "";
	}

	/**
	 * Creates a new {@link MonitoringController} to create Kieker logs.
	 * 
	 * @param id
	 *            the name of the application
	 */
	private void createController(final String id) {
		final File outputPath = new File(BASE_PATH + "/" + reformatId(id));
		if (!outputPath.exists()) {
			outputPath.mkdirs();
		}

		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(ConfigurationFactory.METADATA, "true");
		configuration.setProperty(ConfigurationFactory.AUTO_SET_LOGGINGTSTAMP, "true");
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, SyncFsWriter.class.getName());
		configuration.setProperty(ConfigurationFactory.TIMER_CLASSNAME, "kieker.monitoring.timer.SystemMilliTimer");
		configuration.setProperty(SyncFsWriter.CONFIG_PATH, outputPath.getAbsolutePath());
		configuration.setProperty(SyncFsWriter.CONFIG_MAXENTRIESINFILE, String.valueOf(MAXLOG));
		configuration.setProperty(SyncFsWriter.CONFIG_MAXLOGFILES, "-1");

		final IMonitoringController controller = MonitoringController.createInstance(configuration);
		controllerMap.put(id, controller);
	}

	/**
	 * Reformats the name of an application to bring it into a form which can be
	 * assigned as folder name.
	 * 
	 * @param id
	 *            name of the application
	 * @return escaped and reformatted app name
	 */
	private String reformatId(final String id) {
		return id.replaceAll(" ", "").replaceAll("-", "");
	}

}
