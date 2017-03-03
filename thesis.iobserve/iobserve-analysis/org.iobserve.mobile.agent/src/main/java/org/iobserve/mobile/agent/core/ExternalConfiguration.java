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
package org.iobserve.mobile.agent.core;

/**
 * The data in this class gets set external by code inserted into the original
 * application.
 */
public final class ExternalConfiguration {

	/**
	 * Log tag for consistent log outputs of the agent.
	 */
	private static final String LOG_TAG = "Android Agent";

	/**
	 * URL for sending monitoring beacons.
	 */
	private static String beaconUrl;
	/**
	 * URL for sending initial hello request to create a session.
	 */
	private static String helloUrl;

	/**
	 * New instance creation not allowed.
	 */
	private ExternalConfiguration() {
	}

	/**
	 * @return the beaconUrl
	 */
	public static String getBeaconUrl() {
		return beaconUrl;
	}

	/**
	 * @param beaconUrl
	 *            the beaconUrl to set
	 */
	public static void setBeaconUrl(final String beaconUrl) {
		ExternalConfiguration.beaconUrl = beaconUrl;
	}

	/**
	 * @return the helloUrl
	 */
	public static String getHelloUrl() {
		return helloUrl;
	}

	/**
	 * @param helloUrl
	 *            the helloUrl to set
	 */
	public static void setHelloUrl(final String helloUrl) {
		ExternalConfiguration.helloUrl = helloUrl;
	}

	/**
	 * @return the logTag
	 */
	public static String getLogTag() {
		return LOG_TAG;
	}
}
