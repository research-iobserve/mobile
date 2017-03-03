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
public abstract class AbstractAndroidModule {

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
	 * Sets the {@link AndroidDataCollector}}.
	 * 
	 * @param androidDataCollector
	 *            the data collector to set
	 */
	public void setAndroidDataCollector(final AndroidDataCollector androidDataCollector) {
		this.androidDataCollector = androidDataCollector;
	}

	/**
	 * Sets the {@link CallbackManager}.
	 * 
	 * @param callbackManager
	 *            the callback manager to set
	 */
	public void setCallbackManager(final CallbackManager callbackManager) {
		this.callbackManager = callbackManager;
	}

	/**
	 * Pushes data to the {@link CallbackManager}.
	 * 
	 * @param data
	 *            data which should be passed to the callback manager
	 */
	protected void pushData(final MobileDefaultData data) {
		callbackManager.pushData(data);
	}
}
