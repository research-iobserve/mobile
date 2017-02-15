package thesis.android.instrument.config.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * XML root mapping class which holds the whole XML configuration.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
@XmlRootElement(name = "instrumenter-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class RootConfigurationXml {

	/**
	 * Connection informations.
	 */
	@XmlElement(name = "cmr-bridge")
	private ConnectionInfoXml connectionInfo;

	/**
	 * Manifest transformation configuration.
	 */
	@XmlElement(name = "manifestTransformer")
	private ManifestTransformerConfigXml manifestTransformerConfig;

	/**
	 * Instrumentation configuration.
	 */
	@XmlElement(name = "instrumentation")
	private InstrumentationRootConfigurationXml instrumentationRootConfiguration;

	/**
	 * Agent build configuration.
	 */
	@XmlElement(name = "agentBuild")
	private AgentBuildConfigurationXml agentBuildConfiguration;

	/**
	 * @return the connectionInfo
	 */
	public ConnectionInfoXml getConnectionInfo() {
		return connectionInfo;
	}

	/**
	 * @return the manifestTransformerConfig
	 */
	public ManifestTransformerConfigXml getManifestTransformer() {
		return manifestTransformerConfig;
	}

	/**
	 * @return the instrumentationRootConfiguration
	 */
	public InstrumentationRootConfigurationXml getInstrumentationRootConfiguration() {
		return instrumentationRootConfiguration;
	}

	/**
	 * @return the agentBuildConfiguration
	 */
	public AgentBuildConfigurationXml getAgentBuildConfiguration() {
		return agentBuildConfiguration;
	}

}
