package org.iobserve.analysis.constraint.model;

import java.util.List;

import org.iobserve.analysis.constraint.model.types.AbstractTargetConstraint;

public class PCMConstraint {

	private String name;
	private ProvideExpression target;
	private List<AbstractTargetConstraint> concreteConstraints;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the target
	 */
	public ProvideExpression getTarget() {
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(ProvideExpression target) {
		this.target = target;
	}

	/**
	 * @return the concreteConstraints
	 */
	public List<AbstractTargetConstraint> getConcreteConstraints() {
		return concreteConstraints;
	}

	/**
	 * @param concreteConstraints the concreteConstraints to set
	 */
	public void setConcreteConstraints(List<AbstractTargetConstraint> concreteConstraints) {
		this.concreteConstraints = concreteConstraints;
	}

}
