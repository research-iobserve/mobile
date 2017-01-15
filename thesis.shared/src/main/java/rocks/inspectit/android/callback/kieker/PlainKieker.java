package rocks.inspectit.android.callback.kieker;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import rocks.fasterxml.jackson.annotation.JsonSubTypes;
import rocks.fasterxml.jackson.annotation.JsonTypeInfo;
import rocks.inspectit.android.callback.data.MobileDefaultData;

/**
 * Created by DMO on 09.01.2017.
 */
public class PlainKieker extends MobileDefaultData implements IKiekerCompatible {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({@JsonSubTypes.Type(value = TraceMetadata.class), @JsonSubTypes.Type(value = BeforeOperationEvent.class), @JsonSubTypes.Type(value = AfterOperationEvent.class)})
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
