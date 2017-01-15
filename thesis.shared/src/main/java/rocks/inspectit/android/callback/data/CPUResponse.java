package rocks.inspectit.android.callback.data;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.system.CPUUtilizationRecord;
import rocks.inspectit.android.callback.kieker.IKiekerCompatible;

/**
 * Created by DMO on 26.11.2016.
 */

public class CPUResponse extends MobileDefaultData implements IKiekerCompatible {

	private double usage;

	public double getUsage() {
		return usage;
	}

	public void setUsage(double usage) {
		this.usage = usage;
	}

	@Override
	public IMonitoringRecord generateRecord() {
		return new CPUUtilizationRecord(System.nanoTime(), "", "0", 0, 0, 0, 0, 0, usage, 0);
	}
}
