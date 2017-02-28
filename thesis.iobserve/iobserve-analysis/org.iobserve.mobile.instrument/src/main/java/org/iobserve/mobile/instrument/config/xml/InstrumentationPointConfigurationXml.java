package org.iobserve.mobile.instrument.config.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * XML Mapping class for the instrumentation point configuration.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class InstrumentationPointConfigurationXml {

	/**
	 * Type of the instrumentation point.
	 */
	@XmlAttribute(name = "type")
	private String type;

	/**
	 * Value of the instrumentation point.
	 */
	@XmlValue
	private String value;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

}
