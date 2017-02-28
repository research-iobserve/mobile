package org.iobserve.mobile.instrument.config.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlValue;

/**
 * XML Mapping class for a single Android application permission.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PermissionXml {

	/**
	 * Name of the permission.
	 */
	@XmlValue
	private String permission;

	/**
	 * Gets the name of the permission.
	 * 
	 * @return the name of the permission
	 */
	public String getName() {
		return permission;
	}

}
