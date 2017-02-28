package org.iobserve.shared.callback.data;

import org.iobserve.shared.callback.kieker.NetInfoResponse;
import org.iobserve.shared.callback.kieker.PlainKieker;

import rocks.fasterxml.jackson.annotation.JsonIgnoreProperties;
import rocks.fasterxml.jackson.annotation.JsonSubTypes;
import rocks.fasterxml.jackson.annotation.JsonTypeInfo;
import rocks.inspectit.android.callback.data.HelloRequest;
import rocks.inspectit.android.callback.data.NetRequestResponse;

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