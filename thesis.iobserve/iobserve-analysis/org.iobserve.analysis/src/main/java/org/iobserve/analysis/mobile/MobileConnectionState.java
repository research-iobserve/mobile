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

import org.iobserve.common.mobile.record.MobileNetworkEventRecord;

import privacy_ext.CommunicationLinkPrivacy;

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
	 * Creates a new instance out of a {@link CommunicationLinkPrivacy}
	 * instance.
	 * 
	 * @param link
	 *            the privacy communication link instance
	 */
	public MobileConnectionState(final CommunicationLinkPrivacy link) {
		// set connection type
		this.setConnectionType(MobileConnectionType.get(link.getConnectionType()));
		if (this.getConnectionType() == MobileConnectionType.WLAN) {
			this.setConnected(true);
			this.setConnectionInfo(new MobileWifiConnectionInfo(link.getSsid(), link.getBssid(), link.getProtocol()));
		} else if (this.getConnectionType() == MobileConnectionType.MOBILE) {
			this.setConnected(true);
			this.setConnectionInfo(new MobileMobileConnectionInfo(link.getProtocol(), link.getCarrier()));
		} else {
			this.setConnected(false);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (connected ? 1231 : 1237);
		result = prime * result + ((connectionInfo == null) ? 0 : connectionInfo.hashCode());
		result = prime * result + ((connectionType == null) ? 0 : connectionType.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MobileConnectionState other = (MobileConnectionState) obj;
		if (connected != other.connected)
			return false;
		if (connectionInfo == null) {
			if (other.connectionInfo != null)
				return false;
		} else if (!connectionInfo.equals(other.connectionInfo))
			return false;
		if (connectionType != other.connectionType)
			return false;
		return true;
	}

}
