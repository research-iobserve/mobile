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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.iobserve.analysis.model.AllocationModelBuilder;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelBuilder;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.utils.Opt;
import org.iobserve.common.mobile.record.MobileNetworkRequestEventRecord;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import teetime.framework.AbstractConsumerStage;

/**
 * Transformation which consumes {@link MobileNetworkRequestEventRecord} and
 * performs adjustments at the PCM models.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class TNetworkRequest extends AbstractConsumerStage<MobileNetworkRequestEventRecord> {

	/**
	 * Whether we should check for a correspondent component of the network
	 * request hostname.
	 */
	private static final boolean ADJUST_ALLOCATION = true;

	/** logger. */
	private static final Log LOG = LogFactory.getLog(RecordSwitch.class);

	/** reference to allocation model provider. */
	private final AllocationModelProvider allocationModelProvider;
	/** reference to system model provider. */
	private final SystemModelProvider systemModelProvider;
	/** reference to resource environment model provider. */
	private final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider;
	/** reference to the correspondence model. */
	private final ICorrespondence correspondenceModel;

	/**
	 * Creates a new network request filter.
	 * 
	 * @param allocationModelProvider
	 *            reference to allocation model provider
	 * @param systemModelProvider
	 *            reference to system model provider
	 * @param resourceEnvironmentModelProvider
	 *            reference to resource environment model provider
	 * @param correspondenceModel
	 *            reference to the correspondence model
	 */
	public TNetworkRequest(final AllocationModelProvider allocationModelProvider,
			final SystemModelProvider systemModelProvider,
			final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
			final ICorrespondence correspondenceModel) {
		this.allocationModelProvider = allocationModelProvider;
		this.systemModelProvider = systemModelProvider;
		this.resourceEnvironmentModelProvider = resourceEnvironmentModelProvider;
		this.correspondenceModel = correspondenceModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void execute(final MobileNetworkRequestEventRecord record) {
		final String deviceId = record.getDeviceId();

		// prepare
		final String hostname;
		final String path;
		try {
			final URI temp = new URI(record.getUrl());
			hostname = temp.getHost();
			path = temp.getPath();
		} catch (URISyntaxException e) {
			LOG.error("Failed to extract the host from '" + record.getUrl() + "'.");
			return;
		}

		if (hostname == null || path == null) {
			return;
		}

		// adjusting resource environment
		final ResourceContainer serverContainer = adjustResourceEnvironment(deviceId, hostname);

		if (ADJUST_ALLOCATION) {
			// check correspondence
			Opt.of(correspondenceModel.getCorrespondent(hostname)).ifPresent().apply(correspondent -> {
				String entityName = correspondent.getPcmEntityName();
				adjustSystemAndAllocation(serverContainer, entityName, hostname);
			}).elseApply(() -> {
				LOG.warn("Correspondent for server '" + hostname
						+ "' not found and no adjustments of the repository are allowed.");
			});
		}
	}

	/**
	 * Performs the adjustments at the system model and the allocation model
	 * 
	 * @param serverContainer
	 *            the resource container of the server
	 * @param context
	 *            the name of the context (entity name of the component in the
	 *            repository)
	 * @param hostname
	 *            the hostname of the server
	 */
	private void adjustSystemAndAllocation(ResourceContainer serverContainer, String context, String hostname) {
		// adjust system model
		final AssemblyContext assemblyContext = adjustSystemModel(context, hostname);

		// adjust allocation model
		adjustAllocationModel(serverContainer, assemblyContext);
	}

	/**
	 * Performs adjustments at the allocation model.
	 * 
	 * @param serverContainer
	 *            the server container
	 * @param assemblyContext
	 *            the assembly context
	 */
	private void adjustAllocationModel(final ResourceContainer serverContainer, final AssemblyContext assemblyContext) {
		this.allocationModelProvider.loadModel();

		AllocationModelBuilder.addAllocationContextIfAbsent(this.allocationModelProvider.getModel(), serverContainer,
				assemblyContext);

		this.allocationModelProvider.save();
	}

	/**
	 * Performs adjustments at the system model.
	 * 
	 * @param belongingName
	 *            name of the assembly context of the system
	 * @param deviceId
	 *            the id of the device
	 * @return assembly context of the application
	 */
	private AssemblyContext adjustSystemModel(final String belongingName, final String containerName) {
		this.systemModelProvider.loadModel();
		final org.palladiosimulator.pcm.system.System systemModel = systemModelProvider.getModel();

		final String asmContextName = belongingName + "_" + containerName;
		final AssemblyContext assemblyContext = SystemModelBuilder.createAssemblyContextsIfAbsent(systemModel,
				asmContextName);

		this.systemModelProvider.save();

		return assemblyContext;
	}

	/**
	 * Adjusts the resource container for the server which is requested.
	 * 
	 * @param deviceId
	 *            the id of the device
	 * @param hostname
	 *            the name of the host
	 * @return creates a connection between the device and the host and returns
	 *         the resource container of the server
	 */
	private ResourceContainer adjustResourceEnvironment(final String deviceId, final String hostname) {
		this.resourceEnvironmentModelProvider.loadModel();
		final ResourceEnvironment resourceEnvironment = this.resourceEnvironmentModelProvider.getModel();

		// look for device container
		final Optional<ResourceContainer> deviceResourceContainer = ResourceEnvironmentModelBuilder
				.getResourceContainerByName(resourceEnvironment, deviceId);

		// look for server container
		final ResourceContainer serverContainer = this.buildIfNotPresent(resourceEnvironment, hostname).get();

		// adjust resource env model
		ResourceEnvironmentModelBuilder.connectResourceContainerExtended(resourceEnvironment,
				deviceResourceContainer.get(), serverContainer, TNetworkEvent.CONNECTIONMAPPING.get(deviceId));
		// save model
		this.resourceEnvironmentModelProvider.save();

		return serverContainer;
	}

	/**
	 * Creates a resource container with a specified name in the case it doesn't
	 * exist already.
	 * 
	 * @param env
	 *            the resource environment model
	 * @param name
	 *            the name of the resource container
	 * @return an optional which either contains the existing resource container
	 *         or the created one
	 */
	private Optional<ResourceContainer> buildIfNotPresent(final ResourceEnvironment env, final String name) {
		final Optional<ResourceContainer> deviceResourceContainer = ResourceEnvironmentModelBuilder
				.getResourceContainerByName(env, name);

		if (deviceResourceContainer.isPresent()) {
			return deviceResourceContainer;
		} else {
			ResourceEnvironmentModelBuilder.createResourceContainer(env, name);
			return ResourceEnvironmentModelBuilder.getResourceContainerByName(env, name);
		}
	}

}
