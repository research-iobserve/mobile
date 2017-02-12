package rocks.inspectit.android.callback.data;

public class SessionCloseRequest extends MobileDefaultData {
	private String appName;
	private String deviceId;

	public SessionCloseRequest(String app, String device) {
		this.appName = app;
		this.deviceId = device;
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
	public void setAppName(String appName) {
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
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
