package org.iobserve.shared.callback.data;

import rocks.inspectit.android.callback.data.MobileDefaultData;

/**
 * Class holding information about a device which wants to connect to the
 * server.
 * 
 * @author David Monschein
 */
public class HelloRequest extends MobileDefaultData {

	/**
	 * The name of the mobile application.
	 */
	private String appName;

	/**
	 * The device id.
	 */
	private String deviceId;

	/**
	 * Creates a new instance.
	 */
	public HelloRequest() {
	}

	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * @param appName
	 *            the appName to set
	 */
	public void setAppName(final String appName) {
		this.appName = appName;
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
}
