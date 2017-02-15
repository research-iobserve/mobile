package rocks.inspectit.android.module.util;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the state of a {@link HttpURLConnection}.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class ConnectionState {
	/**
	 * Used to determine which points of a connection has been already reached.
	 */
	private Map<ConnectionPoint, Boolean> reachedPointsMap;

	/**
	 * Relates to the {@link ConnectionState#reachedPointsMap} and specifies the
	 * timestamp when each point has been reached.
	 */
	private Map<ConnectionPoint, Long> reachedPointsTimestamps;

	/**
	 * The timestamp when the connection was updated the last time.
	 */
	private long lastUpdate;

	/**
	 * Creates a new connection state. Therefore every connection point is
	 * marked as not reached.
	 */
	public ConnectionState() {
		this.reachedPointsMap = new HashMap<ConnectionPoint, Boolean>();
		this.reachedPointsTimestamps = new HashMap<ConnectionPoint, Long>();

		for (ConnectionPoint p : ConnectionPoint.values()) {
			reachedPointsMap.put(p, false);
		}

		lastUpdate = System.currentTimeMillis();
	}

	/**
	 * Gets the timestamp when a certain point was reached.
	 * 
	 * @param point
	 *            connection point
	 * @return timestamp when the point was reached
	 */
	public long getPointTimestamp(ConnectionPoint point) {
		if (reachedPointsTimestamps.containsKey(point)) {
			return reachedPointsTimestamps.get(point);
		} else {
			return -1L;
		}
	}

	/**
	 * Updates the connection state with a given point.
	 * 
	 * @param point
	 *            the point which has been reached.
	 */
	public void update(ConnectionPoint point) {
		if (point != null && reachedPointsMap.containsKey(point)) {
			if (reachedPointsMap.get(point) == false) {
				reachedPointsTimestamps.put(point, System.currentTimeMillis());
				reachedPointsMap.put(point, true);

				lastUpdate = System.currentTimeMillis();
			}
		}
	}

	/**
	 * Predicts whether it is possible that the connection is finished.
	 * 
	 * @return true if the connection is probably finished - false otherwise
	 */
	public boolean probablyFinished() {
		return reachedPointsMap.get(ConnectionPoint.OUTPUT) || reachedPointsMap.get(ConnectionPoint.RESPONSECODE);
	}

	/**
	 * Gets the time when the state was updated the last time.
	 * 
	 * @return time when the state was updated the last time
	 */
	public long getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * Gets the time between now and the time when the state was updated the
	 * last time
	 * 
	 * @return the time when the state was updated the last time
	 */
	public long getLastUpdatedDiff() {
		return System.currentTimeMillis() - getLastUpdate();
	}

	/**
	 * Enum holding all specified connection points.
	 * 
	 * @author David Monschein
	 * @author Robert Heinrich
	 *
	 */
	public enum ConnectionPoint {
		/**
		 * When the connection gets established.
		 */
		CONNECT,
		/**
		 * When the output stream of the connection is opened.
		 */
		OUTPUT,
		/**
		 * When the response code of the connection is accessed.
		 */
		RESPONSECODE
	}
}
