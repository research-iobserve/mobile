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

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * Data class for holding all parts of a single Palladio Component Model.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class PalladioInstance {

	/**
	 * Creates a new instance.
	 */
	public PalladioInstance() {
	}

	/**
	 * Allocation model of the PCM.
	 */
	public Allocation allocation;

	/**
	 * Resource environment of the PCM.
	 */
	public ResourceEnvironment environment;

	/**
	 * Repository model of the PCM.
	 */
	public Repository repository;

	/**
	 * System model of the PCM.
	 */
	public System system;

	/**
	 * Usage model of the PCM.
	 */
	public UsageModel usageModel;
}
