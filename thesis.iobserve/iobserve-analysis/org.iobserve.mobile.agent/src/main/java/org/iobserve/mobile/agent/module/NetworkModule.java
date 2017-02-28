package org.iobserve.mobile.agent.module;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iobserve.mobile.agent.callback.CallbackManager;
import org.iobserve.mobile.agent.module.util.ConnectionState;
import org.iobserve.mobile.agent.module.util.ExecutionProperty;

import android.content.Context;
import android.webkit.WebView;
import rocks.inspectit.android.callback.data.NetRequestResponse;

/**
 * Special module which handles the instrumented network requests.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class NetworkModule extends AbstractAndroidModule {
	/**
	 * Time after which the existing connections get collected and sent to the
	 * monitoring server.
	 */
	private static final long COLLECT_AFTER = 30000L;

	/**
	 * Maps a single connection to a specific connection state.
	 */
	private Map<HttpURLConnection, ConnectionState> connectionStateMap;

	/**
	 * Executed when a connection is opened. This creates a new entry in the
	 * state map for the connection.
	 * 
	 * @param conn
	 *            the connection which has been created
	 */
	public void openConnection(HttpURLConnection conn) {
		if (!this.connectionStateMap.containsKey(conn)) {
			ConnectionState connState = new ConnectionState();
			connState.update(ConnectionState.ConnectionPoint.CONNECT);
			this.connectionStateMap.put(conn, connState);
		}
	}

	/**
	 * Executed when the response code of a connection is retrieved. This
	 * updates the connection state for the belonging connection.
	 * 
	 * @param conn
	 *            the connection
	 * @return the response code
	 * @throws IOException
	 *             thrown when {@link HttpURLConnection#getResponseCode()}
	 *             throws an exception
	 */
	public int getResponseCode(HttpURLConnection conn) throws IOException {
		if (connectionStateMap.containsKey(conn)) {
			int respCode = conn.getResponseCode();
			connectionStateMap.get(conn).update(ConnectionState.ConnectionPoint.RESPONSECODE);
			return respCode;
		} else {
			return conn.getResponseCode();
		}
	}

	/**
	 * Executed when the output stream of a connection is requested. This
	 * updates the connection state for the belonging connection.
	 * 
	 * @param conn
	 *            the connection
	 * @return the output stream for the connection
	 * @throws IOException
	 *             thrown when {@link HttpURLConnection#getOutputStream()}}
	 *             throws an exception
	 */
	public OutputStream getOutputStream(HttpURLConnection conn) throws IOException {
		if (connectionStateMap.containsKey(conn)) {
			OutputStream out = conn.getOutputStream();
			connectionStateMap.get(conn).update(ConnectionState.ConnectionPoint.OUTPUT);
			return out;
		} else {
			return conn.getOutputStream();
		}
	}

	/**
	 * Executed when the original application wants a {@link WebView} to load a
	 * specific URL.
	 * 
	 * @param url
	 *            the URL which is passed to the {@link WebView}
	 * @param method
	 *            the method of the request (either GET or POST)
	 */
	public void webViewLoad(String url, String method) {
		NetRequestResponse netRequest = new NetRequestResponse();

		netRequest.setDeviceId(androidDataCollector.getDeviceId());
		netRequest.setDuration(0);
		netRequest.setMethod(method);
		netRequest.setResponseCode(200);
		netRequest.setUrl(url);

		this.pushData(netRequest);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initModule(Context ctx) {
		connectionStateMap = new HashMap<HttpURLConnection, ConnectionState>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void shutdownModule() {
	}

	/**
	 * Executed every 60 seconds and looks whether there are closed connections
	 * and then passes data to the {@link CallbackManager} about the connection.
	 */
	@ExecutionProperty(interval = 60000L)
	public void collectData() {
		List<HttpURLConnection> removedConns = new ArrayList<HttpURLConnection>();

		for (HttpURLConnection conn : connectionStateMap.keySet()) {
			ConnectionState state = connectionStateMap.get(conn);
			if (state.getLastUpdatedDiff() >= COLLECT_AFTER) {
				if (state.probablyFinished()) {
					long startStamp = state.getPointTimestamp(ConnectionState.ConnectionPoint.CONNECT);
					// long outputStamp =
					// state.getPointTimestamp(ConnectionState.ConnectionPoint.OUTPUT);
					long responseStamp = state.getPointTimestamp(ConnectionState.ConnectionPoint.RESPONSECODE);

					try {
						int responseCode = conn.getResponseCode();
						String url = conn.getURL().toString();
						String method = conn.getRequestMethod();

						// LOG IT
						NetRequestResponse response = new NetRequestResponse();
						response.setDuration(responseStamp - startStamp);
						response.setMethod(method);
						response.setUrl(url);
						response.setResponseCode(responseCode);
						response.setDeviceId(androidDataCollector.getDeviceId());

						this.pushData(response);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				removedConns.add(conn);
			}
		}

		for (HttpURLConnection toRemove : removedConns) {
			connectionStateMap.remove(toRemove);
		}
	}
}
