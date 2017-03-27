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
package org.iobserve.analysis.model;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iobserve.analysis.filter.RecordSwitch;
import org.iobserve.analysis.mobile.MobileConnectionState;
import org.iobserve.analysis.mobile.MobileMobileConnectionInfo;
import org.iobserve.analysis.mobile.MobileWifiConnectionInfo;
import org.iobserve.analysis.utils.Opt;
import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

import privacy_ext.CommunicationLinkPrivacy;
import privacy_ext.Privacy_extFactory;

/**
 * Model builder for resource environment models.
 *
 * @author Robert Heinrich
 *
 */
public final class ResourceEnvironmentModelBuilder {
	
	/** logger. */
	private static final Log LOG = LogFactory.getLog(RecordSwitch.class);

    /**
     * Constructor for resource models with a start model.
     *
     * @param modelToStartWith
     *            model provider.
     */
    private ResourceEnvironmentModelBuilder() {
    }

    /**
     * Create a {@link ResourceContainer} with the given name, without checking if it already
     * exists. Use {@link #createResourceContainerIfAbsent(String)} instead if you wont create the
     * container if it is already available.
     *
     * @param model
     *            resource environment model
     * @param name
     *            name of the new container
     * @return builder
     */
    public static ResourceContainer createResourceContainer(final ResourceEnvironment model, final String name) {
        final ResourceContainer resContainer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        resContainer.setEntityName(name);
        model.getResourceContainer_ResourceEnvironment().add(resContainer);
        return resContainer;
    }

    /**
     * Creates a link between the given two container.
     *
     * @param model
     *            resource environment model
     * @param res1
     *            first container
     * @param res2
     *            second container
     * @return link instance, already added to the model
     */
    public static LinkingResource connectResourceContainer(final ResourceEnvironment model,
            final ResourceContainer res1, final ResourceContainer res2) {
        final LinkingResource link = ResourceenvironmentFactory.eINSTANCE.createLinkingResource();
        link.getConnectedResourceContainers_LinkingResource().add(res1);
        link.getConnectedResourceContainers_LinkingResource().add(res2);
        model.getLinkingResources__ResourceEnvironment().add(link);

        return link;
    }
    
    /**
	 * Creates a linking resource with specified connection information.
	 * 
	 * @param model
	 *            the resource environment model
	 * @param device
	 *            the resource container for the device
	 * @param server
	 *            the resource container for the server
	 * @param connexion
	 *            the connection information
	 */
	public static void connectResourceContainerExtended(final ResourceEnvironment model, final ResourceContainer device,
			final ResourceContainer server, MobileConnectionState connexion) {

		if (privacyLinkExists(model, device, server, connexion)) {
			return;
		}

		Opt.of(device).ifPresent().apply(deviceContainer -> {
			final LinkingResource connectionLink = ResourceEnvironmentModelBuilder.connectResourceContainer(model,
					deviceContainer, server);
			connectionLink.setEntityName(deviceContainer.getEntityName() + "_" + server.getEntityName());

			final CommunicationLinkPrivacy privacyLink = Privacy_extFactory.eINSTANCE.createCommunicationLinkPrivacy();

			// bring in the info
			privacyLink.setConnectionType(connexion.getConnectionType().getStringExpression());
			switch (connexion.getConnectionType()) {
			case MOBILE:
				final MobileMobileConnectionInfo info = (MobileMobileConnectionInfo) connexion.getConnectionInfo();
				privacyLink.setProtocol(info.getProtocol());
				privacyLink.setCarrier(info.getCarrier());
				break;
			case WLAN:
				final MobileWifiConnectionInfo wifiinfo = (MobileWifiConnectionInfo) connexion.getConnectionInfo();
				privacyLink.setSsid(wifiinfo.getSsid());
				privacyLink.setBssid(wifiinfo.getBssid());
				privacyLink.setProtocol(wifiinfo.getProtocol());
				break;
			default:
				LOG.warn("Found an request without an active connection.");
			}

			// set it
			connectionLink.setCommunicationLinkResourceSpecifications_LinkingResource(privacyLink);
		}).elseApply(() -> LOG.warn("Container for device doesn't exist."));
	}

    /**
     * Get the {@link ResourceContainer} by its {@link ResourceContainer#getEntityName()}.
     *
     * @param resourceEnvironment
     *            the resource environment model
     * @param name
     *            name
     * @return resource container instance or null if no resource container available with the given
     *         name.
     */
    public static Optional<ResourceContainer> getResourceContainerByName(final ResourceEnvironment resourceEnvironment,
            final String name) {
        return resourceEnvironment.getResourceContainer_ResourceEnvironment().stream()
                .filter(container -> container.getEntityName().equals(name)).findFirst();
    }
    
    /**
	 * Checks whether a connection with specified info already exists.
	 * 
	 * @param model
	 *            the resource environment model
	 * @param device
	 *            the resource container for the device
	 * @param server
	 *            the resource container for the server
	 * @param connexion
	 *            the connection information
	 */
	private static boolean privacyLinkExists(final ResourceEnvironment model, final ResourceContainer device,
			final ResourceContainer server, MobileConnectionState connexion) {

		for (LinkingResource lr : model.getLinkingResources__ResourceEnvironment()) {
			boolean b1 = false, b2 = false;
			for (ResourceContainer container : lr.getConnectedResourceContainers_LinkingResource()) {
				if (container.equals(device)) {
					b1 = true;
				}
				if (container.equals(server)) {
					b2 = true;
				}
			}

			if (b1 && b2) {
				CommunicationLinkResourceSpecification specification = lr
						.getCommunicationLinkResourceSpecifications_LinkingResource();
				if (specification != null && specification instanceof CommunicationLinkPrivacy) {
					MobileConnectionState state = new MobileConnectionState((CommunicationLinkPrivacy) specification);
					if (state.equals(connexion)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
