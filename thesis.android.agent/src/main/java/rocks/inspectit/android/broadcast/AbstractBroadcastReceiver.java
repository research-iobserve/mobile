package rocks.inspectit.android.broadcast;

import android.content.BroadcastReceiver;

import rocks.inspectit.android.AndroidDataCollector;
import rocks.inspectit.android.callback.CallbackManager;
import rocks.inspectit.android.callback.data.MobileDefaultData;

/**
 * Created by DMO on 04.01.2017.
 */
public abstract class AbstractBroadcastReceiver extends BroadcastReceiver {

    protected AndroidDataCollector androidDataCollector;
    private CallbackManager callbackManager;

    abstract public String[] getFilterActions();

    public void setAndroidDataCollector(AndroidDataCollector androidDataCollector) {
        this.androidDataCollector = androidDataCollector;
    }

    public void setCallbackManager(CallbackManager callbackManager) {
        this.callbackManager = callbackManager;
    }

    protected void pushData(MobileDefaultData data) {
        callbackManager.pushData(data);
    }
}
