package org.iobserve.mobile.agent.sensor;

/**
 * Interface for the sensor concept of the agent.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public interface ISensor {
	/**
	 * Is executed before the original method gets executed.
	 */
	void beforeBody();

	/**
	 * Gets executed if the real method throws an exception.
	 * 
	 * @param clazz
	 *            the class of the exception
	 */
	void exceptionThrown(String clazz);

	/**
	 * Gets executed after the original method was executed.
	 */
	void firstAfterBody();

	/**
	 * Gets executed after the original method and the
	 * {@link ISensor#firstAfterBody()} were executed.
	 */
	void secondAfterBody();

	/**
	 * Sets the name of the class which owns the method.
	 * 
	 * @param owner
	 *            name of the class which owns the method
	 */
	void setOwner(String owner);

	/**
	 * Sets the signature of the method for which this sensor is responsible
	 * 
	 * @param methodSignature
	 *            the signature of the method
	 */
	void setSignature(String methodSignature);
}
