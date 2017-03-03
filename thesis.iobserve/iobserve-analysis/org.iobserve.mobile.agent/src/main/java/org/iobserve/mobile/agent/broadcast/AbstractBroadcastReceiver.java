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
package org.iobserve.mobile.agent.broadcast;

import org.iobserve.mobile.agent.callback.CallbackManager;
import org.iobserve.mobile.agent.core.AndroidDataCollector;
import org.iobserve.shared.callback.data.MobileDefaultData;

import android.content.BroadcastReceiver;

/**
 * Abstract class for setting up a broadcast receiver for Android.
 * 
 * @author David Monschein
 *
 */
public abstract class AbstractBroadcastReceiver extends BroadcastReceiver {
	/**
	 * The data collector.
	 */
	protected AndroidDataCollector androidDataCollector;

	/**
	 * The callback manager.
	 */
	private CallbackManager callbackManager;

	/**
	 * Defines for which actions the receiver should be broadcasted.
	 * 
	 * @return action types which should be reported
	 */
	public abstract String[] getFilterActions();

	/**
	 * Sets the {@link AndroidDataCollector}.
	 * 
	 * @param androidDataCollector
	 *            data collector instance
	 */
	public void setAndroidDataCollector(final AndroidDataCollector androidDataCollector) {
		this.androidDataCollector = androidDataCollector;
	}

	/**
	 * Sets the {@link CallbackManager}}.
	 * 
	 * @param callbackManager
	 *            callbck manager instance
	 */
	public void setCallbackManager(final CallbackManager callbackManager) {
		this.callbackManager = callbackManager;
	}

	/**
	 * Pushes data to the callback manager which is responsible for further
	 * tasks.
	 * 
	 * @param data
	 *            the data which should be passed to the callback manager.
	 */
	protected void pushData(final MobileDefaultData data) {
		callbackManager.pushData(data);
	}
}
