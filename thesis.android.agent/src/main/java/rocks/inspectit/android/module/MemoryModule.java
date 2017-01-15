package rocks.inspectit.android.module;

import android.content.Context;

import rocks.inspectit.android.callback.data.MemoryResponse;
import rocks.inspectit.android.module.util.ExecutionProperty;

/**
 * Created by DMO on 29.11.2016.
 */

public class MemoryModule extends AbstractAndroidModule {

    @Override
    public void initModule(Context ctx) {
    }

    @Override
    public void shutdownModule() {
    }

    @ExecutionProperty(interval = 30000L)
    public void pickupUsage() {
        double currentUsage = androidDataCollector.getRamUsage();

        MemoryResponse memoryResponse = new MemoryResponse();
        memoryResponse.setPercentUsed(currentUsage);

        this.pushData(memoryResponse);
    }
}
