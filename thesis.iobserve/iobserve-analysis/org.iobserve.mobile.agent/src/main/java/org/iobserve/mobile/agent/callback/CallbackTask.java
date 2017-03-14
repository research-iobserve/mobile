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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.iobserve.mobile.agent.core.ExternalConfiguration;
import org.iobserve.mobile.agent.util.DependencyManager;
import org.iobserve.shared.callback.data.SessionCreationResponse;

import android.os.AsyncTask;
import android.util.Log;
import rocks.fasterxml.jackson.databind.ObjectMapper;

/**
 * Task which is responsible for sending data to the REST interface of the
 * server. This task runs asynchronously so it doesn't block the UI thread.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class CallbackTask extends AsyncTask<String, Void, String> {
	/**
	 * Consistent log tag which is used by the agent.
	 */
	private static final String LOG_TAG = ExternalConfiguration.getLogTag();

	/**
	 * JSON object mapper for serializing and de-serializing JSON strings.
	 */
	private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();

	/**
	 * The REST interface URL.
	 */
	private String callbackUrl;

	/**
	 * Reference to the {@link CallbackManager}.
	 */
	private CallbackManager callbackManager = DependencyManager.getCallbackManager();

	/**
	 * Creates a new task with a specified url.
	 * 
	 * @param url
	 *            REST interface URL
	 */
	public CallbackTask(final String url) {
		this.callbackUrl = url;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String doInBackground(final String... params) {
		if (params.length == 1) {
			return postRequest(callbackUrl, params[0]);
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onPostExecute(final String result) {
		if (result != null && !result.isEmpty()) {
			try {
				final SessionCreationResponse resp = OBJECTMAPPER.readValue(result, SessionCreationResponse.class);
				if (resp.getSessionId() != null && resp.getSessionId().length() > 0) {
					callbackManager.applySessionId(resp.getSessionId());
				}
			} catch (IOException e) {
				Log.e(LOG_TAG, "Couldn't read the response to the session request.");
				return;
			}
		}
	}

	/**
	 * Performs a post request to a given URL with given data
	 * 
	 * @param rawUrl
	 *            the URL
	 * @param data
	 *            the data
	 * @return the response from the server
	 */
	private String postRequest(final String rawUrl, final String data) {
		Log.i(LOG_TAG, "Sending back beacon to '" + rawUrl + "'.");
		try {
			final URL url = new URL(rawUrl);
			final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			final OutputStream os = conn.getOutputStream();
			os.write(data.getBytes());
			os.flush();

			final BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			String all = null;
			while ((output = br.readLine()) != null) {
				all = all == null ? output : all + "\n" + output;
			}
			conn.disconnect();

			return all;
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Malformed URL while sending the data to the CMR.");
			return null;
		} catch (IOException e) {
			Log.e(LOG_TAG, "Couldn't send the data back to the CMR because of an IOException.");
			return null;
		}
	}
}
