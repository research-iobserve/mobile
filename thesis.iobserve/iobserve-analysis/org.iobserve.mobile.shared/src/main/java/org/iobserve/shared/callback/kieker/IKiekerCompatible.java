package org.iobserve.shared.callback.kieker;

import kieker.common.record.IMonitoringRecord;

/**
 * Interface indicating that a monitoring record can be converted into a Kieker
 * record.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public interface IKiekerCompatible {
	/**
	 * Generates a corresponding Kieker record.
	 * 
	 * @return corresponding Kieker record
	 */
	public IMonitoringRecord generateRecord();
}
