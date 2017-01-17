package thesis.android.instrument.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class Permission {

	@XmlValue
	private String permission;

	public String getName() {
		return permission;
	}

}
