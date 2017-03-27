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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * XML Mapping class for the instrumentation point configuration.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class InstrumentationPointConfigurationXml {

	/**
	 * Type of the instrumentation point.
	 */
	@XmlAttribute(name = "type")
	private String type;

	/**
	 * Value of the instrumentation point.
	 */
	@XmlValue
	private String value;

	/**
	 * Creates a new instance.
	 */
	public InstrumentationPointConfigurationXml() {
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

}
