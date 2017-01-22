package rocks.inspectit.android.callback.kieker;

import java.io.IOException;

import kieker.common.record.IMonitoringRecord;
import rocks.fasterxml.jackson.core.JsonGenerator;
import rocks.fasterxml.jackson.databind.JsonNode;
import rocks.fasterxml.jackson.databind.ObjectMapper;
import rocks.fasterxml.jackson.databind.SerializerProvider;
import rocks.fasterxml.jackson.databind.node.ObjectNode;
import rocks.fasterxml.jackson.databind.ser.std.StdSerializer;

public class PlainKiekerSerializer extends StdSerializer<IMonitoringRecord> {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -5302451867765103114L;

	public PlainKiekerSerializer() {
		this(null);
	}
	
	public PlainKiekerSerializer(Class<IMonitoringRecord> type) {
		super(type);
	}

	@Override
	public void serialize(IMonitoringRecord arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		JsonNode tree = (JsonNode) mapper.valueToTree(arg0);
		ObjectNode objNode = (ObjectNode) tree;
		
		objNode.put("type", arg0.getClass().getSimpleName());
		
		arg2.defaultSerializeValue(objNode, arg1);
	}
	
}
