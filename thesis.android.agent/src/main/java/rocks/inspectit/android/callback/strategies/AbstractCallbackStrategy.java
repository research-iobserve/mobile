package rocks.inspectit.android.callback.strategies;

import rocks.fasterxml.jackson.core.JsonProcessingException;
import rocks.fasterxml.jackson.databind.ObjectMapper;
import rocks.inspectit.android.ExternalConfiguration;
import rocks.inspectit.android.callback.CallbackTask;
import rocks.inspectit.android.callback.data.MobileCallbackData;
import rocks.inspectit.android.callback.data.MobileDefaultData;

/**
 * Abstract class which represents a strategy for handling the data which is
 * sent back to the server.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public abstract class AbstractCallbackStrategy {
	/**
	 * JSON Object mapper for serializing and deserializing JSON strings.
	 */
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * {@link MobileCallbackData} which holds the data which should be sent to
	 * the server
	 */
	protected MobileCallbackData data;

	/**
	 * Constructs a new callback strategy and creates an empty
	 * {@link MobileCallbackData} object for holding data.
	 */
	public AbstractCallbackStrategy() {
		this.data = new MobileCallbackData();
	}

	/**
	 * Adds data to the data holder.
	 * 
	 * @param data
	 *            data which should be added
	 */
	public abstract void addData(MobileDefaultData data);

	/**
	 * Stops the callback strategy.
	 */
	public abstract void stop();

	/**
	 * Immediately sends a data object to the server with not respecting the
	 * strategy.
	 * 
	 * @param data
	 *            data which should be sent immediately
	 * @param isHello
	 *            whether it is a session creation message or not
	 */
	public void sendImmediately(MobileCallbackData data, boolean isHello) {
		this.sendBeacon(data, isHello);
		data.clear();
	}

	/**
	 * Sends a bye message and flushes all records gathered at the moment to the
	 * outstream.
	 * 
	 * @param data
	 *            the container which holds the goodbye message
	 */
	public void sendByeMessage(MobileCallbackData data) {
		// send data currently in the buffer
		this.sendBeacon();

		// send the goodbye
		data.setSessionId(data.getSessionId());
		this.sendBeacon(data, false);
	}

	/**
	 * Sets the session id.
	 * 
	 * @param id
	 *            session id
	 */
	public void setSessId(String id) {
		this.data.setSessionId(id);
	}

	/**
	 * Sends the data to the server.
	 */
	public synchronized void sendBeacon() {
		this.sendBeacon(this.data, false);
		data.clear();
	}

	/**
	 * Sends a specified beacon to the server.
	 * 
	 * @param data
	 *            beacon
	 * @param helloReq
	 *            whether it is a session creation message or not
	 */
	private void sendBeacon(MobileCallbackData data, boolean helloReq) {
		if (data != null && data.getChildData().size() > 0) {
			String callbackUrl;
			if (helloReq) {
				callbackUrl = ExternalConfiguration.getHelloUrl();
			} else {
				callbackUrl = ExternalConfiguration.getBeaconUrl();
			}

			try {
				new CallbackTask(callbackUrl).execute(mapper.writeValueAsString(data));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}

}
