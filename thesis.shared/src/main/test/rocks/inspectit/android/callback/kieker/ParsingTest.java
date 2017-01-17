package rocks.inspectit.android.callback.kieker;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import rocks.fasterxml.jackson.core.JsonParseException;
import rocks.fasterxml.jackson.databind.JsonMappingException;
import rocks.fasterxml.jackson.databind.ObjectMapper;
import rocks.inspectit.android.callback.data.MobileCallbackData;

public class ParsingTest {
	
	private static final String NETINFOJSON = "{\"childData\":[{\"type\":\"NetInfoResponse\",\"bssid\":\"\",\"ssid\":\"\",\"security\":3,\"mobile\":true,\"wifi\":false}],\"tagList\":[{\"value\":\"true\",\"name\":\"device.network.connected\",\"dynamic\":true},{\"value\":\"1000.0\",\"name\":\"device.battery.cap\",\"dynamic\":true},{\"value\":\"0.5\",\"name\":\"device.battery.pct\",\"dynamic\":true},{\"value\":\"true\",\"name\":\"device.network.mobile\",\"dynamic\":true},{\"value\":\"false\",\"name\":\"device.network.wifi\",\"dynamic\":true}],\"sessionId\":\"25dba4d9-4d56-49a9-86ef-0df86a82002e\",\"creationTimestamp\":1484685584077}";
	private static ObjectMapper mapper;

	@BeforeClass
	public static void setUpBeforeClass() {
		mapper = new ObjectMapper();
	}

	@Test
	public void test() throws JsonParseException, JsonMappingException, IOException {
		mapper.readValue(NETINFOJSON, MobileCallbackData.class);
	}

}
