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
package org.iobserve.shared.callback.kieker;

import java.io.IOException;

import org.iobserve.common.mobile.record.MobileDeploymentRecord;
import org.iobserve.common.mobile.record.MobileUndeploymentRecord;

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
 * Used to deserialize a {@link PlainKieker} object at the server side.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class PlainKiekerDeserializer extends JsonDeserializer<IMonitoringRecord> {

	/**
	 * Default constructor for Jackson mapper.
	 */
	public PlainKiekerDeserializer() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMonitoringRecord deserialize(final JsonParser jp, final DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		final JsonNode node = jp.getCodec().readTree(jp);

		final String type = node.get("type").asText();

		if ("BeforeOperationEvent".equals(type)) {
			final long timestamp = node.get("timestamp").asLong();
			final long traceId = node.get("traceId").asLong();
			final String clazzSig = formatClazz(node.get("classSignature").asText());
			final String operationSig = formatOperation(clazzSig, node.get("operationSignature").asText());
			final int orderIndex = node.get("orderIndex").asInt();

			return new BeforeOperationEvent(timestamp, traceId, orderIndex, operationSig, clazzSig);
		} else if ("AfterOperationFailedEvent".equals(type)) {
			final long timestamp = node.get("timestamp").asLong();
			final long traceId = node.get("traceId").asLong();
			final String clazzSig = formatClazz(node.get("classSignature").asText());
			final String operationSig = formatOperation(clazzSig, node.get("operationSignature").asText());
			final int orderIndex = node.get("orderIndex").asInt();
			final String cause = node.get("cause").asText();

			return new AfterOperationFailedEvent(timestamp, traceId, orderIndex, operationSig, clazzSig, cause);
		} else if ("AfterOperationEvent".equals(type)) {
			final long timestamp = node.get("timestamp").asLong();
			final long traceId = node.get("traceId").asLong();
			final String clazzSig = formatClazz(node.get("classSignature").asText());
			final String operationSig = formatOperation(clazzSig, node.get("operationSignature").asText());
			final int orderIndex = node.get("orderIndex").asInt();

			return new AfterOperationEvent(timestamp, traceId, orderIndex, operationSig, clazzSig);
		} else if ("TraceMetadata".equals(type)) {
			final long traceId = node.get("traceId").asLong();
			final long threadId = node.get("threadId").asLong();
			final String sessionId = node.get("sessionId").asText();
			final String hostname = node.get("hostname").asText();
			final long parentTraceId = node.get("parentTraceId").asLong();
			final int parentOrderId = node.get("parentOrderId").asInt();

			return new TraceMetadata(traceId, threadId, sessionId, hostname, parentTraceId, parentOrderId);
		} else if ("MobileDeploymentRecord".equals(type)) {
			final String activity = node.get("activityName").asText();
			final String deviceId = node.get("deviceId").asText();

			return new MobileDeploymentRecord(deviceId, activity);
		} else if ("MobileUndeploymentRecord".equals(type)) {
			final String activity = node.get("activityName").asText();
			final String deviceId = node.get("deviceId").asText();

			return new MobileUndeploymentRecord(deviceId, activity);
		}

		return null;
	}

	/**
	 * Formats a class by converting the internal representation to the original
	 * class name.
	 * 
	 * @param clazzz
	 *            internal representation of the class
	 * @return original class name
	 */
	private String formatClazz(final String clazzz) {
		return clazzz.replaceAll("/", ".");
	}

	/**
	 * Formats a operation given in the internal representation format.
	 * 
	 * @param operation
	 *            operation in the internal representation format
	 * @return reformatted operation in nearly original form
	 */
	private String formatOperation(final String clazz, final String operation) {
		final String[] opSplit = operation.split("\\)");
		String op = operation;
		if (opSplit.length == 2) {
			op = opSplit[0] + ")"; // remove return type
		}
		return clazz + "." + op.replaceAll(";", "").replaceAll("/", ".").replaceAll("L", "");
	}

}
