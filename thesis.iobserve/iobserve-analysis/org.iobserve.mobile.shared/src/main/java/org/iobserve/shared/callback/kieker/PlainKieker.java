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

import kieker.common.record.IMonitoringRecord;

import org.iobserve.shared.callback.data.MobileDefaultData;

import rocks.fasterxml.jackson.databind.annotation.JsonDeserialize;
import rocks.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Monitoring record which wraps a Kieker record.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class PlainKieker extends MobileDefaultData implements IKiekerCompatible {

	/**
	 * The Kieker record.
	 */
	@JsonSerialize(using = PlainKiekerSerializer.class)
	@JsonDeserialize(using = PlainKiekerDeserializer.class)
	private IMonitoringRecord record;

	/**
	 * Creates an empty instance.
	 */
	public PlainKieker() {
	}

	/**
	 * Creates a new instance which holds a Kieker monitoring record.
	 * 
	 * @param record
	 *            Kieker monitoring record.
	 */
	public PlainKieker(final IMonitoringRecord record) {
		this.record = record;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMonitoringRecord generateRecord() {
		return this.record;
	}
}
