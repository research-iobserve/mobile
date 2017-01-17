package thesis.android.instrument.config;

import javax.xml.bind.annotation.XmlElement;

public class SensorConfig {

	@XmlElement(name = "parent")
	private String parent;

	@XmlElement(name = "method")
	private String method;

	@XmlElement(name = "signature")
	private String signature;

	@XmlElement(name = "handler")
	private String handler;

	public String getParent() {
		return parent;
	}

	public String getMethod() {
		return method;
	}

	public String getSignature() {
		return signature;
	}

	public String getHandler() {
		return handler;
	}

}
