package org.iobserve.analysis.filter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
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
import org.iobserve.analysis.model.correspondence.Correspondent;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
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
import rocks.inspectit.android.callback.kieker.MobileDeploymentRecord;
import rocks.inspectit.android.callback.kieker.MobileNetworkEventRecord;
import rocks.inspectit.android.callback.kieker.MobileNetworkRequestEventRecord;
import rocks.inspectit.android.callback.kieker.MobileRecord;
import teetime.framework.AbstractConsumerStage;

public class TMobile extends AbstractConsumerStage<MobileRecord> {
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
	/** mapping of device to a connection state */
	private final Map<String, MobileConnectionState> connectionMapping;
	/** correspondence mapping */
	private final ICorrespondence correspondence;

	public TMobile(final ICorrespondence correspondence, final AllocationModelProvider allocationModelProvider,
			final SystemModelProvider systemModelProvider,
			final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
			final RepositoryModelProvider repositoryModelProvider) {
		this.correspondence = correspondence;
		this.allocationModelProvider = allocationModelProvider;
		this.systemModelProvider = systemModelProvider;
		this.resourceEnvironmentModelProvider = resourceEnvironmentModelProvider;
		this.repositoryModelProvider = repositoryModelProvider;
		this.connectionMapping = new HashMap<String, MobileConnectionState>();
	}

	@Override
	protected void execute(MobileRecord element) {
		if (element instanceof MobileNetworkEventRecord) {
			this.process(element.getDeviceId(), (MobileNetworkEventRecord) element);
		} else if (element instanceof MobileNetworkRequestEventRecord) {
			this.process(element.getDeviceId(), (MobileNetworkRequestEventRecord) element);
		} else if (element instanceof MobileDeploymentRecord) {
			this.updateModel(element.getDeviceId(), (MobileDeploymentRecord) element);
		}
	}

	private void process(String deviceId, MobileNetworkEventRecord record) {

		this.resourceEnvironmentModelProvider.loadModel();
		final ResourceEnvironment resourceEnvironment = this.resourceEnvironmentModelProvider.getModel();

		Optional<ResourceContainer> deviceResourceContainer = ResourceEnvironmentModelBuilder
				.getResourceContainerByName(resourceEnvironment, deviceId);

		Opt.of(deviceResourceContainer).ifPresent().apply(container -> {
			TMobile.this.connectionMapping.put(deviceId, new MobileConnectionState(record));
		}).elseApply(() -> LOG.warn("Container for device with id '" + deviceId + "' doesn't exist."));

		// save model
		this.resourceEnvironmentModelProvider.save();
	}

