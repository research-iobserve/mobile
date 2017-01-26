package rocks.inspectit.android.callback.data;

/**
 * Created by DMO on 22.11.2016.
 */

public class HelloRequest extends MobileDefaultData {

	private String appName;
	private String deviceId;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
