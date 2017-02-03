package rocks.inspectit.android.module;

import android.content.Context;
import rocks.inspectit.android.callback.data.CPUResponse;
import rocks.inspectit.android.module.util.ExecutionProperty;

/**
 * Module which is responsible for collecting cpu usage metrics.
 * 
 * @author David Monschein
 * @author RobertHeinrich
 *
 */
public class CPUModule extends AbstractAndroidModule {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initModule(Context ctx) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void shutdownModule() {

	}

	/**
	 * Picks up the CPU usage every 30 seconds.
	 */
	@ExecutionProperty(interval = 30000L)
	public void pickupUsage() {
		float currentUsage = androidDataCollector.getCpuUsage();

		CPUResponse cpuResponse = new CPUResponse();
		cpuResponse.setUsage(currentUsage);

		this.pushData(cpuResponse);
	}
}
