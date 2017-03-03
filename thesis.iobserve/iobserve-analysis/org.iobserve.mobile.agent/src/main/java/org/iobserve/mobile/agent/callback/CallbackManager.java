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
package org.iobserve.mobile.agent.callback;

import java.util.ArrayList;
import java.util.List;

import kieker.common.record.IMonitoringRecord;

import org.iobserve.mobile.agent.callback.strategies.AbstractCallbackStrategy;
import org.iobserve.mobile.agent.core.ExternalConfiguration;
import org.iobserve.mobile.agent.util.DependencyManager;
import org.iobserve.shared.callback.data.HelloRequest;
import org.iobserve.shared.callback.data.MobileCallbackData;
import org.iobserve.shared.callback.data.MobileDefaultData;
import org.iobserve.shared.callback.kieker.PlainKieker;

import android.util.Log;

/**
 * Component which handles the connection to the server which persists the
 * monitoring data.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class CallbackManager {
	/**
	 * Consistent log tag for the agent.
	 */
	private static final String LOG_TAG = ExternalConfiguration.getLogTag();

	/**
	 * The callback strategy which is used to send data.
	 */
	private AbstractCallbackStrategy strategy = DependencyManager.getCallbackStrategy();

	/**
	 * Whether there is an existing session.
	 */
	private boolean sessActive;

	/**
	 * Contains all data which isn't sent already because there is no
	 * connection.
	 */
	private List<MobileDefaultData> sessQueue;

	/**
	 * Creates a new callback manager.
	 */
	public CallbackManager() {
		this.sessActive = false;
		this.sessQueue = new ArrayList<MobileDefaultData>();
	}

	/**
	 * Pass data to the callback manager and the manager manages the
	 * transmission to the server.
	 * 
	 * @param data
	 *            data which should be transferred to the server
	 */
	public void pushData(final MobileDefaultData data) {
		if (!sessActive) {
			this.sessQueue.add(data);
		} else {
			this.strategy.addData(data);
		}
	}

	/**
	 * Pushes a Kieker record which is sent to the server.
	 * 
	 * @param record
	 *            the Kieker record which should be transferred to the server
	 */
	public void pushKiekerData(final IMonitoringRecord record) {
		this.pushData(new PlainKieker(record));
	}

	/**
	 * Pushes a hello message for session creation.
	 * 
	 * @param response
	 *            hello request which should be sent to the servers
	 */
	public void pushHelloMessage(final HelloRequest response) {
		final MobileCallbackData data = new MobileCallbackData();
		data.setSessionId(null);

		final List<MobileDefaultData> childs = new ArrayList<MobileDefaultData>();
		childs.add(response);

		data.setChildData(childs);

		// directly send it
		strategy.sendImmediately(data, true);
	}

	/**
	 * Shuts down the callback manager.
	 */
	public void shutdown() {
		strategy.flush();
		strategy.stop();
	}

	/**
	 * Flushes all entries in the session queue to the callback strategy.
	 */
	private void swapQueue() {
		for (MobileDefaultData data : sessQueue) {
			strategy.addData(data);
		}
		sessQueue.clear();
	}

	/**
	 * Applies a given session id which is used to communicate with the server.
	 * 
	 * @param id
	 *            session id
	 */
	public void applySessionId(final String id) {
		if (!sessActive) {
			Log.i(LOG_TAG, "Created session with id '" + id + "' for communicating with the CMR.");

			strategy.setSessId(id);
			sessActive = true;

			swapQueue();
		}
	}
}
