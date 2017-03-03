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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * XML Mapping class for the main instrumentation configuration.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class InstrumentationRootConfigurationXml {

	/**
	 * List of instrumentation point configurations.
	 */
	@XmlElementWrapper(name = "mapping")
	@XmlElement(name = "point")
	private List<InstrumentationPointConfigurationXml> instrPointConfigs;

	/**
	 * Creates a new instance.
	 */
	public InstrumentationRootConfigurationXml() {
	}

	/**
	 * Gets the list of instrumentation point configurations.
	 * 
	 * @return the list of instrumentation point configurations
	 */
	public List<InstrumentationPointConfigurationXml> getInstrPointConfigs() {
		return instrPointConfigs;
	}

}
