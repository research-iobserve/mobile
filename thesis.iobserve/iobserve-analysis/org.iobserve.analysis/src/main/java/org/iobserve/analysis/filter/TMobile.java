package org.iobserve.analysis.filter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.iobserve.analysis.mobile.MobileConnectionState;
import org.iobserve.analysis.model.AllocationModelBuilder;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelBuilder;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelBuilder;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.utils.Opt;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

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
	/** mapping of device to a connection state */
	private final Map<String, MobileConnectionState> connectionMapping;
	/** correspondence mapping */
	private final ICorrespondence correspondence;

	public TMobile(final ICorrespondence correspondence, final AllocationModelProvider allocationModelProvider,
			final SystemModelProvider systemModelProvider,
			final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider) {
		this.correspondence = correspondence;
		this.allocationModelProvider = allocationModelProvider;
		this.systemModelProvider = systemModelProvider;
		this.resourceEnvironmentModelProvider = resourceEnvironmentModelProvider;
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
		try {
			hostname = new URI(record.getUrl()).getHost();
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

		Opt.of(deviceResourceContainer).ifPresent().apply(deviceContainer -> {
			LinkingResource connectionLink = ResourceEnvironmentModelBuilder
					.connectResourceContainer(resourceEnvironment, deviceContainer, serverContainer);
			// TODO bring in the infos of the connection to this link
			MobileConnectionState connState = connectionMapping.get(deviceId);
		}).elseApply(() -> LOG.warn("Container for device with id '" + deviceId + "' doesn't exist."));

		// save model
		this.resourceEnvironmentModelProvider.save();
	}

	private void updateModel(String devId, MobileDeploymentRecord record) {
		final ResourceEnvironment resourceEnvironment = this.resourceEnvironmentModelProvider.getModel();

		// create resource container for the device
		ResourceContainer container = this.buildIfNotPresent(resourceEnvironment, devId).get();

		// update system model
		// TODO this should be built with correspondence
		String asmContextName = record.getAppName() + "_" + devId;
		AssemblyContext assemblyContext = SystemModelBuilder
				.getAssemblyContextByName(this.systemModelProvider.getModel(), asmContextName).get();

		if (assemblyContext == null) {
			this.systemModelProvider.loadModel();
			SystemModelBuilder.createAssemblyContextsIfAbsent(this.systemModelProvider.getModel(), asmContextName);
			this.systemModelProvider.save();
			assemblyContext = SystemModelBuilder
					.getAssemblyContextByName(this.systemModelProvider.getModel(), asmContextName).get();
		}

		// update allocation model
		this.allocationModelProvider.loadModel();
		AllocationModelBuilder.addAllocationContextIfAbsent(this.allocationModelProvider.getModel(), container,
				assemblyContext);
		this.allocationModelProvider.save();

		// save model
		this.resourceEnvironmentModelProvider.save();
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
