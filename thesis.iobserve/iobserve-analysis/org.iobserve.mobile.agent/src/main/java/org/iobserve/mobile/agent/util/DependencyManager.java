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
package org.iobserve.mobile.agent.util;

import org.iobserve.mobile.agent.callback.CallbackManager;
import org.iobserve.mobile.agent.callback.strategies.AbstractCallbackStrategy;
import org.iobserve.mobile.agent.core.AndroidDataCollector;

/**
 * Holds the main components of the agent.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public final class DependencyManager {
	/**
	 * Reference to the {@link CallbackManager}.
	 */
	private static CallbackManager callbackManager;

	/**
	 * Reference to the {@link AndroidDataCollector}.
	 */
	private static AndroidDataCollector androidDataCollector;

	/**
	 * Reference to the {@link AbstractCallbackStrategy}.
	 */
	private static AbstractCallbackStrategy callbackStrategy;

	/**
	 * Helper class where no instance creation is allowed.
	 */
	private DependencyManager() {
	}

	/**
	 * @return the callbackManager
	 */
	public static CallbackManager getCallbackManager() {
		return callbackManager;
	}

	/**
	 * @param callbackManager
	 *            the callbackManager to set
	 */
	public static void setCallbackManager(final CallbackManager callbackManager) {
		DependencyManager.callbackManager = callbackManager;
	}

	/**
	 * @return the androidDataCollector
	 */
	public static AndroidDataCollector getAndroidDataCollector() {
		return androidDataCollector;
	}

	/**
	 * @param androidDataCollector
	 *            the androidDataCollector to set
	 */
	public static void setAndroidDataCollector(final AndroidDataCollector androidDataCollector) {
		DependencyManager.androidDataCollector = androidDataCollector;
	}

	/**
	 * @return the callbackStrategy
	 */
	public static AbstractCallbackStrategy getCallbackStrategy() {
		return callbackStrategy;
	}

	/**
	 * @param callbackStrategy
	 *            the callbackStrategy to set
	 */
	public static void setCallbackStrategy(final AbstractCallbackStrategy callbackStrategy) {
		DependencyManager.callbackStrategy = callbackStrategy;
	}
}
