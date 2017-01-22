package rocks.inspectit.android.callback.kieker;

import kieker.common.record.IMonitoringRecord;
import rocks.inspectit.android.callback.data.MobileDefaultData;

/**
 * Created by DMO on 04.01.2017.
 */

public class NetInfoResponse extends MobileDefaultData implements IKiekerCompatible {

	private boolean wifi;
	private boolean mobile;
	private byte security;
	private String ssid;
	private String bssid;

	public NetInfoResponse(boolean wifi, boolean mobile, byte security, String wlanSSID, String wlanBSSID) {
		setWifi(wifi);
		setMobile(mobile);
		setSecurity(security);
		setSsid(wlanSSID);
		setBssid(wlanBSSID);
	}

	/**
	 * To support automatic mapping from Jackson.
	 */
	public NetInfoResponse() {
	}

	@Override
	public IMonitoringRecord generateRecord() {
		if (wifi) {
			return new MobileNetworkEventRecord(ssid, bssid, security);
		} else if (mobile) {
			return new MobileNetworkEventRecord(security);
		} else {
			return null;
		}
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

	public byte getSecurity() {
		return security;
	}

	public void setSecurity(byte security) {
		this.security = security;
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
}
