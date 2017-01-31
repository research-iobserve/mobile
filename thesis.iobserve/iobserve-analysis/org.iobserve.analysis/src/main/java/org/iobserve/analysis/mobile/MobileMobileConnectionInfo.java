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
 * Class which contains information about a mobile connection of a mobile
 * device.
 * 
 * @author David Monschein
 *
 */
public class MobileMobileConnectionInfo extends AbstractMobileConnectionInfo {

	/**
	 * The protocol of which is used by the connection.
	 */
	private String protocol;

	/**
	 * The carrier which is used.
	 */
	private String provider;

	/**
	 * Creates a new instance with a given protocol and a given provider.
	 * 
	 * @param protocol
	 *            the name of the protocol
	 * @param provider
	 *            the name of the carrier
	 */
	public MobileMobileConnectionInfo(final String protocol, final String provider) {
		super();
		this.protocol = protocol;
		this.provider = provider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MobileConnectionType getCorrespondingType() {
		return MobileConnectionType.MOBILE;
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

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @param provider
	 *            the provider to set
	 */
	public void setProvider(final String provider) {
		this.provider = provider;
	}

}
