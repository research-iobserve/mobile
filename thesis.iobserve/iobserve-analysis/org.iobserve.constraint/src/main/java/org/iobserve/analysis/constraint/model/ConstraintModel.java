package org.iobserve.analysis.constraint.model;

import java.util.ArrayList;
import java.util.List;

public class ConstraintModel {
	private PCMProvider pcmProvider;
	private List<PCMConstraint> constraints;

	public ConstraintModel() {
		this(null, new ArrayList<PCMConstraint>());
	}

	public ConstraintModel(PCMProvider provider, List<PCMConstraint> constraints) {
		this.pcmProvider = provider;
		this.constraints = constraints;
	}

	/**
	 * @return the pcmProvider
	 */
	public PCMProvider getPcmProvider() {
		return pcmProvider;
	}

	/**
	 * @param pcmProvider
	 *            the pcmProvider to set
	 */
	public void setPcmProvider(PCMProvider pcmProvider) {
		this.pcmProvider = pcmProvider;
	}

	/**
	 * @return the constraints
	 */
	public List<PCMConstraint> getConstraints() {
		return constraints;
	}

	/**
	 * @param constraints
	 *            the constraints to set
	 */
	public void setConstraints(List<PCMConstraint> constraints) {
		this.constraints = constraints;
	}

}
