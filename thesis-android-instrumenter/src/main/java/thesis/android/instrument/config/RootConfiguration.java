package thesis.android.instrument.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "instrumenter-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class RootConfiguration {

	@XmlElement(name = "cmr-bridge")
	private ConnectionInfo connectionInfo;

	@XmlElement(name = "manifestTransformer")
	private ManifestTransformerConfig manifestTransformerConfig;

	@XmlElement(name = "instrumentation")
	private InstrumentationRootConfiguration instrumentationRootConfiguration;

	@XmlElement(name = "agentBuild")
	private AgentBuildConfiguration agentBuildConfiguration;

	public ConnectionInfo getConnectionInfo() {
		return connectionInfo;
	}

	public ManifestTransformerConfig getManifestTransformer() {
		return manifestTransformerConfig;
	}

	public InstrumentationRootConfiguration getInstrumentationRootConfiguration() {
		return instrumentationRootConfiguration;
	}

	public AgentBuildConfiguration getAgentBuildConfiguration() {
		return agentBuildConfiguration;
	}

}
