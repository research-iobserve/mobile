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
package org.iobserve.mobile.agent;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.iobserve.mobile.agent.callback.CallbackManager;
import org.iobserve.mobile.agent.core.AndroidDataCollector;
import org.iobserve.mobile.agent.module.NetworkModule;
import org.iobserve.mobile.agent.sensor.TraceSensor;
import org.iobserve.mobile.agent.util.DependencyManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import android.app.Activity;

/**
 * Testing class for the agent functionality.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AgentTest {

	@Mock
	private Activity mockedActivity;

	@Mock
	private HttpURLConnection urlConnection;

	@Mock
	private CallbackManager callbackManager;

	@Mock
	private AndroidDataCollector dataCollector;

	private boolean networkTestBool;
	private boolean sensorTestBool;

	/**
	 * Creates a new test instance.
	 */
	public AgentTest() {
	}

	@Test
	public void emptyTest() {
	}

	/**
	 * Test for the network monitoring module.
	 * 
	 * @throws MalformedURLException
	 *             only happens if the google url is invalid and this is
	 *             unlikely
	 */
	public void testNetworkModule() throws MalformedURLException {
		final NetworkModule module = new NetworkModule(0);
		module.initModule(mockedActivity);

		networkTestBool = false;

		// WHEN STATEMENTS
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				AgentTest.this.networkTestBool = true;
				return null;
			}
		}).when(callbackManager).pushData(any());
		when(urlConnection.getURL()).thenReturn(new URL("http://www.google.de"));

		module.setCallbackManager(callbackManager);
		module.setAndroidDataCollector(dataCollector);

		module.openConnection(urlConnection);
		assertFalse(networkTestBool);
		try {
			module.getResponseCode(urlConnection);
		} catch (IOException e) {
			fail("Exception thrown.");
		}

		module.collectData();
		assertTrue(networkTestBool);
	}

	/**
	 * Tests the sensor logic.
	 */
	public void testSensor() {
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				AgentTest.this.sensorTestBool = true;
				return null;
			}
		}).when(callbackManager).pushKiekerData(any());
		DependencyManager.setCallbackManager(callbackManager);

		final TraceSensor ts = new TraceSensor();

		ts.beforeBody(0);

		ts.firstAfterBody(0);
		ts.secondAfterBody(0);

		assertTrue(this.sensorTestBool);
	}

}
