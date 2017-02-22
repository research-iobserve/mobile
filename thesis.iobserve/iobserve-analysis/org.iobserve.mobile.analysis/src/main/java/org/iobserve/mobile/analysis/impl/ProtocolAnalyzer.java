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

public class ProtocolAnalyzer extends AbstractPalladioAnalyzer<CommunicationLinkPrivacy> {

	private static final Set<String> SECURE_PROTOCOLS_MOBILE = new HashSet<>(Arrays.asList(new String[] { "lte" }));
	private static final Set<String> SECURE_PROTOCOLS_WIFI = new HashSet<>(
			Arrays.asList(new String[] { "wpa", "wpa2" }));

	@Override
	protected ConstraintViolation isViolation(CommunicationLinkPrivacy value) {
		MobileConnectionState cState = new MobileConnectionState(value);
		if (cState.getConnectionType() == MobileConnectionType.WLAN) {
			MobileWifiConnectionInfo info = (MobileWifiConnectionInfo) cState.getConnectionInfo();
			if (SECURE_PROTOCOLS_WIFI.contains(info.getProtocol())) {
				return null;
			} else {
				return new ConstraintViolation(
						"Protocol '" + info.getProtocol() + "' is not listed as a secure wifi protocol type.");
			}
		} else if (cState.getConnectionType() == MobileConnectionType.MOBILE) {
			MobileMobileConnectionInfo info = (MobileMobileConnectionInfo) cState.getConnectionInfo();
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

	@Override
	protected List<CommunicationLinkPrivacy> extract(PalladioInstance instance) {
		return this.getPrivacyLinks(instance);
	}

}
