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
package org.iobserve.shared.callback.data;

import kieker.common.record.IMonitoringRecord;

import org.iobserve.common.mobile.record.MobileNetworkRequestEventRecord;
import org.iobserve.shared.callback.kieker.IKiekerCompatible;

/**
 * Monitoring record class for network requests.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class NetRequestResponse extends MobileDefaultData implements IKiekerCompatible {

	/**
	 * Id of the device.
	 */
	private String deviceId;
	/**
	 * URL which has been accessed.
	 */
	private String url;
	/**
	 * Method which was used.
	 */
	private String method;
	/**
	 * Duration of the request.
	 */
	private long duration;
	/**
	 * Response code of the request.
	 */
	private int responseCode;

	/**
	 * Creates an empty net request response with default values.
	 */
	public NetRequestResponse() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMonitoringRecord generateRecord() {
		return new MobileNetworkRequestEventRecord(deviceId, url, method, responseCode, duration);
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(final String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(final String url) {
		this.url = url;
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            the method to set
	 */
	public void setMethod(final String method) {
		this.method = method;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(final long duration) {
		this.duration = duration;
	}

	/**
	 * @return the responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode
	 *            the responseCode to set
	 */
	public void setResponseCode(final int responseCode) {
		this.responseCode = responseCode;
	}
}
