package rocks.inspectit.android.callback.strategies;

import android.os.Handler;
import rocks.inspectit.android.callback.data.MobileDefaultData;

/**
 * Callback strategy which collects data over a specified period of time and
 * afterwards sends a package which contains all data. This strategy is
 * recommended for production use.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class IntervalStrategy extends AbstractCallbackStrategy {

	/**
	 * The length of the interval.
	 */
	private long intervalLength;

	/**
	 * Handler for scheduling tasks.
	 */
	private Handler mHandler = new Handler();

	/**
	 * Periodically executed task.
	 */
	private Runnable mHandlerTask;

	/**
	 * Boolean which indicates whether the strategy is already running or not.
	 */
	private boolean alreadyRunning;

	/**
	 * Creates a new interval callback strategy with a given interval length.
	 * 
	 * @param intervalLength
	 *            the length of the interval in milliseconds
	 */
	public IntervalStrategy(final long intervalLength) {
		alreadyRunning = false;
		this.setIntervalLength(intervalLength);

		// INIT REPEATING TASK
		mHandlerTask = new Runnable() {
			@Override
			public void run() {
				sendData();
				mHandler.postDelayed(mHandlerTask, intervalLength);
			}
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void addData(MobileDefaultData data) {
		super.data.addChildData(data);
		if (!alreadyRunning) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mHandlerTask.run();
				}
			}, 10000);
			alreadyRunning = true;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() {
		mHandler.removeCallbacks(mHandlerTask);
	}

	/**
	 * @return the intervalLength
	 */
	public long getIntervalLength() {
		return intervalLength;
	}

	/**
	 * @param intervalLength
	 *            the intervalLength to set
	 */
	public void setIntervalLength(long intervalLength) {
		this.intervalLength = intervalLength;
	}

	/**
	 * Sends the data gathered in the last interval period to the server.
	 */
	private void sendData() {
		super.sendBeacon();
	}
}
