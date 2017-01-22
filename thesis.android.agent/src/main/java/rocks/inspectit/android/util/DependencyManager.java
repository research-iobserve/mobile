package rocks.inspectit.android.util;

import rocks.inspectit.android.AndroidDataCollector;
import rocks.inspectit.android.TagCollector;
import rocks.inspectit.android.callback.CallbackManager;
import rocks.inspectit.android.callback.strategies.AbstractCallbackStrategy;

/**
 * Created by DMO on 17.12.2016.
 */

public class DependencyManager {
    private static CallbackManager callbackManager;
    private static AndroidDataCollector androidDataCollector;
    private static TagCollector tagCollector;
    private static AbstractCallbackStrategy callbackStrategy;

    public static CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public static void setCallbackManager(CallbackManager callbackManager) {
        DependencyManager.callbackManager = callbackManager;
    }

    public static AndroidDataCollector getAndroidDataCollector() {
        return androidDataCollector;
    }

    public static void setAndroidDataCollector(AndroidDataCollector androidDataCollector) {
        DependencyManager.androidDataCollector = androidDataCollector;
    }

    public static TagCollector getTagCollector() {
        return tagCollector;
    }

    public static void setTagCollector(TagCollector tagCollector) {
        DependencyManager.tagCollector = tagCollector;
    }

    public static AbstractCallbackStrategy getCallbackStrategy() {
        return callbackStrategy;
    }

    public static void setCallbackStrategy(AbstractCallbackStrategy callbackStrategy) {
        DependencyManager.callbackStrategy = callbackStrategy;
    }
}
