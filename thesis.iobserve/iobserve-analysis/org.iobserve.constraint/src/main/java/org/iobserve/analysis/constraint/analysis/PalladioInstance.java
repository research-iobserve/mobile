package org.iobserve.analysis.constraint.analysis;

import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

public class PalladioInstance {
	public ResourceEnvironment environment;
	public AllocationContext allocation;
	public UsageModel usageModel;
	public System system;
}
