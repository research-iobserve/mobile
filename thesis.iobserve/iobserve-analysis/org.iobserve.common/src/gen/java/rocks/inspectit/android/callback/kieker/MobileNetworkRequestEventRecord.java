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

package rocks.inspectit.android.callback.kieker;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.Version;

import rocks.inspectit.android.callback.kieker.MobileRecord;
import rocks.inspectit.android.callback.kieker.NetworkEvent;

/**
 * @author Generic Kieker
 * 
 * @since 1.10
 */
public class MobileNetworkRequestEventRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory, MobileRecord, NetworkEvent {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // MobileRecord.deviceId
			 + TYPE_SIZE_STRING // MobileNetworkRequestEventRecord.url
			 + TYPE_SIZE_STRING // MobileNetworkRequestEventRecord.method
			 + TYPE_SIZE_INT // MobileNetworkRequestEventRecord.responseCode
			 + TYPE_SIZE_LONG // MobileNetworkRequestEventRecord.duration
	;
	private static final long serialVersionUID = 8300232394325188760L;
	
	public static final Class<?>[] TYPES = {
		String.class, // MobileRecord.deviceId
		String.class, // MobileNetworkRequestEventRecord.url
		String.class, // MobileNetworkRequestEventRecord.method
		int.class, // MobileNetworkRequestEventRecord.responseCode
		long.class, // MobileNetworkRequestEventRecord.duration
	};
	
	/* user-defined constants */
	/* default constants */
	public static final String DEVICE_ID = "";
	public static final String URL = "";
	public static final String METHOD = "";
	/* property declarations */
	private final String deviceId;
	private final String url;
	private final String method;
	private final int responseCode;
	private final long duration;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param deviceId
	 *            deviceId
	 * @param url
	 *            url
	 * @param method
	 *            method
	 * @param responseCode
	 *            responseCode
	 * @param duration
	 *            duration
	 */
	public MobileNetworkRequestEventRecord(final String deviceId, final String url, final String method, final int responseCode, final long duration) {
		this.deviceId = deviceId == null?"":deviceId;
		this.url = url == null?"":url;
		this.method = method == null?"":method;
		this.responseCode = responseCode;
		this.duration = duration;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public MobileNetworkRequestEventRecord(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.deviceId = (String) values[0];
		this.url = (String) values[1];
		this.method = (String) values[2];
		this.responseCode = (Integer) values[3];
		this.duration = (Long) values[4];
	}
	
	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected MobileNetworkRequestEventRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.deviceId = (String) values[0];
		this.url = (String) values[1];
		this.method = (String) values[2];
		this.responseCode = (Integer) values[3];
		this.duration = (Long) values[4];
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
	public MobileNetworkRequestEventRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.deviceId = stringRegistry.get(buffer.getInt());
		this.url = stringRegistry.get(buffer.getInt());
		this.method = stringRegistry.get(buffer.getInt());
		this.responseCode = buffer.getInt();
		this.duration = buffer.getLong();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getDeviceId(),
			this.getUrl(),
			this.getMethod(),
			this.getResponseCode(),
			this.getDuration()
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getDeviceId());
		stringRegistry.get(this.getUrl());
		stringRegistry.get(this.getMethod());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putInt(stringRegistry.get(this.getDeviceId()));
		buffer.putInt(stringRegistry.get(this.getUrl()));
		buffer.putInt(stringRegistry.get(this.getMethod()));
		buffer.putInt(this.getResponseCode());
		buffer.putLong(this.getDuration());
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
	
	public final String getUrl() {
		return this.url;
	}
	
	public final String getMethod() {
		return this.method;
	}
	
	public final int getResponseCode() {
		return this.responseCode;
	}
	
	public final long getDuration() {
		return this.duration;
	}
	
}
