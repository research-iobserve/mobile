package rocks.inspectit.android.callback.kieker;

import kieker.common.record.IMonitoringRecord;
import rocks.inspectit.android.callback.data.MobileDefaultData;

/**
 * Created by DMO on 04.01.2017.
 */

public class NetInfoResponse extends MobileDefaultData implements IKiekerCompatible {

	private boolean wifi;
	private boolean mobile;
	private String protocolName; // USED BY BOTH
	private String operatorName;
	private String ssid;
	private String bssid;
	private String deviceId;

	public NetInfoResponse(String deviceId, boolean wifi, boolean mobile, String protcolName, String operatorName,
			String wlanSSID, String wlanBSSID) {
		setWifi(wifi);
		setMobile(mobile);
		setSsid(wlanSSID);
		setBssid(wlanBSSID);
		setDeviceId(deviceId);
		setOperatorName(operatorName);
		setProtocolName(protcolName);
	}

	/**
	 * To support automatic mapping from Jackson.
	 */
	public NetInfoResponse() {
	}

	@Override
	public IMonitoringRecord generateRecord() {
		return new MobileNetworkEventRecord(deviceId, wifi, mobile, operatorName, protocolName, ssid, bssid);
	}

	public boolean isWifi() {
		return wifi;
	}

	public void setWifi(boolean wifi) {
		this.wifi = wifi;
	}

	public boolean isMobile() {
		return mobile;
	}

	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getBssid() {
		return bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
}
