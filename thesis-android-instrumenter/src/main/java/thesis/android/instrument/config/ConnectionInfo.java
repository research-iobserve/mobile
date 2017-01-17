package thesis.android.instrument.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ConnectionInfo {

	private String helloUrl;
	private String beaconUrl;

	public String getHelloUrl() {
		return helloUrl;
	}

	public void setHelloUrl(String helloUrl) {
		this.helloUrl = helloUrl;
	}

	public String getBeaconUrl() {
		return beaconUrl;
	}

	public void setBeaconUrl(String beaconUrl) {
		this.beaconUrl = beaconUrl;
	}

}
