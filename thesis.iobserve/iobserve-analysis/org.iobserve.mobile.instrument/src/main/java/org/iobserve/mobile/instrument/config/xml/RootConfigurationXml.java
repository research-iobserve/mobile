/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.mobile.instrument.config.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * XML root mapping class which holds the whole XML configuration.
 * 
 * @author Robert Heinrich
 * @author David Monschein
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
	 * Configuration for packages where traces should get collected.
	 */
	@XmlElement(name = "traceCollection")
	private TraceCollectionConfiguration traceCollectionList;

	/**
	 * Creates a new instance.
	 */
	public RootConfigurationXml() {
	}

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

	/**
	 * @return the traceCollectionList
	 */
	public TraceCollectionConfiguration getTraceCollectionList() {
		return traceCollectionList;
	}

	/**
	 * @param traceCollectionList
	 *            the traceCollectionList to set
	 */
	public void setTraceCollectionList(final TraceCollectionConfiguration traceCollectionList) {
		this.traceCollectionList = traceCollectionList;
	}

}
