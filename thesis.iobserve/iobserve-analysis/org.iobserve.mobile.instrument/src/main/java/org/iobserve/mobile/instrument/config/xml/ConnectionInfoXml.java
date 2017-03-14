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
package org.iobserve.mobile.instrument.config.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * XML Mapping class for the agent connection info.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ConnectionInfoXml {

	/**
	 * REST URL for the session creation requests.
	 */
	private String helloUrl;

	/**
	 * REST URL for the beacons.
	 */
	private String beaconUrl;

	/**
	 * Creates a new instance.
	 */
	public ConnectionInfoXml() {
	}

	/**
	 * @return the helloUrl
	 */
	public String getHelloUrl() {
		return helloUrl;
	}

	/**
	 * @param helloUrl
	 *            the helloUrl to set
	 */
	public void setHelloUrl(final String helloUrl) {
		this.helloUrl = helloUrl;
	}

	/**
	 * @return the beaconUrl
	 */
	public String getBeaconUrl() {
		return beaconUrl;
	}

	/**
	 * @param beaconUrl
	 *            the beaconUrl to set
	 */
	public void setBeaconUrl(final String beaconUrl) {
		this.beaconUrl = beaconUrl;
	}

}
