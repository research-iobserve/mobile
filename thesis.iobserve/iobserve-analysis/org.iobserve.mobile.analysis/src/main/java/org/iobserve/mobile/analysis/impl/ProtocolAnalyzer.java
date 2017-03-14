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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.iobserve.analysis.mobile.MobileConnectionState;
import org.iobserve.analysis.mobile.MobileConnectionType;
import org.iobserve.analysis.mobile.MobileMobileConnectionInfo;
import org.iobserve.analysis.mobile.MobileWifiConnectionInfo;
import org.iobserve.mobile.analysis.AbstractPalladioAnalyzer;
import org.iobserve.mobile.analysis.ConstraintViolation;
import org.iobserve.mobile.analysis.PalladioInstance;

import privacy_ext.CommunicationLinkPrivacy;

/**
 * Creates a new protocol analyzer instance which checks the connection protocol
 * for links.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class ProtocolAnalyzer extends AbstractPalladioAnalyzer<CommunicationLinkPrivacy> {

	private static final Set<String> SECURE_PROTOCOLS_MOBILE = new HashSet<>(Arrays.asList(new String[] { "lte" }));
	private static final Set<String> SECURE_PROTOCOLS_WIFI = new HashSet<>(
			Arrays.asList(new String[] { "wpa", "wpa2" }));

	/**
	 * Creates a new instance.
	 */
	public ProtocolAnalyzer() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ConstraintViolation isViolation(final CommunicationLinkPrivacy value) {
		final MobileConnectionState cState = new MobileConnectionState(value);
		if (cState.getConnectionType() == MobileConnectionType.WLAN) {
			final MobileWifiConnectionInfo info = (MobileWifiConnectionInfo) cState.getConnectionInfo();
			if (SECURE_PROTOCOLS_WIFI.contains(info.getProtocol())) {
				return null;
			} else {
				return new ConstraintViolation(
						"Protocol '" + info.getProtocol() + "' is not listed as a secure wifi protocol type.");
			}
		} else if (cState.getConnectionType() == MobileConnectionType.MOBILE) {
			final MobileMobileConnectionInfo info = (MobileMobileConnectionInfo) cState.getConnectionInfo();
			if (SECURE_PROTOCOLS_MOBILE.contains(info.getProtocol())) {
				return null;
			} else {
				return new ConstraintViolation(
						"Protocol '" + info.getProtocol() + "' is not listed as a secure mobile protocol type.");
			}
		}

		// no violation because we don't know the connection type
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<CommunicationLinkPrivacy> extract(final PalladioInstance instance) {
		return this.getPrivacyLinks(instance);
	}

}
