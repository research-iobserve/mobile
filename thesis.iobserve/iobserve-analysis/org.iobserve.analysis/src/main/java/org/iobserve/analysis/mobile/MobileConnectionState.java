package org.iobserve.analysis.mobile;

import rocks.inspectit.android.callback.kieker.MobileNetworkEventRecord;

public class MobileConnectionState {

	private boolean connected;

	private MobileConnectionType connectionType;
	private MobileConnectionInfo connectionInfo;

	public MobileConnectionState(boolean connected, MobileConnectionType connectionType,
			MobileConnectionInfo connectionInfo) {
		super();
		this.connected = connected;
		this.connectionType = connectionType;
		this.connectionInfo = connectionInfo;
	}

	public MobileConnectionState(MobileNetworkEventRecord record) {
		this.setConnected(record.isWifi() || record.isMobile());

		// set connection type
		if (record.isWifi()) {
			this.setConnectionType(MobileConnectionType.WLAN);
			this.setConnectionInfo(
					new MobileWifiConnectionInfo(record.getWSSID(), record.getWBSSID(), record.getProtocol()));
		} else if (record.isMobile()) {
			this.setConnectionType(MobileConnectionType.MOBILE);
			this.setConnectionInfo(new MobileMobileConnectionInfo(record.getProtocol(), record.getMobileOperator()));
		}
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public MobileConnectionType getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(MobileConnectionType connectionType) {
		this.connectionType = connectionType;
	}

	public MobileConnectionInfo getConnectionInfo() {
		return connectionInfo;
	}

	public void setConnectionInfo(MobileConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

}
