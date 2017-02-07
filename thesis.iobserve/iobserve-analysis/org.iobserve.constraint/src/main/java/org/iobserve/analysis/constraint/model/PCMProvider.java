package org.iobserve.analysis.constraint.model;

import java.util.HashMap;
import java.util.Map;

public class PCMProvider {

	private Map<String, ProvideExpression> provideMapping;

	public PCMProvider() {
		this.provideMapping = new HashMap<>();
	}

	/**
	 * @return the provideMapping
	 */
	public Map<String, ProvideExpression> getProvideMapping() {
		return provideMapping;
	}

	/**
	 * @param provideMapping
	 *            the provideMapping to set
	 */
	public void setProvideMapping(Map<String, ProvideExpression> provideMapping) {
		this.provideMapping = provideMapping;
	}

}
