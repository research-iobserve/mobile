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
package org.iobserve.mobile.agent.callback.strategies;

import org.iobserve.mobile.agent.callback.CallbackTask;
import org.iobserve.mobile.agent.core.ExternalConfiguration;
import org.iobserve.shared.callback.data.MobileCallbackData;
import org.iobserve.shared.callback.data.MobileDefaultData;

import rocks.fasterxml.jackson.core.JsonProcessingException;
import rocks.fasterxml.jackson.databind.ObjectMapper;

/**
 * Abstract class which represents a strategy for handling the data which is
 * sent back to the server.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public abstract class AbstractCallbackStrategy {
	/**
	 * JSON Object mapper for serializing and deserializing JSON strings.
	 */
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * {@link MobileCallbackData} which holds the data which should be sent to
	 * the server.
	 */
	protected MobileCallbackData data;

	/**
	 * Constructs a new callback strategy and creates an empty
	 * {@link MobileCallbackData} object for holding data.
	 */
	public AbstractCallbackStrategy() {
		this.data = new MobileCallbackData();
	}

	/**
	 * Adds data to the data holder.
	 * 
	 * @param dat
	 *            data which should be added
	 */
	public abstract void addData(MobileDefaultData dat);

	/**
	 * Stops the callback strategy.
	 */
	public abstract void stop();

	/**
	 * Immediately sends a data object to the server with not respecting the
	 * strategy.
	 * 
	 * @param dat
	 *            data which should be sent immediately
	 * @param izHello
	 *            whether it is a session creation message or not
	 */
	public void sendImmediately(final MobileCallbackData dat, final boolean izHello) {
		this.sendBeacon(dat, izHello);
		data.clear();
	}

	/**
	 * Flushes all records gathered at the moment to the output REST interface.
	 */
	public void flush() {
		// send data currently in the buffer
		this.sendBeacon();
	}

	/**
	 * Sets the session id.
	 * 
	 * @param id
	 *            session id
	 */
	public void setSessId(final String id) {
		this.data.setSessionId(id);
	}

	/**
	 * Sends the data to the server.
	 */
	public synchronized void sendBeacon() {
		this.sendBeacon(this.data, false);
		data.clear();
	}

	/**
	 * Sends a specified beacon to the server.
	 * 
	 * @param dat
	 *            beacon
	 * @param helloReq
	 *            whether it is a session creation message or not
	 */
	private void sendBeacon(final MobileCallbackData dat, final boolean helloReq) {
		if (dat != null && dat.getChildData().size() > 0) {
			final String callbackUrl;
			if (helloReq) {
				callbackUrl = ExternalConfiguration.getHelloUrl();
			} else {
				callbackUrl = ExternalConfiguration.getBeaconUrl();
			}

			try {
				new CallbackTask(callbackUrl).execute(mapper.writeValueAsString(dat));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}

}
