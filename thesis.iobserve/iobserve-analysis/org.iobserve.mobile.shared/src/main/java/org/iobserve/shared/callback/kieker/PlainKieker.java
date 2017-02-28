package org.iobserve.shared.callback.kieker;

import org.iobserve.shared.callback.data.MobileDefaultData;

import kieker.common.record.IMonitoringRecord;
import rocks.fasterxml.jackson.databind.annotation.JsonDeserialize;
import rocks.fasterxml.jackson.databind.annotation.JsonSerialize;

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
