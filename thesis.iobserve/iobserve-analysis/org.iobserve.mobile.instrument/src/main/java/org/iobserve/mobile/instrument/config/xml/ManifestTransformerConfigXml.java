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
	 * Creates a new instance.
	 */
	public ManifestTransformerConfigXml() {
	}

	/**
	 * Gets the list of permissions which are needed.
	 * 
	 * @return list of permissions which are needed
	 */
	public List<String> getPermissions() {
		return permissions;
	}

}
