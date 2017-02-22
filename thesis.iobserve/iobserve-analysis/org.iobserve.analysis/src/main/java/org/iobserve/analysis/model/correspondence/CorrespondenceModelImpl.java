/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.model.correspondence;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.JAXB;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.iobserve.analysis.protocom.PcmCorrespondentMethod;
import org.iobserve.analysis.protocom.PcmEntity;
import org.iobserve.analysis.protocom.PcmEntityCorrespondent;
import org.iobserve.analysis.protocom.PcmMapping;
import org.iobserve.analysis.protocom.PcmOperationSignature;

/**
 * Implementation of {@link ICorrespondence}.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
class CorrespondenceModelImpl implements ICorrespondence {

	private static final Logger LOGGER = LogManager.getLogger(CorrespondenceModelImpl.class);

	/** cache for already mapped correspondences. */
	private final Map<String, Correspondent> cachedCorrespondents = new HashMap<>();

	/** raw mapping objects created during ProtoCom artifacts generation. */
	private final PcmMapping rawMapping;

	/** mapper for method signature to operation signature. */
	private final IOperationSignatureSelector opSigMapper;

	/** fast access map for class-signature to object. */
	private Map<String, PcmEntityCorrespondent> mapping;

	/** namespace of current palladio framework. */
	// private static final String PROTOCOM_BASE_PACKAGE_NAME =
	// "org.palladiosimulator.protocom";

	/**
	 * Builds the signature out of packagname.MethodName().
	 */
	private final IMethodSignatureBuilder mPackageNameClassNameMethodName = new IMethodSignatureBuilder() {

		@Override
		public String build(final PcmCorrespondentMethod method) {
			final String packageName = method.getParent().getPackageName();
			final String className = method.getParent().getUnitName();
			final String methodName = method.getName();
			return packageName + "." + className + "." + methodName + "()";
		}
	};

	/**
	 * Builds the signature like it would appear in the source code for instance
	 * void Get().
	 */
	private final IMethodSignatureBuilder mOnlyMethodName = new IMethodSignatureBuilder() {

		@Override
		public String build(final PcmCorrespondentMethod method) {
			final StringBuilder builder = new StringBuilder();

			// build method signature
			builder.append(method.getVisibilityModifier());
			builder.append(" ");
			builder.append(method.getReturnType());
			builder.append(" ");
			builder.append(method.getName());
			builder.append("(");
			builder.append(method.getParameters().replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
			// TODO I do not know how to handle multiple parameters..since
			// I did not see such after protocom build
			builder.append(")");
			// TODO <exception throws signature> is missing since this
			// is not retrievable from protocom-generation process so far.

			final String methodSig = builder.toString().trim();
			return methodSig;
		}
	};

	/**
	 * Create correspondence model.
	 *
	 * @param theMapping
	 *            mapping instance
	 * @param mapper
	 *            selector
	 */
	public CorrespondenceModelImpl(final PcmMapping theMapping, final IOperationSignatureSelector mapper) {
		this.rawMapping = theMapping;
		this.opSigMapper = mapper;
	}

	/**
	 * Create the correspondence model.
	 *
	 * @param mappingFile
	 *            input stream of mapping file
	 * @param mapper
	 *            selector
	 */
	public CorrespondenceModelImpl(final InputStream mappingFile, final IOperationSignatureSelector mapper) {
		this.rawMapping = JAXB.unmarshal(mappingFile, PcmMapping.class);
		this.opSigMapper = mapper;
		this.initMapping();
	}

	/**
	 * Init mapping.
	 * <ul>
	 * <li>Create Map for fast access</li>
	 * <li>Set parent references on {@link PcmMapping} objects</li>
	 * </ul>
	 */
	public void initMapping() {
		this.mapping = new HashMap<>();

		for (final PcmEntity nextPcmEntity : this.rawMapping.getEntities()) {
			nextPcmEntity.setParent(this.rawMapping);

			// set the parent reference
			for (final PcmOperationSignature nextOperation : nextPcmEntity.getOperationSigs()) {
				nextOperation.setParent(nextPcmEntity);
			}

			// set parent reference and convert the mapping
			for (final PcmEntityCorrespondent nextCorrespondent : nextPcmEntity.getCorrespondents()) {
				nextCorrespondent.setParent(nextPcmEntity);

				final String qualifiedName = (nextCorrespondent.getPackageName() + "."
						+ nextCorrespondent.getUnitName()).trim().replaceAll(" ", "");
				this.mapping.put(qualifiedName, nextCorrespondent);

				// set parent reference
				for (final PcmCorrespondentMethod nextCorresMethod : nextCorrespondent.getMethods()) {
					nextCorresMethod.setParent(nextCorrespondent);
				}
			}
		}
	}

	// ********************************************************************
	// * MAPPING
	// ********************************************************************

	@Override
	public Optional<Correspondent> getCorrespondent(final String classSig, final String operationSig) {
		// TODO debug print, remove later
		// System.out.print(String.format("Try to get correspondence for
		// classSig=%s,
		// operationSig=%s...",
		// classSig, operationSig));

		// assert parameters are not null
		if ((classSig == null) || (operationSig == null)) {
			return ICorrespondence.NULL_CORRESPONDENZ;
		}

		// create the request key for searching in the cache
		final String requestKey = classSig.trim().replaceAll(" ", "") + operationSig.trim().replaceAll(" ", "");

		// try to get the correspondent from the cache
		Correspondent correspondent = this.cachedCorrespondents.get(requestKey);

		// in case the correspondent is not available it has to be mapped
		if (correspondent == null) {
			final PcmEntityCorrespondent pcmEntityCorrespondent = this.getPcmEntityCorrespondent(classSig);
			if (pcmEntityCorrespondent == null) {
				CorrespondenceModelImpl.LOGGER.info("Mapping not available for class signature: " + classSig);

				return ICorrespondence.NULL_CORRESPONDENZ;
			}

			final PcmOperationSignature pcmOperationSignature = this.getPcmOperationSignature(pcmEntityCorrespondent,
					operationSig);
			if (pcmOperationSignature == null) {
				CorrespondenceModelImpl.LOGGER.info("Mapping not available for operation signature: " + operationSig);

				return ICorrespondence.NULL_CORRESPONDENZ;
			}

			// create correspondent object
			correspondent = CorrespondentFactory.newInstance(pcmEntityCorrespondent.getParent().getName(),
					pcmEntityCorrespondent.getParent().getId(), pcmOperationSignature.getName(),
					pcmOperationSignature.getId());

			// put into cache for next time
			this.cachedCorrespondents.put(requestKey, correspondent);
		}

		return Optional.of(correspondent);
	}

	@Override
	public Optional<Correspondent> getCorrespondent(final String classSig) {

		CorrespondenceModelImpl.LOGGER.debug(String.format("Try to get correspondence for classSig=%s ...", classSig));

		// assert parameters are not null
		if (classSig == null) {
			return ICorrespondence.NULL_CORRESPONDENZ;
		}

		// create the request key for searching in the cache
		final String requestKey = classSig.trim().replaceAll(" ", "");

		// try to get the correspondent from the cache
		Correspondent correspondent = this.cachedCorrespondents.get(requestKey);

		// in case the correspondent is not available it has to be mapped
		if (correspondent == null) {
			final PcmEntityCorrespondent pcmEntityCorrespondent = this.getPcmEntityCorrespondent(classSig);
			if (pcmEntityCorrespondent == null) {
				return ICorrespondence.NULL_CORRESPONDENZ; // or something else
			}

			// create correspondent object
			correspondent = CorrespondentFactory.newInstance(pcmEntityCorrespondent.getParent().getName(),
					pcmEntityCorrespondent.getParent().getId(), null, null);

			// put into cache for next time
			this.cachedCorrespondents.put(requestKey, correspondent);
		}

		return Optional.of(correspondent);
	}

	/**
	 * Get the {@link PcmEntity} based on the qualified class name.
	 *
	 * @param classSig
	 *            class signature
	 *
	 * @return null if not available
	 */
	private PcmEntityCorrespondent getPcmEntityCorrespondent(final String classSig) {
		final PcmEntityCorrespondent pcmEntityCorrespondent = this.mapping.get(classSig.trim().replaceAll(" ", ""));
		return pcmEntityCorrespondent;
	}

	/**
	 * Get the corresponding operation signature based the given operation
	 * signature.
	 *
	 * @param pcmEntityCorrespondent
	 *            pcm entity correspondence
	 * @param operationSig
	 *            operation signature
	 * @return pcm operation signature or null if operation signature not
	 *         available
	 */
	private PcmOperationSignature getPcmOperationSignature(final PcmEntityCorrespondent pcmEntityCorrespondent,
			final String operationSig) {
		PcmOperationSignature opSig = null;
		for (final PcmCorrespondentMethod nextCorresMethod : pcmEntityCorrespondent.getMethods()) {
			final String methodSig = this.mPackageNameClassNameMethodName.build(nextCorresMethod);

			if (operationSig.replaceAll(" ", "").equals(methodSig.replaceAll(" ", ""))) {
				opSig = this.mapOperationSignature(nextCorresMethod);
				break;
			}
		}

		return opSig;
	}

	/**
	 * Map the given method to the correspondent operation signature based on
	 * the name. The comparison is done by searching the operation signature
	 * name which is contained in the given method name.
	 *
	 * @param method
	 *            method
	 *
	 * @return null if not found
	 */
	private PcmOperationSignature mapOperationSignature(final PcmCorrespondentMethod method) {
		final PcmEntity pcmEntity = method.getParent().getParent();
		PcmOperationSignature opSig = null;
		for (final PcmOperationSignature nextOpSig : pcmEntity.getOperationSigs()) {
			if (this.opSigMapper.select(method, nextOpSig)) {
				opSig = nextOpSig;
				break;
			}
		}
		return opSig;
	}

	/**
	 * Test method to print all mappings.
	 */
	private void printAllMappings() {
		for (final String nextMappingKey : this.mapping.keySet()) {
			System.out.println(nextMappingKey);
			final PcmEntityCorrespondent correspondent = this.mapping.get(nextMappingKey);
			System.out.println(correspondent);
		}
	}

	/**
	 * String builder to build method signatures based on the given
	 * {@link PcmCorrespondentMethod} instance.
	 */
	private interface IMethodSignatureBuilder {
		/**
		 * @param method
		 *            method
		 * @return signature of the method based on the given
		 *         {@link PcmCorrespondentMethod}
		 */
		String build(PcmCorrespondentMethod method);
	}

}
