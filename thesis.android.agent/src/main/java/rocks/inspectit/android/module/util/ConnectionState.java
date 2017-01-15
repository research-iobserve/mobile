package rocks.inspectit.android.module.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DMO on 09.12.2016.
 */

public class ConnectionState {
	private Map<ConnectionPoint, Boolean> reachedPointsMap;
	private Map<ConnectionPoint, Long> reachedPointsTimestamps;
	private long lastUpdate;

	public ConnectionState() {
		this.reachedPointsMap = new HashMap<ConnectionPoint, Boolean>();
		this.reachedPointsTimestamps = new HashMap<ConnectionPoint, Long>();

		for (ConnectionPoint p : ConnectionPoint.values()) {
			reachedPointsMap.put(p, false);
		}

		lastUpdate = System.currentTimeMillis();
	}

	public long getPointTimestamp(ConnectionPoint point) {
		if (reachedPointsTimestamps.containsKey(point)) {
			return reachedPointsTimestamps.get(point);
		} else {
			return -1L;
		}
	}

	public void update(ConnectionPoint point) {
		if (point != null && reachedPointsMap.containsKey(point)) {
			if (reachedPointsMap.get(point) == false) {
				reachedPointsTimestamps.put(point, System.currentTimeMillis());
				reachedPointsMap.put(point, true);

				lastUpdate = System.currentTimeMillis();
			}
		}
	}

	public boolean probablyFinished() {
		return reachedPointsMap.get(ConnectionPoint.OUTPUT) || reachedPointsMap.get(ConnectionPoint.RESPONSECODE);
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public long getLastUpdatedDiff() {
		return System.currentTimeMillis() - getLastUpdate();
	}

	public enum ConnectionPoint {
		CONNECT, OUTPUT, RESPONSECODE
	}
}
