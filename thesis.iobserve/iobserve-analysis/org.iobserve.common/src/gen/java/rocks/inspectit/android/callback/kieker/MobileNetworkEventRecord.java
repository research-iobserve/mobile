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

/**
 * @author Generic Kieker
 * 
 * @since 1.10
 */
public class MobileNetworkEventRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory, MobileRecord {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // MobileRecord.deviceId
			 + TYPE_SIZE_BOOLEAN // MobileNetworkEventRecord.wifi
			 + TYPE_SIZE_BOOLEAN // MobileNetworkEventRecord.mobile
			 + TYPE_SIZE_STRING // MobileNetworkEventRecord.mobileOperator
			 + TYPE_SIZE_STRING // MobileNetworkEventRecord.protocol
			 + TYPE_SIZE_STRING // MobileNetworkEventRecord.wSSID
			 + TYPE_SIZE_STRING // MobileNetworkEventRecord.wBSSID
	;
	private static final long serialVersionUID = 4719845710182256176L;
	
	public static final Class<?>[] TYPES = {
		String.class, // MobileRecord.deviceId
		boolean.class, // MobileNetworkEventRecord.wifi
		boolean.class, // MobileNetworkEventRecord.mobile
		String.class, // MobileNetworkEventRecord.mobileOperator
		String.class, // MobileNetworkEventRecord.protocol
		String.class, // MobileNetworkEventRecord.wSSID
		String.class, // MobileNetworkEventRecord.wBSSID
	};
	
	/* user-defined constants */
	/* default constants */
	public static final String DEVICE_ID = "";
	public static final String MOBILE_OPERATOR = "";
	public static final String PROTOCOL = "";
	public static final String W_SSID = "";
	public static final String W_BSSID = "";
	/* property declarations */
	private final String deviceId;
	private final boolean wifi;
	private final boolean mobile;
	private final String mobileOperator;
	private final String protocol;
	private final String wSSID;
	private final String wBSSID;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param deviceId
	 *            deviceId
	 * @param wifi
	 *            wifi
	 * @param mobile
	 *            mobile
	 * @param mobileOperator
	 *            mobileOperator
	 * @param protocol
	 *            protocol
	 * @param wSSID
	 *            wSSID
	 * @param wBSSID
	 *            wBSSID
	 */
	public MobileNetworkEventRecord(final String deviceId, final boolean wifi, final boolean mobile, final String mobileOperator, final String protocol, final String wSSID, final String wBSSID) {
		this.deviceId = deviceId == null?"":deviceId;
		this.wifi = wifi;
		this.mobile = mobile;
		this.mobileOperator = mobileOperator == null?"":mobileOperator;
		this.protocol = protocol == null?"":protocol;
		this.wSSID = wSSID == null?"":wSSID;
		this.wBSSID = wBSSID == null?"":wBSSID;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public MobileNetworkEventRecord(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.deviceId = (String) values[0];
		this.wifi = (Boolean) values[1];
		this.mobile = (Boolean) values[2];
		this.mobileOperator = (String) values[3];
		this.protocol = (String) values[4];
		this.wSSID = (String) values[5];
		this.wBSSID = (String) values[6];
	}
	
	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected MobileNetworkEventRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.deviceId = (String) values[0];
		this.wifi = (Boolean) values[1];
		this.mobile = (Boolean) values[2];
		this.mobileOperator = (String) values[3];
		this.protocol = (String) values[4];
		this.wSSID = (String) values[5];
		this.wBSSID = (String) values[6];
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
	public MobileNetworkEventRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.deviceId = stringRegistry.get(buffer.getInt());
		this.wifi = buffer.get()==1?true:false;
		this.mobile = buffer.get()==1?true:false;
		this.mobileOperator = stringRegistry.get(buffer.getInt());
		this.protocol = stringRegistry.get(buffer.getInt());
		this.wSSID = stringRegistry.get(buffer.getInt());
		this.wBSSID = stringRegistry.get(buffer.getInt());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getDeviceId(),
			this.isWifi(),
			this.isMobile(),
			this.getMobileOperator(),
			this.getProtocol(),
			this.getWSSID(),
			this.getWBSSID()
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getDeviceId());
		stringRegistry.get(this.getMobileOperator());
		stringRegistry.get(this.getProtocol());
		stringRegistry.get(this.getWSSID());
		stringRegistry.get(this.getWBSSID());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putInt(stringRegistry.get(this.getDeviceId()));
		buffer.put((byte)(this.isWifi()?1:0));
		buffer.put((byte)(this.isMobile()?1:0));
		buffer.putInt(stringRegistry.get(this.getMobileOperator()));
		buffer.putInt(stringRegistry.get(this.getProtocol()));
		buffer.putInt(stringRegistry.get(this.getWSSID()));
		buffer.putInt(stringRegistry.get(this.getWBSSID()));
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
	
	public final boolean isWifi() {
		return this.wifi;
	}
	
	public final boolean isMobile() {
		return this.mobile;
	}
	
	public final String getMobileOperator() {
		return this.mobileOperator;
	}
	
	public final String getProtocol() {
		return this.protocol;
	}
	
	public final String getWSSID() {
		return this.wSSID;
	}
	
	public final String getWBSSID() {
		return this.wBSSID;
	}
	
}
