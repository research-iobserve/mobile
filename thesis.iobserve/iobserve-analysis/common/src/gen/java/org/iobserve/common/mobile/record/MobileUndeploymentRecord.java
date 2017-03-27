/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package org.iobserve.common.mobile.record;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.Version;

import org.iobserve.common.mobile.record.MobileRecord;
import org.iobserve.common.record.IUndeploymentRecord;

/**
 * @author Generic Kieker
 * 
 * @since 1.10
 */
public class MobileUndeploymentRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory, MobileRecord, IUndeploymentRecord {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // MobileRecord.deviceId
			 + TYPE_SIZE_STRING // MobileUndeploymentRecord.activityName
	;
	private static final long serialVersionUID = 4740054219719127322L;
	
	public static final Class<?>[] TYPES = {
		String.class, // MobileRecord.deviceId
		String.class, // MobileUndeploymentRecord.activityName
	};
	
	/* user-defined constants */
	/* default constants */
	public static final String DEVICE_ID = "";
	public static final String ACTIVITY_NAME = "";
	/* property declarations */
	private final String deviceId;
	private final String activityName;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param deviceId
	 *            deviceId
	 * @param activityName
	 *            activityName
	 */
	public MobileUndeploymentRecord(final String deviceId, final String activityName) {
		this.deviceId = deviceId == null?"":deviceId;
		this.activityName = activityName == null?"":activityName;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public MobileUndeploymentRecord(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.deviceId = (String) values[0];
		this.activityName = (String) values[1];
	}
	
	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected MobileUndeploymentRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.deviceId = (String) values[0];
		this.activityName = (String) values[1];
	}

	/**
	 * This constructor converts the given array into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record.
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public MobileUndeploymentRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.deviceId = stringRegistry.get(buffer.getInt());
		this.activityName = stringRegistry.get(buffer.getInt());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getDeviceId(),
			this.getActivityName()
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getDeviceId());
		stringRegistry.get(this.getActivityName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putInt(stringRegistry.get(this.getDeviceId()));
		buffer.putInt(stringRegistry.get(this.getActivityName()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize() {
		return SIZE;
	}
	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}

	public final String getDeviceId() {
		return this.deviceId;
	}
	
	public final String getActivityName() {
		return this.activityName;
	}
	
}
