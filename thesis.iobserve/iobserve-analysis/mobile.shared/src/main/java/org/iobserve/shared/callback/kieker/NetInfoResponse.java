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

import org.iobserve.common.mobile.record.MobileNetworkEventRecord;
import org.iobserve.shared.callback.data.MobileDefaultData;

/**
 * Monitoring record for the network connection information of a mobile device.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class NetInfoResponse extends MobileDefaultData implements IKiekerCompatible {
	/**
	 * If the device is connected to a wifi or not.
	 */
	private boolean wifi;

	/**
	 * If the device is connected to a mobile network connection.
	 */
	private boolean mobile;

	/**
	 * The name of the protocol which is used.
	 */
	private String protocolName; // USED BY BOTH

	/**
	 * The name of the network carrier.
	 */
	private String carrierName;

	/**
	 * SSID of the wifi (only set if wifi is true).
	 */
	private String ssid;

	/**
	 * BSSID of the wifi (only set if wifi is true).
	 */
	private String bssid;

	/**
	 * Id of the device.
	 */
	private String deviceId;

	/**
	 * Creates a new record with given values.
	 * 
	 * @param deviceId
	 *            the device id
	 * @param wifi
	 *            if wifi connection or not
	 * @param mobile
	 *            if mobile connection or not
	 * @param protcolName
	 *            name of the protocol
	 * @param carrierName
	 *            name of the carrier
	 * @param wlanSSID
	 *            wifi ssid
	 * @param wlanBSSID
	 *            wifi bssid
	 */
	public NetInfoResponse(final String deviceId, final boolean wifi, final boolean mobile, final String protcolName,
			final String carrierName, final String wlanSSID, final String wlanBSSID) {
		setWifi(wifi);
		setMobile(mobile);
		setSsid(wlanSSID);
		setBssid(wlanBSSID);
		setDeviceId(deviceId);
		setCarrierName(carrierName);
		setProtocolName(protcolName);
	}

	/**
	 * Empty constructor to support automatic mapping from Jackson.
	 */
	public NetInfoResponse() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMonitoringRecord generateRecord() {
		return new MobileNetworkEventRecord(deviceId, wifi, mobile, carrierName, protocolName, ssid, bssid);
	}

	/**
	 * @return the wifi
	 */
	public boolean isWifi() {
		return wifi;
	}

	/**
	 * @param wifi
	 *            the wifi to set
	 */
	public void setWifi(final boolean wifi) {
		this.wifi = wifi;
	}

	/**
	 * @return the mobile
	 */
	public boolean isMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(final boolean mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the protocolName
	 */
	public String getProtocolName() {
		return protocolName;
	}

	/**
	 * @param protocolName
	 *            the protocolName to set
	 */
	public void setProtocolName(final String protocolName) {
		this.protocolName = protocolName;
	}

	/**
	 * @return the carrierName
	 */
	public String getCarrierName() {
		return carrierName;
	}

	/**
	 * @param carrierName
	 *            the carrierName to set
	 */
	public void setCarrierName(final String carrierName) {
		this.carrierName = carrierName;
	}

	/**
	 * @return the ssid
	 */
	public String getSsid() {
		return ssid;
	}

	/**
	 * @param ssid
	 *            the ssid to set
	 */
	public void setSsid(final String ssid) {
		this.ssid = ssid;
	}

	/**
	 * @return the bssid
	 */
	public String getBssid() {
		return bssid;
	}

	/**
	 * @param bssid
	 *            the bssid to set
	 */
	public void setBssid(final String bssid) {
		this.bssid = bssid;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(final String deviceId) {
		this.deviceId = deviceId;
	}
}