	private void process(String deviceId, MobileNetworkRequestEventRecord record) {
		if (!connectionMapping.containsKey(deviceId)) {
			LOG.warn("No connection info present for device '" + deviceId + "'.");
			return;
		}

		// prework
		final String hostname;
		final String path;
		try {
			URI temp = new URI(record.getUrl());
			hostname = temp.getHost();
			path = temp.getPath();
		} catch (URISyntaxException e) {
			LOG.error("Failed to extract the host from '" + record.getUrl() + "'.");
			return;
		}

		// adjusting
		this.resourceEnvironmentModelProvider.loadModel();
		final ResourceEnvironment resourceEnvironment = this.resourceEnvironmentModelProvider.getModel();

		// look for device container
		Optional<ResourceContainer> deviceResourceContainer = ResourceEnvironmentModelBuilder
				.getResourceContainerByName(resourceEnvironment, deviceId);

		// look for server container
		ResourceContainer serverContainer = this.buildIfNotPresent(resourceEnvironment, hostname).get();

		// adjust resource env model
		Opt.of(deviceResourceContainer).ifPresent().apply(deviceContainer -> {
			LinkingResource connectionLink = ResourceEnvironmentModelBuilder
					.connectResourceContainer(resourceEnvironment, deviceContainer, serverContainer);

			CommunicationLinkResourceSpecification specification = connectionLink
					.getCommunicationLinkResourceSpecifications_LinkingResource();
			if (specification == null) {
				// create a specification
				specification = ResourceenvironmentFactory.eINSTANCE.createCommunicationLinkResourceSpecification();
			}

			MobileConnectionState connState = connectionMapping.get(deviceId);

			// bring in the info
			specification.setConnectionType(connState.getConnectionType().getStringExpression());
			switch (connState.getConnectionType()) {
			case MOBILE:
				MobileMobileConnectionInfo info = (MobileMobileConnectionInfo) connState.getConnectionInfo();
				specification.setProtocol(info.getProtocol());
				specification.setOperator(info.getProvider());
				break;
			case WLAN:
				MobileWifiConnectionInfo wifiinfo = (MobileWifiConnectionInfo) connState.getConnectionInfo();
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

		// adjust repository model
		this.repositoryModelProvider.loadModel();
		final Repository repo = this.repositoryModelProvider.getModel();

		// adjusting
		String belongingName = "I" + hostname.replaceAll("\\.", "_");
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
		String preparedPath = path.split("\\.")[0];
		String operationName = record.getMethod().toLowerCase() + preparedPath.replaceAll("\\/", "_");

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

			PrimitiveDataType stringDatatype = RepositoryFactory.eINSTANCE.createPrimitiveDataType();
			stringDatatype.setType(PrimitiveTypeEnum.INT);
			signature.setReturnType__OperationSignature(stringDatatype);

			belonging.getSignatures__OperationInterface().add(signature);
		}

		this.repositoryModelProvider.save();

		// adjust system model
		this.systemModelProvider.loadModel();
		org.palladiosimulator.pcm.system.System systemModel = systemModelProvider.getModel();

		String asmContextName = belongingName + "_" + deviceId;
		AssemblyContext assemblyContext = SystemModelBuilder.createAssemblyContextsIfAbsent(systemModel,
				asmContextName);

		this.systemModelProvider.save();

		// adjust allocation model
		this.allocationModelProvider.loadModel();

		AllocationModelBuilder.addAllocationContextIfAbsent(this.allocationModelProvider.getModel(), serverContainer,
				assemblyContext);

		this.allocationModelProvider.save();
	}

	private void updateModel(String devId, MobileDeploymentRecord record) {
		this.resourceEnvironmentModelProvider.loadModel();
		final ResourceEnvironment resourceEnvironment = this.resourceEnvironmentModelProvider.getModel();

		this.systemModelProvider.loadModel();
		org.palladiosimulator.pcm.system.System systemModel = systemModelProvider.getModel();

		// create resource container for the device
		ResourceContainer container = this.buildIfNotPresent(resourceEnvironment, devId).get();

		// update system model
		Optional<Correspondent> correspondent = correspondence.getCorrespondent(record.getAppName());
		Opt.of(correspondent).ifPresent().apply(corr -> {
			String asmContextName = correspondent.get().getPcmEntityName() + "_" + devId;
			AssemblyContext assemblyContext = SystemModelBuilder.createAssemblyContextsIfAbsent(systemModel,
					asmContextName);

			this.allocationModelProvider.loadModel();
			AllocationModelBuilder.addAllocationContextIfAbsent(allocationModelProvider.getModel(), container,
					assemblyContext);
		}).elseApply(() -> LOG.warn("Couldn't find repository correspondent for app '" + record.getAppName() + "'."));

		// save model
		this.resourceEnvironmentModelProvider.save();
		this.systemModelProvider.save();
		this.allocationModelProvider.save();
	}

	private Optional<ResourceContainer> buildIfNotPresent(ResourceEnvironment env, String name) {
		Optional<ResourceContainer> deviceResourceContainer = ResourceEnvironmentModelBuilder
				.getResourceContainerByName(env, name);

		if (deviceResourceContainer.isPresent()) {
			return deviceResourceContainer;
		} else {
			ResourceEnvironmentModelBuilder.createResourceContainer(env, name);
			return ResourceEnvironmentModelBuilder.getResourceContainerByName(env, name);
		}
	}

}
