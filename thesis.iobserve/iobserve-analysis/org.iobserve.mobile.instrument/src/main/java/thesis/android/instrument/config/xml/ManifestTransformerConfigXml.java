package thesis.android.instrument.config.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * XML Mapping class for the Android application manifest transformation
 * configuration.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ManifestTransformerConfigXml {

	/**
	 * List of permissions which are needed.
	 */
	@XmlElementWrapper(name = "permissions")
	@XmlElement(name = "permission")
	private List<String> permissions;

	/**
	 * Gets the list of permissions which are needed.
	 * 
	 * @return list of permissions which are needed
	 */
	public List<String> getPermissions() {
		return permissions;
	}

}
