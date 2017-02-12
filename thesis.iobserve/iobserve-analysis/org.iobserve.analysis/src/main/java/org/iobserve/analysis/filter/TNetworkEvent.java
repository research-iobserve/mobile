/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.mobile.MobileConnectionState;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.utils.Opt;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import rocks.inspectit.android.callback.kieker.MobileNetworkEventRecord;
import rocks.inspectit.android.callback.kieker.MobileNetworkRequestEventRecord;
import rocks.inspectit.android.callback.kieker.NetworkEvent;

/**
 * TMobile is responsible for the processing of monitoring records which belong
 * to mobile applications. It can handle the following records:
 * {@link MobileNetworkEventRecord} and {@link MobileNetworkRequestEventRecord}.
 * 
 * @author David Monschein
 */
public class TNetworkEvent extends AbstractConsumerStage<NetworkEvent> {
	/** logger. */
	private static final Log LOG = LogFactory.getLog(RecordSwitch.class);

	/** mapping of device to a connection state. */
	protected static final Map<String, MobileConnectionState> CONNECTIONMAPPING;

	/** further process output port. */
	private final OutputPort<MobileNetworkRequestEventRecord> outputPort = this.createOutputPort();

	/** reference to resource environment model provider. */
	private final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider;

	static {
		CONNECTIONMAPPING = new HashMap<String, MobileConnectionState>();
	}

	/**
	 * Creates a new TNetworkEVent filter.
	 */
	public TNetworkEvent(final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider) {
		this.resourceEnvironmentModelProvider = resourceEnvironmentModelProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void execute(final NetworkEvent element) {
		if (element instanceof MobileNetworkEventRecord) {
			final MobileNetworkEventRecord record = (MobileNetworkEventRecord) element;
			this.process(record);
		} else if (element instanceof MobileNetworkRequestEventRecord) {
			final MobileNetworkRequestEventRecord record = (MobileNetworkRequestEventRecord) element;
			if (CONNECTIONMAPPING.containsKey(record.getDeviceId())) {
				this.outputPort.send(record);
			} else {
				LOG.warn("No connection info present for device with id '" + record.getDeviceId() + "'.");
			}
		}
	}

	/**
	 * Processes a record with the type {@link MobileNetworkEventRecord}.
	 * 
	 * @param deviceId
	 *            the id of the device
	 * @param record
	 *            the record
	 */
	private void process(final MobileNetworkEventRecord record) {
		final String deviceId = record.getDeviceId();

		this.resourceEnvironmentModelProvider.loadModel();
		final ResourceEnvironment resourceEnvironment = this.resourceEnvironmentModelProvider.getModel();

		final Optional<ResourceContainer> deviceResourceContainer = ResourceEnvironmentModelBuilder
				.getResourceContainerByName(resourceEnvironment, deviceId);

		Opt.of(deviceResourceContainer).ifPresent().apply(container -> {
			TNetworkEvent.CONNECTIONMAPPING.put(deviceId, new MobileConnectionState(record));
		}).elseApply(() -> LOG.warn("Container for device with id '" + deviceId + "' doesn't exist."));

		// save model
		this.resourceEnvironmentModelProvider.save();
	}

	/**
	 * @return output port
	 */
	public OutputPort<MobileNetworkRequestEventRecord> getOutputPort() {
		return this.outputPort;
	}

}
