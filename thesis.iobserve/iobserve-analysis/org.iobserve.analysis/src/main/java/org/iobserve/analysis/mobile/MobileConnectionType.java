package org.iobserve.analysis.mobile;

import java.util.HashMap;
import java.util.Map;

public enum MobileConnectionType {
	WLAN("wifi"), MOBILE("mobile");

	private String name;
	private static final Map<String, MobileConnectionType> MAPPING;

	static {
		MAPPING = new HashMap<>();
		for (MobileConnectionType instance : MobileConnectionType.values()) {
			MAPPING.put(instance.getStringExpression(), instance);
		}
	}

	private MobileConnectionType(String type) {
		this.name = type;
	}

	public String getStringExpression() {
		return name;
	}

	public MobileConnectionType get(String val) {
		return MAPPING.get(val);
	}
}
