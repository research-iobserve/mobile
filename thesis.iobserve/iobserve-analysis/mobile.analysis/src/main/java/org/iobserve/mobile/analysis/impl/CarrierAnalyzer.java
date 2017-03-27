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
import org.iobserve.analysis.mobile.MobileMobileConnectionInfo;
import org.iobserve.mobile.analysis.AbstractPalladioAnalyzer;
import org.iobserve.mobile.analysis.ConstraintViolation;
import org.iobserve.mobile.analysis.PalladioInstance;

import privacy_ext.CommunicationLinkPrivacy;

/**
 * Analyzer for mobile connections which inspects the carrier of the links.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class CarrierAnalyzer extends AbstractPalladioAnalyzer<MobileMobileConnectionInfo> {

	/**
	 * List of carriers which are whitelisted.
	 */
	private Set<String> carrierList;

	/**
	 * Creates a new instance with a file which provides a whitelist.
	 * 
	 * @param carrList
	 *            path to a file which provides a whitelist of carriers
	 *            line-wise
	 */
	public CarrierAnalyzer(final String carrList) {
		try {
			carrierList = this.provideFile(carrList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<MobileMobileConnectionInfo> extract(final PalladioInstance instance) {
		final List<CommunicationLinkPrivacy> links = getPrivacyLinks(instance);
		final List<MobileMobileConnectionInfo> mobile = new ArrayList<>();

		for (CommunicationLinkPrivacy link : links) {
			final MobileConnectionState state = new MobileConnectionState(link);
			if (state.getConnectionType() == MobileConnectionType.MOBILE) {
				mobile.add((MobileMobileConnectionInfo) state.getConnectionInfo());
			}
		}

		return mobile;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ConstraintViolation isViolation(final MobileMobileConnectionInfo value) {
		if (carrierList.contains(value.getCarrier())) {
			return null;
		} else {
			return new ConstraintViolation("Carrier '" + value.getCarrier() + "' is not listed as a secure carrier.");
		}
	}
}
