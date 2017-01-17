package rocks.inspectit.android.callback.kieker;

import kieker.common.record.IMonitoringRecord;
import rocks.fasterxml.jackson.annotation.JsonTypeInfo;
import rocks.fasterxml.jackson.databind.annotation.JsonDeserialize;
import rocks.inspectit.android.callback.data.MobileDefaultData;

/**
 * Created by DMO on 09.01.2017.
 */

public class PlainKieker extends MobileDefaultData implements IKiekerCompatible {

	@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, include = JsonTypeInfo.As.PROPERTY, property = "type")
	@JsonDeserialize(using = PlainKiekerDeserializer.class)
	private IMonitoringRecord record;

	public PlainKieker() {
	}

	public PlainKieker(IMonitoringRecord record) {
		this.record = record;
	}

	@Override
	public IMonitoringRecord generateRecord() {
		return this.record;
	}
}
