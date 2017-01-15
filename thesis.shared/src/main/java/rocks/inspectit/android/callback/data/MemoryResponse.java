package rocks.inspectit.android.callback.data;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.system.ResourceUtilizationRecord;
import rocks.inspectit.android.callback.kieker.IKiekerCompatible;

/**
 * Created by DMO on 29.11.2016.
 */

public class MemoryResponse extends MobileDefaultData implements IKiekerCompatible {

	private double percentUsed;

	public double getPercentUsed() {
		return percentUsed;
	}

	public void setPercentUsed(double percentUsed) {
		this.percentUsed = percentUsed;
	}

	@Override
	public IMonitoringRecord generateRecord() {
		return new ResourceUtilizationRecord(System.nanoTime(), "", "memory", percentUsed);
	}
}
