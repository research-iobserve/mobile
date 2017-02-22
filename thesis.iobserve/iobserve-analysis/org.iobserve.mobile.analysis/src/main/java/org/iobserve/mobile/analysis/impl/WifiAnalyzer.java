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

public class WifiAnalyzer extends AbstractPalladioAnalyzer<MobileWifiConnectionInfo> {

	private Set<String> ssidList;
	private Set<String> bssidList;

	public WifiAnalyzer(String ssidFile, String bssidFile) {
		try {
			ssidList = this.provideFile(ssidFile);
			bssidList = this.provideFile(bssidFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected List<MobileWifiConnectionInfo> extract(PalladioInstance instance) {
		List<CommunicationLinkPrivacy> links = getPrivacyLinks(instance);
		List<MobileWifiConnectionInfo> wifi = new ArrayList<>();

		for (CommunicationLinkPrivacy link : links) {
			MobileConnectionState state = new MobileConnectionState(link);
			if (state.getConnectionType() == MobileConnectionType.WLAN) {
				wifi.add((MobileWifiConnectionInfo) state.getConnectionInfo());
			}
		}

		return wifi;
	}

	@Override
	protected ConstraintViolation isViolation(MobileWifiConnectionInfo value) {
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
