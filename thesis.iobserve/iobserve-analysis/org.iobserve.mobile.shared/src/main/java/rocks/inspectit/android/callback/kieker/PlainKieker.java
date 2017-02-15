package rocks.inspectit.android.callback.kieker;

import kieker.common.record.IMonitoringRecord;
import rocks.fasterxml.jackson.databind.annotation.JsonDeserialize;
import rocks.fasterxml.jackson.databind.annotation.JsonSerialize;
import rocks.inspectit.android.callback.data.MobileDefaultData;

/**
 * Monitoring record which wraps a Kieker record.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class PlainKieker extends MobileDefaultData implements IKiekerCompatible {

	/**
	 * The Kieker record.
	 */
	@JsonSerialize(using = PlainKiekerSerializer.class)
	@JsonDeserialize(using = PlainKiekerDeserializer.class)
	private IMonitoringRecord record;

	/**
	 * Creates an empty instance.
	 */
	public PlainKieker() {
	}

	/**
	 * Creates a new instance which holds a Kieker monitoring record.
	 * 
	 * @param record
	 *            Kieker monitoring record.
	 */
	public PlainKieker(IMonitoringRecord record) {
		this.record = record;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMonitoringRecord generateRecord() {
		return this.record;
	}
}
