package rocks.inspectit.android.callback.data;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.system.ResourceUtilizationRecord;
import rocks.inspectit.android.callback.kieker.IKiekerCompatible;

/**
 * Class holding information about the current memory usage of a mobile device.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class MemoryResponse extends MobileDefaultData implements IKiekerCompatible {

	/**
	 * Percent of the RAM which are in use.
	 */
	private double percentUsed;

	/**
	 * @return the percentUsed
	 */
	public double getPercentUsed() {
		return percentUsed;
	}

	/**
	 * @param percentUsed
	 *            the percentUsed to set
	 */
	public void setPercentUsed(double percentUsed) {
		this.percentUsed = percentUsed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMonitoringRecord generateRecord() {
		return new ResourceUtilizationRecord(System.nanoTime(), "", "memory", percentUsed);
	}
}
