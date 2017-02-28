package org.iobserve.shared.callback.data;

import org.iobserve.common.mobile.record.MobileNetworkRequestEventRecord;
import org.iobserve.shared.callback.kieker.IKiekerCompatible;

import kieker.common.record.IMonitoringRecord;

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
	public void setDeviceId(String deviceId) {
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
	public void setUrl(String url) {
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
	public void setMethod(String method) {
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
	public void setDuration(long duration) {
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
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
}
