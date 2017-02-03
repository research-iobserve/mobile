package rocks.inspectit.android.module;

import android.content.Context;
import rocks.inspectit.android.callback.data.MemoryResponse;
import rocks.inspectit.android.module.util.ExecutionProperty;

/**
 * Module for collecting statistics about the memory usage.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class MemoryModule extends AbstractAndroidModule {

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
	 * Picks up the memory usage every 30 seconds.
	 */
	@ExecutionProperty(interval = 30000L)
	public void pickupUsage() {
		double currentUsage = androidDataCollector.getRamUsage();

		MemoryResponse memoryResponse = new MemoryResponse();
		memoryResponse.setPercentUsed(currentUsage);

		this.pushData(memoryResponse);
	}
}
