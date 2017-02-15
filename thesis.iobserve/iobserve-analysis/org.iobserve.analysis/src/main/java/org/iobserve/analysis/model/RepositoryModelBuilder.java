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

import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RepositoryFactory;

/**
 * Help functions to interact with an existing PCM repository model.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public final class RepositoryModelBuilder {

	/**
	 * Private constructor.
	 */
	private RepositoryModelBuilder() {
	}

	/**
	 * Creates a provided role for a specified component and a given interface
	 * if it doesn't exist.
	 * 
	 * @param repo
	 *            the repository model
	 * @param comp
	 *            the component
	 * @param iface
	 *            the operation interface
	 */
	public static void createProvidesRoleIfAbsent(final Repository repo, final BasicComponent comp,
			OperationInterface iface) {

		// CHECK IF PROVIDED ROLE EXISTS
		for (ProvidedRole pRole : comp.getProvidedRoles_InterfaceProvidingEntity()) {
			if (pRole instanceof OperationProvidedRole) {
				OperationProvidedRole opProvRole = (OperationProvidedRole) pRole;

				if (opProvRole.getProvidedInterface__OperationProvidedRole().equals(iface)) {
					return;
				}
			}
		}

		OperationProvidedRole providedRole = RepositoryFactory.eINSTANCE.createOperationProvidedRole();
		providedRole.setEntityName(comp.getEntityName() + "_" + iface.getEntityName());
		providedRole.setProvidedInterface__OperationProvidedRole(iface);
		comp.getProvidedRoles_InterfaceProvidingEntity().add(providedRole);
	}

	/**
	 * Creates a basic component in the repository model if it doesn't exist.
	 * 
	 * @param repo
	 *            the repository model
	 * @param name
	 *            the name of the basic component
	 * @return the created or retrieved basic component
	 */
	public static BasicComponent createBasicComponentIfAbsent(final Repository repo, final String name) {
		BasicComponent bel = null;
		for (RepositoryComponent comp : repo.getComponents__Repository()) {
			if (comp.getEntityName().equals(name) && comp instanceof BasicComponent) {
				bel = (BasicComponent) comp;
				break;
			}
		}

		if (bel == null) {
			bel = RepositoryFactory.eINSTANCE.createBasicComponent();
			bel.setEntityName(name);

			repo.getComponents__Repository().add(bel);
		}

		return bel;
	}

	/**
	 * Creates an operation interface with a specified name if it is absent.
	 * 
	 * @param repo
	 *            the repository model
	 * @param name
	 *            the name of the operation interface
	 * @return the created or retrieved operation interface
	 */
	public static OperationInterface createOperationInterfaceIfAbsent(final Repository repo, final String name) {
		// adjusting
		OperationInterface belonging = null;
		for (Interface iface : repo.getInterfaces__Repository()) {
			if (iface.getEntityName().equals(name) && iface instanceof OperationInterface) {
				belonging = (OperationInterface) iface;
				break;
			}
		}

		if (belonging == null) {
			// create
			belonging = RepositoryFactory.eINSTANCE.createOperationInterface();
			belonging.setEntityName(name);

			repo.getInterfaces__Repository().add(belonging);
		}

		return belonging;
	}

	/**
	 * Creates an operation signature with a specified name if it doesn't exist.
	 * 
	 * @param iface
	 *            the operation interface the signature belongs to
	 * @param operationName
	 *            the name of the operation
	 * @return the created operation signature
	 */
	public static OperationSignature createOperationSignatureIfAbsent(final OperationInterface iface,
			final String operationName) {
		OperationSignature signature = null;
		for (OperationSignature sig : iface.getSignatures__OperationInterface()) {
			if (sig.getEntityName().equals(operationName)) {
				signature = sig;
				break;
			}
		}

		if (signature == null) {
			// create
			signature = RepositoryFactory.eINSTANCE.createOperationSignature();
			signature.setEntityName(operationName);

			iface.getSignatures__OperationInterface().add(signature);
		}

		return signature;
	}

}
