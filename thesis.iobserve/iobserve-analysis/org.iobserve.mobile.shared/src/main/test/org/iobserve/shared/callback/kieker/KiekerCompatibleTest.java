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

import org.iobserve.common.mobile.record.MobileNetworkEventRecord;
import org.iobserve.common.mobile.record.MobileNetworkRequestEventRecord;
import org.iobserve.shared.callback.data.NetRequestResponse;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
		// network event
		final NetInfoResponse resp = new NetInfoResponse("a", true, false, "b", "c", "d", "e");
		final IMonitoringRecord record = resp.generateRecord();

		assertTrue(record != null);
		assertTrue(record instanceof MobileNetworkEventRecord);

		final MobileNetworkEventRecord rec = (MobileNetworkEventRecord) record;
		assertEquals(rec.getWSSID(), resp.getSsid());
		assertEquals(rec.isMobile(), resp.isMobile());
		assertEquals(rec.isWifi(), resp.isWifi());
		assertEquals(rec.getDeviceId(), resp.getDeviceId());
		assertEquals(rec.getMobileCarrierName(), resp.getCarrierName());
		assertEquals(rec.getProtocol(), resp.getProtocolName());
		assertEquals(rec.getWBSSID(), resp.getBssid());

		// network request
		final NetRequestResponse resp2 = new NetRequestResponse();
		resp2.setDeviceId("a");
		resp2.setDuration(5L);
		resp2.setMethod("b");
		resp2.setResponseCode(200);
		resp2.setUrl("test");

		final IMonitoringRecord record2 = resp2.generateRecord();

		assertTrue(record2 != null);
		assertTrue(record2 instanceof MobileNetworkRequestEventRecord);

		final MobileNetworkRequestEventRecord rec2 = (MobileNetworkRequestEventRecord) record2;
		assertEquals(rec2.getDeviceId(), resp2.getDeviceId());
		assertEquals(rec2.getDuration(), resp2.getDuration());
		assertEquals(rec2.getMethod(), resp2.getMethod());
		assertEquals(rec2.getResponseCode(), resp2.getResponseCode());
		assertEquals(rec2.getUrl(), resp2.getUrl());
	}

}
