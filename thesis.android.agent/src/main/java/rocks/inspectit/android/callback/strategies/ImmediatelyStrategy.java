package rocks.inspectit.android.callback.strategies;

import rocks.inspectit.android.callback.data.MobileDefaultData;

/**
 * Created by David on 26.10.16.
 */

public class ImmediatelyStrategy extends AbstractCallbackStrategy {
    @Override
    public synchronized void addData(MobileDefaultData data) {
        this.data.addChildData(data);

        // DIRECTLY SEND
        super.sendBeacon();
    }

    @Override
    public void stop() {

    }
}
