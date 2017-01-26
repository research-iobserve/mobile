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

// TODO handle kieker sessions
@Path("/rest/mobile")
public class RestService {
	private static final String BASE_PATH = new File("kieker-logs/").getAbsolutePath();
	private static final int MAXLOG = 100000;

	private static ObjectMapper objectMapper;
	private static ISessionStorage<String> sessionStorage;
	private static Map<String, IMonitoringController> controllerMap;

	static {
		objectMapper = new ObjectMapper();
		sessionStorage = new SimpleSessionStorage<>();
		controllerMap = new HashMap<>();
	}

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

	private MobileCallbackData preprocessBeacon(String data) {
		try {
			return objectMapper.readValue(data, MobileCallbackData.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String processHello(MobileCallbackData data) {
		if (data.getSessionId() == null) {
			if (data.getChildData().size() != 1 || !(data.getChildData().get(0) instanceof HelloRequest)) {
				return "";
			}

			String appName = ((HelloRequest) data.getChildData().get(0)).getAppName();
			String deviceId = ((HelloRequest) data.getChildData().get(0)).getDeviceId();

			if (!controllerMap.containsKey(appName)) {
				createController(appName);
			}
			createDeploymentRecord(appName, deviceId);

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

	private String processBeacon(MobileCallbackData data) {
		if (data.getSessionId() != null && sessionStorage.exists(data.getSessionId())) {
			String appId = sessionStorage.get(data.getSessionId());

			for (MobileDefaultData child : data.getChildData()) {
				if (child instanceof IKiekerCompatible) {
					// PASS
					controllerMap.get(appId).newMonitoringRecord(((IKiekerCompatible) child).generateRecord());
				}
			}
		}
		return "";
	}

	private void createDeploymentRecord(String appName, String deviceId) {
		MobileDeploymentRecord record = new MobileDeploymentRecord(deviceId, appName);
		controllerMap.get(appName).newMonitoringRecord(record);
	}

	private void createController(String id) {
		File outputPath = new File(BASE_PATH + "/" + reformatId(id));
		if (!outputPath.exists())
			outputPath.mkdirs();

		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(ConfigurationFactory.METADATA, "true");
		configuration.setProperty(ConfigurationFactory.AUTO_SET_LOGGINGTSTAMP, "false");
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, SyncFsWriter.class.getName());
		configuration.setProperty(SyncFsWriter.CONFIG_PATH, outputPath.getAbsolutePath());
		configuration.setProperty(SyncFsWriter.CONFIG_MAXENTRIESINFILE, String.valueOf(MAXLOG));
		configuration.setProperty(SyncFsWriter.CONFIG_MAXLOGFILES, "-1");

		controllerMap.put(id, MonitoringController.createInstance(configuration));
	}

	private String reformatId(String id) {
		return id.replaceAll(" ", "").replaceAll("-", "");
	}

}
