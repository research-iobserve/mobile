package org.iobserve.analysis.constraint.parser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.iobserve.analysis.constraint.analysis.PCMAnalyzer;
import org.iobserve.analysis.constraint.analysis.PalladioInstance;
import org.iobserve.analysis.constraint.model.ConstraintModel;
import org.junit.BeforeClass;
import org.junit.Test;
import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

public class ParsingTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void test() throws URISyntaxException, IOException {
		PalladioInstance instance = new PalladioInstance();
		// create a simple one
		instance.environment = ResourceenvironmentFactory.eINSTANCE.createResourceEnvironment();

		ResourceContainer rc1 = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
		rc1.setEntityName("rc1");

		ResourceContainer rc2 = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
		rc2.setEntityName("rc2");

		instance.environment.getResourceContainer_ResourceEnvironment().add(rc1);
		instance.environment.getResourceContainer_ResourceEnvironment().add(rc2);

		LinkingResource link1 = ResourceenvironmentFactory.eINSTANCE.createLinkingResource();
		link1.getConnectedResourceContainers_LinkingResource().add(rc1);
		link1.getConnectedResourceContainers_LinkingResource().add(rc2);

		CommunicationLinkResourceSpecification specification = ResourceenvironmentFactory.eINSTANCE
				.createCommunicationLinkResourceSpecification();
		specification.setProtocol("wpa");
		link1.setCommunicationLinkResourceSpecifications_LinkingResource(specification);

		instance.environment.getLinkingResources__ResourceEnvironment().add(link1);
		// __________________ //

		ConstraintModelParser parser = new ConstraintModelParser();

		URL defaultImage = ParsingTest.class.getResource("/constraints.cst");
		File imageFile = new File(defaultImage.toURI());

		ConstraintModel model = parser.parseModel(imageFile);
		PCMAnalyzer analyzer = new PCMAnalyzer(instance);
		analyzer.analyze(model);
	}

}
