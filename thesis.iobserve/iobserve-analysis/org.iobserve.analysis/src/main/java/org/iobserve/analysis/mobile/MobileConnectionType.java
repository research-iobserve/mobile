/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.mobile;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for the different types of network connection.
 * 
 * @author David Monschein
 */
public enum MobileConnectionType {
	WLAN("wifi"), MOBILE("mobile");

	/**
	 * Mapping between the string representation of a connection type and the
	 * enum value.
	 */
	private static final Map<String, MobileConnectionType> MAPPING;

	/**
	 * The belonging name of an connection type.
	 */
	private String name;

	/**
	 * Initializes the mapping.
	 */
	static {
		MAPPING = new HashMap<>();
		for (MobileConnectionType instance : MobileConnectionType.values()) {
			MAPPING.put(instance.getStringExpression(), instance);
		}
	}

	/**
	 * Creates a new connection type with a given string representation.
	 * 
	 * @param type
	 *            string representation of the connection type
	 */
	private MobileConnectionType(final String type) {
		this.name = type;
	}

	/**
	 * Gets the string representation.
	 * 
	 * @return string representation
	 */
	public String getStringExpression() {
		return name;
	}

	/**
	 * Gets the enum value from a given string representation.
	 * 
	 * @param val
	 *            the string representation
	 * @return the belonging enum value and null if there is none
	 */
	public MobileConnectionType get(final String val) {
		return MAPPING.get(val);
	}
}
