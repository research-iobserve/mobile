package rocks.inspectit.android.module;

import android.content.Context;

import rocks.inspectit.android.AndroidDataCollector;
import rocks.inspectit.android.callback.CallbackManager;
import rocks.inspectit.android.callback.data.MobileDefaultData;

/**
 * Created by David on 23.10.16.
 */

abstract public class AbstractAndroidModule {

    protected AndroidDataCollector androidDataCollector;
    private CallbackManager callbackManager;

    abstract public void initModule(Context ctx);

    abstract public void shutdownModule();

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
