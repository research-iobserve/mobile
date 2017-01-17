package rocks.inspectit.android.callback.kieker;

import java.io.IOException;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import rocks.fasterxml.jackson.core.JsonParser;
import rocks.fasterxml.jackson.core.JsonProcessingException;
import rocks.fasterxml.jackson.databind.DeserializationContext;
import rocks.fasterxml.jackson.databind.JsonDeserializer;
import rocks.fasterxml.jackson.databind.JsonNode;

/**
 * @author David Monschein
 *
 */
public class PlainKiekerDeserializer extends JsonDeserializer<IMonitoringRecord> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMonitoringRecord deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		String type = node.get("type").asText();

		if (type.equals("BeforeOperationEvent")) {
			long timestamp = node.get("timestamp").asLong();
			long traceId = node.get("traceId").asLong();
			String clazzSig = node.get("classSignature").asText();
			String operationSig = node.get("operationSignature").asText();
			int orderIndex = node.get("orderIndex").asInt();

			return new BeforeOperationEvent(timestamp, traceId, orderIndex, operationSig, clazzSig);
		} else if (type.equals("AfterOperationFailedEvent")) {
			long timestamp = node.get("timestamp").asLong();
			long traceId = node.get("traceId").asLong();
			String clazzSig = node.get("classSignature").asText();
			String operationSig = node.get("operationSignature").asText();
			int orderIndex = node.get("orderIndex").asInt();
			String cause = node.get("cause").asText();

			return new AfterOperationFailedEvent(timestamp, traceId, orderIndex, operationSig, clazzSig, cause);
		} else if (type.equals("AfterOperationEvent")) {
			long timestamp = node.get("timestamp").asLong();
			long traceId = node.get("traceId").asLong();
			String clazzSig = node.get("classSignature").asText();
			String operationSig = node.get("operationSignature").asText();
			int orderIndex = node.get("orderIndex").asInt();

			return new AfterOperationEvent(timestamp, traceId, orderIndex, operationSig, clazzSig);
		} else if (type.equals("TraceMetadata")) {
			long traceId = node.get("traceId").asLong();
			long threadId = node.get("threadId").asLong();
			String sessionId = node.get("sessionId").asText();
			String hostname = node.get("hostname").asText();
			long parentTraceId = node.get("parentTraceId").asLong();
			int parentOrderId = node.get("parentOrderId").asInt();

			return new TraceMetadata(traceId, threadId, sessionId, hostname, parentTraceId, parentOrderId);
		}

		return null;
	}

}
