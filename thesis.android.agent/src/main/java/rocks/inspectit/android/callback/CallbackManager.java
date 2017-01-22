package rocks.inspectit.android.callback;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import kieker.common.record.IMonitoringRecord;
import rocks.inspectit.android.ExternalConfiguration;
import rocks.inspectit.android.Tag;
import rocks.inspectit.android.callback.data.HelloRequest;
import rocks.inspectit.android.callback.data.MobileCallbackData;
import rocks.inspectit.android.callback.data.MobileDefaultData;
import rocks.inspectit.android.callback.kieker.PlainKieker;
import rocks.inspectit.android.callback.strategies.AbstractCallbackStrategy;
import rocks.inspectit.android.util.DependencyManager;

/**
 * Created by David on 25.10.16.
 */

public class CallbackManager {

    private static final String LOG_TAG = ExternalConfiguration.getLogTag();

    private AbstractCallbackStrategy strategy = DependencyManager.getCallbackStrategy();
    private boolean sessActive;
    private List<MobileDefaultData> sessQueue;

    public CallbackManager() {
        this.sessActive = false;
        this.sessQueue = new ArrayList<MobileDefaultData>();
    }

    public void pushData(MobileDefaultData data) {
        if (!sessActive) {
            this.sessQueue.add(data);
        } else {
            this.strategy.addData(data);
        }
    }

    public void pushKiekerData(IMonitoringRecord record) {
        this.pushData(new PlainKieker(record));
    }

    public void pushHelloMessage(HelloRequest response, Set<Tag> tagList) {
        MobileCallbackData data = new MobileCallbackData();
        data.setTagList(new ArrayList<Tag>(tagList));
        data.setSessionId(null);

        List<MobileDefaultData> childs = new ArrayList<MobileDefaultData>();
        childs.add(response);

        data.setChildData(childs);

        // directly send it
        strategy.sendImmediately(data, true);
    }

    public void shutdown() {
        strategy.stop();
    }

    private void swapQueue() {
        for (MobileDefaultData data : sessQueue) {
            strategy.addData(data);
        }
        sessQueue.clear();
    }

    public void applySessionId(String id) {
        if (!sessActive) {
            Log.i(LOG_TAG, "Created session with id '" + id + "' for communicating with the CMR.");

            strategy.setSessId(id);
            sessActive = true;

            swapQueue();
        }
    }
}
