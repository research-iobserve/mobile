package thesis.inspectit.bridge.rest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.filesystem.SyncFsWriter;
import rocks.fasterxml.jackson.databind.ObjectMapper;
import rocks.inspectit.android.callback.data.HelloRequest;
import rocks.inspectit.android.callback.data.HelloResponse;
import rocks.inspectit.android.callback.data.MobileCallbackData;
import rocks.inspectit.android.callback.data.MobileDefaultData;
import rocks.inspectit.android.callback.kieker.IKiekerCompatible;
import rocks.inspectit.android.callback.kieker.MobileDeploymentRecord;

/**
 * REST interface which is used from the agent to communicate with the
 * monitoring server. This REST interface basically receives monitoring records
 * and if it is possible to convert them into Kieker records the server does so
 * and saves them into Kieker logs.
 * 
 * @author David Monschein
 * @author Robert Heinrich
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
	 * Method of the REST interface which is responsible for session creation
	 * requests
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
	public String postHello(String hello) {
		MobileCallbackData cbData = preprocessBeacon(hello);
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
	public String postBeacon(String beacon) {
		MobileCallbackData cbData = preprocessBeacon(beacon);
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
	private MobileCallbackData preprocessBeacon(String data) {
		try {
			return objectMapper.readValue(data, MobileCallbackData.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Processes a message which only contains a {@link HelloRequest}.
	 * 
	 * @param data
	 *            the message
	 * @return generated result - either a JSON encoded {@link HelloResponse} or
	 *         an error message
	 */
	private String processHello(MobileCallbackData data) {
		if (data.getSessionId() == null) {
			if (data.getChildData().size() != 1 || !(data.getChildData().get(0) instanceof HelloRequest)) {
				return "";
			}

			String appName = ((HelloRequest) data.getChildData().get(0)).getAppName();

			if (!controllerMap.containsKey(appName)) {
				createController(appName);
			}

			HelloResponse resp = new HelloResponse();
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
	private String processBeacon(MobileCallbackData data) {
		if (data.getSessionId() != null && sessionStorage.exists(data.getSessionId())) {
			String appId = sessionStorage.get(data.getSessionId());
			for (MobileDefaultData child : data.getChildData()) {
				if (child instanceof IKiekerCompatible) {
					IMonitoringRecord genRecord = ((IKiekerCompatible) child).generateRecord();
					// PASS
					controllerMap.get(appId).newMonitoringRecord(genRecord);
				}
			}
		}
		return "";
	}

	/**
	 * Creates a deployment record after a session was created.
	 * 
	 * @param appName
	 *            the name of the application
	 * @param deviceId
	 *            the id of the mobile device
	 */
	private void createDeploymentRecord(String activityName, String deviceId, String appName) {
		MobileDeploymentRecord record = new MobileDeploymentRecord(deviceId, activityName);
		controllerMap.get(appName).newMonitoringRecord(record);
	}

	/**
	 * Creates a new {@link MonitoringController} to create Kieker logs.
	 * 
	 * @param id
	 *            the name of the application
	 */
	private void createController(String id) {
		File outputPath = new File(BASE_PATH + "/" + reformatId(id));
		if (!outputPath.exists())
			outputPath.mkdirs();

		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(ConfigurationFactory.METADATA, "true");
		configuration.setProperty(ConfigurationFactory.AUTO_SET_LOGGINGTSTAMP, "true");
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, SyncFsWriter.class.getName());
		configuration.setProperty(ConfigurationFactory.TIMER_CLASSNAME, "kieker.monitoring.timer.SystemMilliTimer");
		configuration.setProperty(SyncFsWriter.CONFIG_PATH, outputPath.getAbsolutePath());
		configuration.setProperty(SyncFsWriter.CONFIG_MAXENTRIESINFILE, String.valueOf(MAXLOG));
		configuration.setProperty(SyncFsWriter.CONFIG_MAXLOGFILES, "-1");

		IMonitoringController controller = MonitoringController.createInstance(configuration);
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
	private String reformatId(String id) {
		return id.replaceAll(" ", "").replaceAll("-", "");
	}

}
