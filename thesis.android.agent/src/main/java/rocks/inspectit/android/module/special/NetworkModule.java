package rocks.inspectit.android.module.special;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import rocks.inspectit.android.callback.data.NetRequestResponse;
import rocks.inspectit.android.module.AbstractAndroidModule;
import rocks.inspectit.android.module.util.ConnectionState;
import rocks.inspectit.android.module.util.ExecutionProperty;

/**
 * Created by DMO on 09.12.2016.
 */

public class NetworkModule extends AbstractAndroidModule {

	private static final long COLLECT_AFTER = 60000L;

	private Map<HttpURLConnection, ConnectionState> connectionStateMap;

	public void openConnection(HttpURLConnection conn) {
		ConnectionState connState = new ConnectionState();
		connState.update(ConnectionState.ConnectionPoint.CONNECT);
		this.connectionStateMap.put(conn, connState);
	}

	public int getResponseCode(HttpURLConnection conn) throws IOException {
		if (connectionStateMap.containsKey(conn)) {
			int respCode = conn.getResponseCode();
			connectionStateMap.get(conn).update(ConnectionState.ConnectionPoint.RESPONSECODE);
			return respCode;
		} else {
			return conn.getResponseCode();
		}
	}

	public OutputStream getOutputStream(HttpURLConnection conn) throws IOException {
		if (connectionStateMap.containsKey(conn)) {
			OutputStream out = conn.getOutputStream();
			connectionStateMap.get(conn).update(ConnectionState.ConnectionPoint.OUTPUT);
			return out;
		} else {
			return conn.getOutputStream();
		}
	}

	@Override
	public void initModule(Context ctx) {
		connectionStateMap = new HashMap<HttpURLConnection, ConnectionState>();
	}

	@Override
	public void shutdownModule() {
	}

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
