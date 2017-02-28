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
public class DependencyManager {
	/**
	 * Reference to the {@link CallbackManager}
	 */
	private static CallbackManager callbackManager;

	/**
	 * Reference to the {@link AndroidDataCollector}
	 */
	private static AndroidDataCollector androidDataCollector;

	/**
	 * Reference to the {@link AbstractCallbackStrategy}
	 */
	private static AbstractCallbackStrategy callbackStrategy;

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
	public static void setCallbackManager(CallbackManager callbackManager) {
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
	public static void setAndroidDataCollector(AndroidDataCollector androidDataCollector) {
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
	public static void setCallbackStrategy(AbstractCallbackStrategy callbackStrategy) {
		DependencyManager.callbackStrategy = callbackStrategy;
	}
}
