package org.iobserve.analysis.filter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.iobserve.analysis.mobile.MobileConnectionState;
import org.iobserve.analysis.mobile.MobileMobileConnectionInfo;
import org.iobserve.analysis.mobile.MobileWifiConnectionInfo;
import org.iobserve.analysis.model.AllocationModelBuilder;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelBuilder;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.utils.Opt;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;
import org.palladiosimulator.pcm.repository.PrimitiveTypeEnum;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import rocks.inspectit.android.callback.kieker.MobileNetworkRequestEventRecord;
import teetime.framework.AbstractConsumerStage;

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

	public TNetworkRequest(final AllocationModelProvider allocationModelProvider,
			final SystemModelProvider systemModelProvider,
			final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
			final RepositoryModelProvider repositoryModelProvider) {
		this.allocationModelProvider = allocationModelProvider;
		this.systemModelProvider = systemModelProvider;
		this.resourceEnvironmentModelProvider = resourceEnvironmentModelProvider;
		this.repositoryModelProvider = repositoryModelProvider;
	}

	@Override
	protected void execute(MobileNetworkRequestEventRecord record) {
		String deviceId = record.getDeviceId();

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

		if (hostname == null || path == null)
			return;

		// adjusting resource environment
		ResourceContainer serverContainer = adjustResourceEnvironment(record.getDeviceId(), hostname);

		// adjust repository model
		String context = adjustRepositoryModel(hostname, path, record);

		// adjust system model
		AssemblyContext assemblyContext = adjustSystemModel(context, deviceId);

		// adjust allocation model
		adjustAllocationModel(serverContainer, assemblyContext);
	}

	private void adjustAllocationModel(ResourceContainer serverContainer, AssemblyContext assemblyContext) {
		this.allocationModelProvider.loadModel();

		AllocationModelBuilder.addAllocationContextIfAbsent(this.allocationModelProvider.getModel(), serverContainer,
				assemblyContext);

		this.allocationModelProvider.save();
	}

	private AssemblyContext adjustSystemModel(String belongingName, String deviceId) {
		this.systemModelProvider.loadModel();
		final org.palladiosimulator.pcm.system.System systemModel = systemModelProvider.getModel();

		final String asmContextName = belongingName + "_" + deviceId;
		final AssemblyContext assemblyContext = SystemModelBuilder.createAssemblyContextsIfAbsent(systemModel,
				asmContextName);

		this.systemModelProvider.save();

		return assemblyContext;
	}

	private String adjustRepositoryModel(String hostname, String path, MobileNetworkRequestEventRecord record) {
		this.repositoryModelProvider.loadModel();
		final Repository repo = this.repositoryModelProvider.getModel();

		// adjusting
		final String belongingName = "I" + hostname.replaceAll("\\.", "_");
		OperationInterface belonging = null;
		for (Interface iface : repo.getInterfaces__Repository()) {
			if (iface.getEntityName().equals(belongingName) && iface instanceof OperationInterface) {
				belonging = (OperationInterface) iface;
				break;
			}
		}

		if (belonging == null) {
			// create
			belonging = RepositoryFactory.eINSTANCE.createOperationInterface();
			belonging.setEntityName(belongingName);

			repo.getInterfaces__Repository().add(belonging);
		}

		// search for operation
		final String preparedPath = path.split("\\.")[0];
		final String operationName = record.getMethod().toLowerCase() + preparedPath.replaceAll("\\/", "_");

		OperationSignature signature = null;
		for (OperationSignature sig : belonging.getSignatures__OperationInterface()) {
			if (sig.getEntityName().equals(operationName)) {
				signature = sig;
				break;
			}
		}

		if (signature == null) {
			// create
			signature = RepositoryFactory.eINSTANCE.createOperationSignature();
			signature.setEntityName(operationName);

			final PrimitiveDataType stringDatatype = RepositoryFactory.eINSTANCE.createPrimitiveDataType();
			stringDatatype.setType(PrimitiveTypeEnum.INT);
			// signature.setReturnType__OperationSignature(stringDatatype);

			belonging.getSignatures__OperationInterface().add(signature);
		}

		this.repositoryModelProvider.save();

		return belongingName;
	}

	private ResourceContainer adjustResourceEnvironment(String deviceId, String hostname) {
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

			final MobileConnectionState connState = TNetworkEvent.connectionMapping.get(deviceId);

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
