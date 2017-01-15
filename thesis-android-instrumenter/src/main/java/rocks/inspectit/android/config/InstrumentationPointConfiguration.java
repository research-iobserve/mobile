package rocks.inspectit.android.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class InstrumentationPointConfiguration {

	@XmlAttribute(name = "type")
	private String type;

	@XmlValue
	private String value;

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

}
