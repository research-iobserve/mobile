package org.iobserve.mobile.analysis.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.iobserve.analysis.mobile.MobileConnectionState;
import org.iobserve.analysis.mobile.MobileConnectionType;
import org.iobserve.analysis.mobile.MobileMobileConnectionInfo;
import org.iobserve.mobile.analysis.AbstractPalladioAnalyzer;
import org.iobserve.mobile.analysis.ConstraintViolation;
import org.iobserve.mobile.analysis.PalladioInstance;

import privacy_ext.CommunicationLinkPrivacy;

public class CarrierAnalyzer extends AbstractPalladioAnalyzer<MobileMobileConnectionInfo> {

	private Set<String> carrierList;

	public CarrierAnalyzer(String carrList) {
		try {
			carrierList = this.provideFile(carrList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected List<MobileMobileConnectionInfo> extract(PalladioInstance instance) {
		List<CommunicationLinkPrivacy> links = getPrivacyLinks(instance);
		List<MobileMobileConnectionInfo> mobile = new ArrayList<>();

		for (CommunicationLinkPrivacy link : links) {
			MobileConnectionState state = new MobileConnectionState(link);
			if (state.getConnectionType() == MobileConnectionType.MOBILE) {
				mobile.add((MobileMobileConnectionInfo) state.getConnectionInfo());
			}
		}

		return mobile;
	}

	@Override
	protected ConstraintViolation isViolation(MobileMobileConnectionInfo value) {
		if (carrierList.contains(value.getCarrier())) {
			return null;
		} else {
			return new ConstraintViolation("Carrier '" + value.getCarrier() + "' is not listed as a secure carrier.");
		}
	}
}
