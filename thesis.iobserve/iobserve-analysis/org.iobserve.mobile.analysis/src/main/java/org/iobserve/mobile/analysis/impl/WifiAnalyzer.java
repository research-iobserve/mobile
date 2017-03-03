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
package org.iobserve.mobile.analysis.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.iobserve.analysis.mobile.MobileConnectionState;
import org.iobserve.analysis.mobile.MobileConnectionType;
import org.iobserve.analysis.mobile.MobileWifiConnectionInfo;
import org.iobserve.mobile.analysis.AbstractPalladioAnalyzer;
import org.iobserve.mobile.analysis.ConstraintViolation;
import org.iobserve.mobile.analysis.PalladioInstance;

import privacy_ext.CommunicationLinkPrivacy;

/**
 * Class for analyzing all Wifi connections.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class WifiAnalyzer extends AbstractPalladioAnalyzer<MobileWifiConnectionInfo> {

	private Set<String> ssidList;
	private Set<String> bssidList;

	/**
	 * Creates a new instance with two files as input. First a whitelist file
	 * for SSIDs and second a whitelist file for BSSIDs.
	 * 
	 * @param ssidFile
	 *            path to SSID whitelist
	 * @param bssidFile
	 *            path to BSSID whitelist
	 */
	public WifiAnalyzer(final String ssidFile, final String bssidFile) {
		try {
			ssidList = this.provideFile(ssidFile);
			bssidList = this.provideFile(bssidFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<MobileWifiConnectionInfo> extract(final PalladioInstance instance) {
		final List<CommunicationLinkPrivacy> links = getPrivacyLinks(instance);
		final List<MobileWifiConnectionInfo> wifi = new ArrayList<>();

		for (CommunicationLinkPrivacy link : links) {
			final MobileConnectionState state = new MobileConnectionState(link);
			if (state.getConnectionType() == MobileConnectionType.WLAN) {
				wifi.add((MobileWifiConnectionInfo) state.getConnectionInfo());
			}
		}

		return wifi;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ConstraintViolation isViolation(final MobileWifiConnectionInfo value) {
		// BSSID & SSID PRECHECK
		final boolean ssidValid = ssidList.contains(value.getSsid());
		final boolean bssidValid = bssidList.contains(value.getBssid());

		if (!ssidValid || !bssidValid) {
			if (!ssidValid) {
				return new ConstraintViolation("SSID '" + value.getSsid() + "' is not a valid SSID.");
			} else {
				return new ConstraintViolation("BSSID '" + value.getBssid() + "' is not a valid BSSID.");
			}
		}

		// EVIL TWIN CHECK
		if (value.getProtocol().toLowerCase().equals("none")) {
			return new ConstraintViolation("Wifi connection with SSID '" + value.getSsid() + "' and BSSID '"
					+ value.getBssid() + "' is probably victim of the evil twin attack.");
		}

		return null;
	}

}
