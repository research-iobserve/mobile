package org.iobserve.analysis.mobile;

public class MobileMobileConnectionInfo extends MobileConnectionInfo {

	private String protocol;
	private String provider;

	public MobileMobileConnectionInfo(String protocol, String provider) {
		super();
		this.protocol = protocol;
		this.provider = provider;
	}

	@Override
	public MobileConnectionType getCorrespondingType() {
		return MobileConnectionType.MOBILE;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

}
