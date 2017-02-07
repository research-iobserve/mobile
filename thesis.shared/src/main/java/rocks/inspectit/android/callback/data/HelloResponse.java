package rocks.inspectit.android.callback.data;

/**
 * Class representing an initial request response from the server for
 * establishing a session.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class HelloResponse {
	/**
	 * The session id.
	 */
	private String sessionId;

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}
