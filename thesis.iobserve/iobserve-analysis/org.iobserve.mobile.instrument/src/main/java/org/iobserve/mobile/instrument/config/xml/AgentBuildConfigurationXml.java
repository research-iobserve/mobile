package org.iobserve.mobile.instrument.config.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * XML Mapping class for the agent build options.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AgentBuildConfigurationXml {

	/**
	 * Folders which are included in the agent build.
	 */
	@XmlElementWrapper(name = "libraryFolders")
	@XmlElement(name = "folder")
	private List<String> libraryFolders;

	/**
	 * Gets the list of folders which are included in the agent build.
	 * 
	 * @return list of folders which are included in the agent build
	 */
	public List<String> getLibraryFolders() {
		return libraryFolders;
	}

}
