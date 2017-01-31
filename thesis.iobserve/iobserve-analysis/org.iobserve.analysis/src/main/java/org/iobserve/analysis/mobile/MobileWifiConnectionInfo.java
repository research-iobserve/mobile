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

/**
 * Class holding information about a wireless connection.
 * 
 * @author David Monschein
 *
 */
public class MobileWifiConnectionInfo extends AbstractMobileConnectionInfo {

	/**
	 * The SSID of the router belonging to the WLAN connection.
	 */
	private String ssid;

	/**
	 * The BSSID of the router belonging to the WLAN connection.
	 */
	private String bssid;

	/**
	 * The protocol which is used by the router.
	 */
	private String protocol;

	/**
	 * Creates a new instance which holds information about a specific wifi
	 * connection.
	 * 
	 * @param ssid
	 *            the SSID of the router
	 * @param bssid
	 *            the BSSID of the router
	 * @param protocol
	 *            the protocol which is used by the router
	 */
	public MobileWifiConnectionInfo(final String ssid, final String bssid, final String protocol) {
		super();
		this.ssid = ssid;
		this.bssid = bssid;
		this.protocol = protocol;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MobileConnectionType getCorrespondingType() {
		return MobileConnectionType.WLAN;
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
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol(final String protocol) {
		this.protocol = protocol;
	}

}
