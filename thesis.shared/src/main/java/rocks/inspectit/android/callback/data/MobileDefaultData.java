package rocks.inspectit.android.callback.data;

import rocks.fasterxml.jackson.annotation.JsonIgnoreProperties;
import rocks.fasterxml.jackson.annotation.JsonSubTypes;
import rocks.fasterxml.jackson.annotation.JsonTypeInfo;
import rocks.inspectit.android.callback.kieker.NetInfoResponse;
import rocks.inspectit.android.callback.kieker.PlainKieker;

/**
 * Parent class for mobile monitoring records.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = HelloRequest.class), @JsonSubTypes.Type(value = NetRequestResponse.class),
		@JsonSubTypes.Type(value = NetInfoResponse.class), @JsonSubTypes.Type(value = PlainKieker.class) })
public class MobileDefaultData {
}