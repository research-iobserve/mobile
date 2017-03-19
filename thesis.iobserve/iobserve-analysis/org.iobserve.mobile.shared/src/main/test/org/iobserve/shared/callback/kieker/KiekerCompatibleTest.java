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
package org.iobserve.shared.callback.kieker;

import kieker.common.record.IMonitoringRecord;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Test for the functionality of the conversion to the Kieker format.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class KiekerCompatibleTest {

	/**
	 * Creates a new test instance.
	 */
	public KiekerCompatibleTest() {
	}

	/**
	 * Setup method.
	 * 
	 * @throws Exception
	 *             if setup fails
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Test for the conversion functionality.
	 */
	@Test
	public void netTest() {
		final NetInfoResponse resp = new NetInfoResponse("a", true, false, "b", "c", "d", "e");
		final IMonitoringRecord record = resp.generateRecord();

		assertTrue(record != null);
	}

}
