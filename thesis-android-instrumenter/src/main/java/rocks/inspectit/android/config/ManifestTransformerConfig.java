package rocks.inspectit.android.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class ManifestTransformerConfig {

	@XmlElementWrapper(name = "permissions")
	@XmlElement(name = "permission")
	private List<String> permissions;

	public List<String> getPermissions() {
		return permissions;
	}

}
