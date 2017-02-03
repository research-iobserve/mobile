/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.mobile;

import rocks.inspectit.android.callback.kieker.MobileNetworkEventRecord;

/**
 * Represents the current network connection of a mobile device.
 * 
 * @author David Monschein
 *
 */
public class MobileConnectionState {

	/**
	 * Whether the device has an active connection or not.
	 */
	private boolean connected;

	/**
	 * The type of the connection.
	 */
	private MobileConnectionType connectionType;

	/**
	 * The connection info for the specific type.
	 */
	private AbstractMobileConnectionInfo connectionInfo;

	/**
	 * Creates a new instance which represents a network connection of a mobile
	 * device.
	 * 
	 * @param connected
	 *            whether the device has an active connection
	 * @param connectionType
	 *            the type of the connection
	 * @param connectionInfo
	 *            information about the current connection
	 */
	public MobileConnectionState(final boolean connected, final MobileConnectionType connectionType,
			final AbstractMobileConnectionInfo connectionInfo) {
		super();
		this.connected = connected;
		this.connectionType = connectionType;
		this.connectionInfo = connectionInfo;
	}

	/**
	 * Creates a new instance out of an mobile network event record.
	 * 
	 * @param record
	 *            the kieker record with the type
	 *            {@link MobileNetworkEventRecord}
	 */
	public MobileConnectionState(final MobileNetworkEventRecord record) {
		this.setConnected(record.isWifi() || record.isMobile());

		// set connection type
		if (record.isWifi()) {
			this.setConnectionType(MobileConnectionType.WLAN);
			this.setConnectionInfo(
					new MobileWifiConnectionInfo(record.getWSSID(), record.getWBSSID(), record.getProtocol()));
		} else if (record.isMobile()) {
			this.setConnectionType(MobileConnectionType.MOBILE);
			this.setConnectionInfo(new MobileMobileConnectionInfo(record.getProtocol(), record.getMobileCarrierName()));
		}
	}

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * @param connected
	 *            the connected to set
	 */
	public void setConnected(final boolean connected) {
		this.connected = connected;
	}

	/**
	 * @return the connectionType
	 */
	public MobileConnectionType getConnectionType() {
		return connectionType;
	}

	/**
	 * @param connectionType
	 *            the connectionType to set
	 */
	public void setConnectionType(final MobileConnectionType connectionType) {
		this.connectionType = connectionType;
	}

	/**
	 * @return the connectionInfo
	 */
	public AbstractMobileConnectionInfo getConnectionInfo() {
		return connectionInfo;
	}

	/**
	 * @param connectionInfo
	 *            the connectionInfo to set
	 */
	public void setConnectionInfo(final AbstractMobileConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

}
