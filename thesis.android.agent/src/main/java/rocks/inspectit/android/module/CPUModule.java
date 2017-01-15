package rocks.inspectit.android.module;

import android.content.Context;

import rocks.inspectit.android.callback.data.CPUResponse;
import rocks.inspectit.android.module.util.ExecutionProperty;

/**
 * Created by DMO on 25.11.2016.
 */

public class CPUModule extends AbstractAndroidModule {
    @Override
    public void initModule(Context ctx) {
    }

    @Override
    public void shutdownModule() {

    }

    @ExecutionProperty(interval = 30000L)
    public void pickupUsage() {
        float currentUsage = androidDataCollector.getCpuUsage();

        CPUResponse cpuResponse = new CPUResponse();
        cpuResponse.setUsage(currentUsage);

        this.pushData(cpuResponse);
    }
}
