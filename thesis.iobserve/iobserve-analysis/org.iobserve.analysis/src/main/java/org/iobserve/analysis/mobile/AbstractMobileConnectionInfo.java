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

/**
 * Abstract class for holding information about a network connection of a mobile
 * device.
 * 
 * @author David Monschein
 *
 */
public abstract class AbstractMobileConnectionInfo {
	/**
	 * Gets the type of the connection.
	 * 
	 * @return type of the connection.
	 */
	public abstract MobileConnectionType getCorrespondingType();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object b) {
		return super.equals(b);
	}
}
