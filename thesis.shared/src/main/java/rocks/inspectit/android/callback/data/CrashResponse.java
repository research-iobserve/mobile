package rocks.inspectit.android.callback.data;

/**
 * Class for holding information about a crash which occurred on a mobile
 * device.
 * 
 * @author David Monschein
 */
public class CrashResponse extends MobileDefaultData {

	/**
	 * Name of the exception.
	 */
	private String exceptionName;

	/**
	 * Message of the exception.
	 */
	private String exceptionMessage;

	/**
	 * Timestamp when the exception occurred.
	 */
	private long exceptionTimestamp;

	/**
	 * Creates a new instance containing the current time as timestamp.
	 */
	public CrashResponse() {
		this.exceptionTimestamp = System.currentTimeMillis();
	}

	/**
	 * @return the exceptionName
	 */
	public String getExceptionName() {
		return exceptionName;
	}

	/**
	 * @param exceptionName
	 *            the exceptionName to set
	 */
	public void setExceptionName(final String exceptionName) {
		this.exceptionName = exceptionName;
	}

	/**
	 * @return the exceptionMessage
	 */
	public String getExceptionMessage() {
		return exceptionMessage;
	}

	/**
	 * @param exceptionMessage
	 *            the exceptionMessage to set
	 */
	public void setExceptionMessage(final String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	/**
	 * @return the exceptionTimestamp
	 */
	public long getExceptionTimestamp() {
		return exceptionTimestamp;
	}

	/**
	 * @param exceptionTimestamp
	 *            the exceptionTimestamp to set
	 */
	public void setExceptionTimestamp(final long exceptionTimestamp) {
		this.exceptionTimestamp = exceptionTimestamp;
	}
}
