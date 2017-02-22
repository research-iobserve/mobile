package org.iobserve.mobile.analysis;

public class ConstraintViolation {

	private String message;

	public ConstraintViolation(String m) {
		this.message = m;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
