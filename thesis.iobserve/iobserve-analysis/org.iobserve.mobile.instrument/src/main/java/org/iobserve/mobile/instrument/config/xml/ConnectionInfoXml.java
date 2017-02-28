package org.iobserve.mobile.instrument.config.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * XML Mapping class for the agent connection info.
 * 
 * @author David Monschein
 * @author Robert Heinrich
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
	 * @return the helloUrl
	 */
	public String getHelloUrl() {
		return helloUrl;
	}

	/**
	 * @param helloUrl
	 *            the helloUrl to set
	 */
	public void setHelloUrl(String helloUrl) {
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
	public void setBeaconUrl(String beaconUrl) {
		this.beaconUrl = beaconUrl;
	}

}
