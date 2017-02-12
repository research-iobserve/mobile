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

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

import teetime.framework.AbstractConsumerStage;

import org.iobserve.analysis.mobile.MobileConnectionState;
import org.iobserve.analysis.mobile.MobileMobileConnectionInfo;
import org.iobserve.analysis.mobile.MobileWifiConnectionInfo;
import org.iobserve.analysis.model.AllocationModelBuilder;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelBuilder;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelBuilder;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.utils.Opt;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

import rocks.inspectit.android.callback.kieker.MobileNetworkRequestEventRecord;

/**
 * Transformation which consumes {@link MobileNetworkRequestEventRecord} and
 * performs adjustments at the PCM models.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class TNetworkRequest extends AbstractConsumerStage<MobileNetworkRequestEventRecord> {

	/** logger. */
	private static final Log LOG = LogFactory.getLog(RecordSwitch.class);

	/** reference to allocation model provider. */
	private final AllocationModelProvider allocationModelProvider;
	/** reference to system model provider. */
	private final SystemModelProvider systemModelProvider;
	/** reference to resource environment model provider. */
	private final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider;
	/** reference to repository model provider. */
	private final RepositoryModelProvider repositoryModelProvider;

	/**
	 * Creates a new network request filter.
	 * 
	 * @param allocationModelProvider
	 *            reference to allocation model provider
	 * @param systemModelProvider
	 *            reference to system model provider
	 * @param resourceEnvironmentModelProvider
	 *            reference to resource environment model provider
	 * @param repositoryModelProvider
	 *            reference to repository model provider
	 */
	public TNetworkRequest(final AllocationModelProvider allocationModelProvider,
			final SystemModelProvider systemModelProvider,
			final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
			final RepositoryModelProvider repositoryModelProvider) {
		this.allocationModelProvider = allocationModelProvider;
		this.systemModelProvider = systemModelProvider;
		this.resourceEnvironmentModelProvider = resourceEnvironmentModelProvider;
		this.repositoryModelProvider = repositoryModelProvider;
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
		final ResourceContainer serverContainer = adjustResourceEnvironment(record.getDeviceId(), hostname);

		// adjust repository model
		final String context = adjustRepositoryModel(hostname, path, record);

		// adjust system model
		final AssemblyContext assemblyContext = adjustSystemModel(context, deviceId);

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
	private AssemblyContext adjustSystemModel(final String belongingName, final String deviceId) {
		this.systemModelProvider.loadModel();
		final org.palladiosimulator.pcm.system.System systemModel = systemModelProvider.getModel();

		final String asmContextName = belongingName + "_" + deviceId;
		final AssemblyContext assemblyContext = SystemModelBuilder.createAssemblyContextsIfAbsent(systemModel,
				asmContextName);

		this.systemModelProvider.save();

		return assemblyContext;
	}

	/**
	 * Adjusts the repository model for the service which is requested by the
	 * application.
	 * 
	 * @param hostname
	 *            hostname url or address
	 * @param path
	 *            the path
	 * @param record
	 *            the request record
	 * @return the name of the belonging operation interface
	 */
	private String adjustRepositoryModel(final String hostname, final String path,
			final MobileNetworkRequestEventRecord record) {
		this.repositoryModelProvider.loadModel();
		final Repository repo = this.repositoryModelProvider.getModel();

		// adjusting
		final String belongingName = "I" + hostname.replaceAll("\\.", "_");
		final OperationInterface belonging = RepositoryModelBuilder.createOperationInterfaceIfAbsent(repo,
				belongingName);

		// search for operation
		final String preparedPath = path.split("\\.")[0];
		final String operationName = record.getMethod().toLowerCase() + preparedPath.replaceAll("\\/", "_");

		RepositoryModelBuilder.createOperationSignatureIfAbsent(belonging, operationName);

		this.repositoryModelProvider.save();

		return belongingName;
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
		Opt.of(deviceResourceContainer).ifPresent().apply(deviceContainer -> {
			final LinkingResource connectionLink = ResourceEnvironmentModelBuilder
					.connectResourceContainer(resourceEnvironment, deviceContainer, serverContainer);

			CommunicationLinkResourceSpecification specification = connectionLink
					.getCommunicationLinkResourceSpecifications_LinkingResource();
			if (specification == null) {
				// create a specification
				specification = ResourceenvironmentFactory.eINSTANCE.createCommunicationLinkResourceSpecification();
			}

			final MobileConnectionState connState = TNetworkEvent.CONNECTIONMAPPING.get(deviceId);

			// bring in the info
			specification.setConnectionType(connState.getConnectionType().getStringExpression());
			switch (connState.getConnectionType()) {
			case MOBILE:
				final MobileMobileConnectionInfo info = (MobileMobileConnectionInfo) connState.getConnectionInfo();
				specification.setProtocol(info.getProtocol());
				specification.setOperator(info.getProvider());
				break;
			case WLAN:
				final MobileWifiConnectionInfo wifiinfo = (MobileWifiConnectionInfo) connState.getConnectionInfo();
				specification.setSsid(wifiinfo.getSsid());
				specification.setBssid(wifiinfo.getBssid());
				specification.setProtocol(wifiinfo.getProtocol());
				break;
			default:
				LOG.warn("Found an request without an active connection.");
			}

			// set it
			connectionLink.setCommunicationLinkResourceSpecifications_LinkingResource(specification);
		}).elseApply(() -> LOG.warn("Container for device with id '" + deviceId + "' doesn't exist."));
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
