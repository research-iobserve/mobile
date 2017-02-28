package org.iobserve.shared.callback.kieker;

import java.io.IOException;

import kieker.common.record.IMonitoringRecord;
import rocks.fasterxml.jackson.core.JsonGenerator;
import rocks.fasterxml.jackson.databind.JsonNode;
import rocks.fasterxml.jackson.databind.ObjectMapper;
import rocks.fasterxml.jackson.databind.SerializerProvider;
import rocks.fasterxml.jackson.databind.node.ObjectNode;
import rocks.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Serializes a montiroing record at the agent side to be able to send it as a
 * JSON string.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class PlainKiekerSerializer extends StdSerializer<IMonitoringRecord> {

	/**
	 * Jackson object mapper to work with JSON.
	 */
	private static final ObjectMapper mapper = new ObjectMapper();

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -5302451867765103114L;

	/**
	 * Create a new serializer.
	 */
	public PlainKiekerSerializer() {
		this(null);
	}

	/**
	 * Create a new serializer for a specific type.
	 * 
	 * @param type
	 *            the type
	 */
	public PlainKiekerSerializer(Class<IMonitoringRecord> type) {
		super(type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(IMonitoringRecord arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
		JsonNode tree = (JsonNode) mapper.valueToTree(arg0);
		ObjectNode objNode = (ObjectNode) tree;

		objNode.put("type", arg0.getClass().getSimpleName());

		arg2.defaultSerializeValue(objNode, arg1);
	}

}
