package org.iobserve.analysis.mobile;

public class MobileWifiConnectionInfo extends MobileConnectionInfo {

	private String ssid;
	private String bssid;
	private String protocol;

	public MobileWifiConnectionInfo(String ssid, String bssid, String protocol) {
		super();
		this.ssid = ssid;
		this.bssid = bssid;
		this.protocol = protocol;
	}

	@Override
	public MobileConnectionType getCorrespondingType() {
		return MobileConnectionType.WLAN;
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

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
