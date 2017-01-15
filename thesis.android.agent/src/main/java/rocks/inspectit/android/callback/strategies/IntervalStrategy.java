package rocks.inspectit.android.callback.strategies;

import android.os.Handler;

import rocks.inspectit.android.callback.data.MobileDefaultData;

/**
 * Created by David on 26.10.16.
 */

public class IntervalStrategy extends AbstractCallbackStrategy {

    private long intervalLength;
    private Handler mHandler = new Handler();
    private Runnable mHandlerTask;

    public IntervalStrategy(final long intervalLength) {
        this.intervalLength = intervalLength;

        // INIT REPEATING TASK
        mHandlerTask = new Runnable() {
            @Override
            public void run() {
                sendData();
                mHandler.postDelayed(mHandlerTask, intervalLength);
            }
        };
        mHandlerTask.run();
    }

    @Override
    public synchronized void addData(MobileDefaultData data) {
        super.data.addChildData(data);
    }

    @Override
    public void stop() {
        mHandler.removeCallbacks(mHandlerTask);
    }

    public long getIntervalLength() {
        return intervalLength;
    }

    public void setIntervalLength(long intervalLength) {
        this.intervalLength = intervalLength;
    }

    private void sendData() {
        super.sendBeacon();
    }
}
