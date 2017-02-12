package rocks.inspectit.android.callback;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import kieker.common.record.IMonitoringRecord;
import rocks.inspectit.android.ExternalConfiguration;
import rocks.inspectit.android.callback.data.HelloRequest;
import rocks.inspectit.android.callback.data.MobileCallbackData;
import rocks.inspectit.android.callback.data.MobileDefaultData;
import rocks.inspectit.android.callback.data.SessionCloseRequest;
import rocks.inspectit.android.callback.kieker.PlainKieker;
import rocks.inspectit.android.callback.strategies.AbstractCallbackStrategy;
import rocks.inspectit.android.util.DependencyManager;

/**
 * Component which handles the connection to the server which persists the
 * monitoring data.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class CallbackManager {
	/**
	 * Consistent log tag for the agent.
	 */
	private static final String LOG_TAG = ExternalConfiguration.getLogTag();

	/**
	 * The callback strategy which is used to send data.
	 */
	private AbstractCallbackStrategy strategy = DependencyManager.getCallbackStrategy();

	/**
	 * Whether there is an existing session.
	 */
	private boolean sessActive;

	/**
	 * Contains all data which isn't sent already because there is no connection
	 */
	private List<MobileDefaultData> sessQueue;

	/**
	 * Creates a new callback manager.
	 */
	public CallbackManager() {
		this.sessActive = false;
		this.sessQueue = new ArrayList<MobileDefaultData>();
	}

	/**
	 * Pass data to the callback manager and the manager manages the
	 * transmission to the server.
	 * 
	 * @param data
	 *            data which should be transferred to the server
	 */
	public void pushData(MobileDefaultData data) {
		if (!sessActive) {
			this.sessQueue.add(data);
		} else {
			this.strategy.addData(data);
		}
	}

	/**
	 * Pushes a Kieker record which is sent to the server.
	 * 
	 * @param record
	 *            the Kieker record which should be transferred to the server
	 */
	public void pushKiekerData(IMonitoringRecord record) {
		this.pushData(new PlainKieker(record));
	}

	/**
	 * Pushes a hello message for session creation.
	 * 
	 * @param response
	 *            hello request which should be sent to the servers
	 */
	public void pushHelloMessage(HelloRequest response) {
		MobileCallbackData data = new MobileCallbackData();
		data.setSessionId(null);

		List<MobileDefaultData> childs = new ArrayList<MobileDefaultData>();
		childs.add(response);

		data.setChildData(childs);

		// directly send it
		strategy.sendImmediately(data, true);
	}

	public void sendBye(SessionCloseRequest request) {
		// undeployment
		MobileCallbackData data = new MobileCallbackData();

		List<MobileDefaultData> childs = new ArrayList<MobileDefaultData>();
		childs.add(request);

		data.setChildData(childs);

		// directly send it and trigger the flushing of the buffer
		strategy.sendByeMessage(data);
	}

	/**
	 * Shuts down the callback manager.
	 */
	public void shutdown() {
		strategy.stop();
	}

	/**
	 * Flushes all entries in the session queue to the callback strategy.
	 */
	private void swapQueue() {
		for (MobileDefaultData data : sessQueue) {
			strategy.addData(data);
		}
		sessQueue.clear();
	}

	/**
	 * Applies a given session id which is used to communicate with the server.
	 * 
	 * @param id
	 *            session id
	 */
	public void applySessionId(String id) {
		if (!sessActive) {
			Log.i(LOG_TAG, "Created session with id '" + id + "' for communicating with the CMR.");

			strategy.setSessId(id);
			sessActive = true;

			swapQueue();
		}
	}
}
