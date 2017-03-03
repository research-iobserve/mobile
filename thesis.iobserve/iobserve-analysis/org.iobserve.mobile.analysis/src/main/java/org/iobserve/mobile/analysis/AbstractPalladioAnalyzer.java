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
package org.iobserve.mobile.analysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;

import privacy_ext.CommunicationLinkPrivacy;

/**
 * Abstract class for analyzing a certain Palladio Component Model instance.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 * @param <T>
 *            Type of Palladio sub items which should be analyzed.
 */
public abstract class AbstractPalladioAnalyzer<T> {

	/**
	 * Creates a new analyzer instance.
	 */
	protected AbstractPalladioAnalyzer() {
	}

	/**
	 * Analyzes a Palladio component model instance.
	 * 
	 * @param palladio
	 *            instance of Palladio model.
	 * @return a list of constraint violations which can be empty if there is
	 *         none
	 */
	public List<ConstraintViolation> analyze(final PalladioInstance palladio) {
		final List<ConstraintViolation> o = new ArrayList<>();
		final List<T> l = this.extract(palladio);
		for (T i : l) {
			final ConstraintViolation v = this.isViolation(i);
			if (v != null) {
				o.add(v);
			}
		}
		return o;
	}

	/**
	 * Method which extracts the sub items to analyze from a Palladio instance.
	 * 
	 * @param instance
	 *            the Palladio instance
	 * @return list of sub items to analyze
	 */
	protected abstract List<T> extract(PalladioInstance instance);

	/**
	 * Checks whether a single item is a privacy violation or not.
	 * 
	 * @param value
	 *            the single item to check
	 * @return a constraint violation or null if the item is valid and
	 *         confidential
	 */
	protected abstract ConstraintViolation isViolation(T value);

	/**
	 * Gets a list of all communication links which have privacy information.
	 * This is a help function for the extract function of child classes.
	 * 
	 * @param instance
	 *            the palladio instance
	 * @return all communication links with privacy information
	 */
	protected List<CommunicationLinkPrivacy> getPrivacyLinks(final PalladioInstance instance) {
		final List<CommunicationLinkPrivacy> output = new ArrayList<>();
		final List<LinkingResource> lResources = instance.environment.getLinkingResources__ResourceEnvironment();
		for (LinkingResource link : lResources) {
			final CommunicationLinkResourceSpecification specification = link
					.getCommunicationLinkResourceSpecifications_LinkingResource();

			if (specification instanceof CommunicationLinkPrivacy) {
				output.add((CommunicationLinkPrivacy) specification);
			}
		}
		return output;
	}

	/**
	 * Loads a file to a set of its lines.
	 * 
	 * @param filename
	 *            the name of the file
	 * @return set of line values
	 * @throws IOException
	 *             if the file couldn't be read
	 */
	protected Set<String> provideFile(final String filename) throws IOException {
		final FileReader fileReader = new FileReader(filename);
		final BufferedReader bufferedReader = new BufferedReader(fileReader);
		final List<String> lines = new ArrayList<String>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		bufferedReader.close();
		return new HashSet<>(lines);
	}

}
