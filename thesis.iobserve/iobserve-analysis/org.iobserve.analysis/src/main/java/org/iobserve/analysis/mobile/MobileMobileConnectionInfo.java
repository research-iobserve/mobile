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
 * @author Robert Heinrich
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
	private String carrier;

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
		this.carrier = provider;
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
	public void setCarrier(final String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the provider
	 */
	public String getCarrier() {
		return carrier;
	}

	/**
	 * @param provider
	 *            the provider to set
	 */
	public void setProvider(final String provider) {
		this.carrier = provider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
		result = prime * result + ((carrier == null) ? 0 : carrier.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MobileMobileConnectionInfo other = (MobileMobileConnectionInfo) obj;
		if (protocol == null) {
			if (other.protocol != null)
				return false;
		} else if (!protocol.equals(other.protocol))
			return false;
		if (carrier == null) {
			if (other.carrier != null)
				return false;
		} else if (!carrier.equals(other.carrier))
			return false;
		return true;
	}

}
