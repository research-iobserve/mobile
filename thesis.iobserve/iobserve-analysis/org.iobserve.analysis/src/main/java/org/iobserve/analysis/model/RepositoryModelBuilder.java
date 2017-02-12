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

import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Repository;
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
