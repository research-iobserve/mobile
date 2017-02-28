package org.iobserve.mobile.agent.callback.strategies;

import org.iobserve.shared.callback.data.MobileDefaultData;

/**
 * Strategy which immediately sends all data objects to the server. This means
 * every data object is sent alone to the server and therefore this strategy
 * produces a high overhead and is only suggested for debug purposes.
 * 
 * @author David Monschein
 * @author RobertHeinrich
 *
 */
public class ImmediatelyStrategy extends AbstractCallbackStrategy {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void addData(MobileDefaultData data) {
		this.data.addChildData(data);

		// DIRECTLY SEND
		super.sendBeacon();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() {

	}
}
