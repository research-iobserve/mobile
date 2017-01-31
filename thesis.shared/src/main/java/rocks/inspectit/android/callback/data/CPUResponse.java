package rocks.inspectit.android.callback.data;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.system.CPUUtilizationRecord;
import rocks.inspectit.android.callback.kieker.IKiekerCompatible;

/**
 * Class for holding information about the cpu usage of the mobile device.
 * 
 * @author David Monschein
 */
public class CPUResponse extends MobileDefaultData implements IKiekerCompatible {

	private double usage;

	/**
	 * Creates a new object containing a CPU usage of 0.0%.
	 */
	public CPUResponse() {
	}

	/**
	 * Gets the usage.
	 * 
	 * @return usage of the CPU in percent.
	 */
	public double getUsage() {
		return usage;
	}

	/**
	 * Sets the usage in percent.
	 * 
	 * @param usage
	 *            The usage in percent.
	 */
	public void setUsage(final double usage) {
		this.usage = usage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMonitoringRecord generateRecord() {
		return new CPUUtilizationRecord(System.nanoTime(), "", "0", 0, 0, 0, 0, 0, usage, 0);
	}
}
