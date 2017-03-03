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
	private static final ObjectMapper MAPPER = new ObjectMapper();

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
	public PlainKiekerSerializer(final Class<IMonitoringRecord> type) {
		super(type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IMonitoringRecord arg0, final JsonGenerator arg1, final SerializerProvider arg2)
			throws IOException {
		final JsonNode tree = (JsonNode) MAPPER.valueToTree(arg0);
		final ObjectNode objNode = (ObjectNode) tree;

		objNode.put("type", arg0.getClass().getSimpleName());

		arg2.defaultSerializeValue(objNode, arg1);
	}

}
