package thesis.android.instrument.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class AgentBuildConfiguration {

	@XmlElementWrapper(name = "libraryFolders")
	@XmlElement(name = "folder")
	private List<String> libraryFolders;

	public List<String> getLibraryFolders() {
		return libraryFolders;
	}

}
