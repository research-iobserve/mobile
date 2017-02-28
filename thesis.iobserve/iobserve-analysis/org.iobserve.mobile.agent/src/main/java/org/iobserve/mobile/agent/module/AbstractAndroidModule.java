package org.iobserve.mobile.agent.module;

import org.iobserve.mobile.agent.callback.CallbackManager;
import org.iobserve.mobile.agent.core.AndroidDataCollector;
import org.iobserve.shared.callback.data.MobileDefaultData;

import android.content.Context;

/**
 * Abstract Android module which has an init and an exit point.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
abstract public class AbstractAndroidModule {

	/**
	 * Link to the {@link AndroidDataCollector} component.
	 */
	protected AndroidDataCollector androidDataCollector;

	/**
	 * Link to the {@link CallbackManager} component.
	 */
	private CallbackManager callbackManager;

	/**
	 * Initializes the module with a given context.
	 * 
	 * @param ctx
	 *            context of the application
	 */
	public abstract void initModule(Context ctx);

	/**
	 * Shuts the module down.
	 */
	public abstract void shutdownModule();

	/**
	 * Sets the {@link AndroidDataCollector}}
	 * 
	 * @param androidDataCollector
	 *            the data collector to set
	 */
	public void setAndroidDataCollector(AndroidDataCollector androidDataCollector) {
		this.androidDataCollector = androidDataCollector;
	}

	/**
	 * Sets the {@link CallbackManager}
	 * 
	 * @param callbackManager
	 *            the callback manager to set
	 */
	public void setCallbackManager(CallbackManager callbackManager) {
		this.callbackManager = callbackManager;
	}

	/**
	 * Pushes data to the {@link CallbackManager}
	 * 
	 * @param data
	 *            data which should be passed to the callback manager
	 */
	protected void pushData(MobileDefaultData data) {
		callbackManager.pushData(data);
	}
}
