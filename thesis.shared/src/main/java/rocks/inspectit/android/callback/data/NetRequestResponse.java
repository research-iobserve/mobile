package rocks.inspectit.android.callback.data;

import kieker.common.record.IMonitoringRecord;
import rocks.inspectit.android.callback.kieker.IKiekerCompatible;
import rocks.inspectit.android.callback.kieker.MobileNetworkRequestEventRecord;

/**
 * Created by DMO on 10.12.2016.
 */

public class NetRequestResponse extends MobileDefaultData implements IKiekerCompatible {
	
	private String deviceId;
    private String url;
    private String method;
    private long duration;
    private int responseCode;

    public NetRequestResponse() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    
    public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

    @Override
    public IMonitoringRecord generateRecord() {
        return new MobileNetworkRequestEventRecord(deviceId, url, method, responseCode, duration);
    }
}
