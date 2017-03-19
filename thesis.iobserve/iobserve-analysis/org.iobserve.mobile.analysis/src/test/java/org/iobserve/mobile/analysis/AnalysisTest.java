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

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.iobserve.mobile.analysis.impl.CarrierAnalyzer;
import org.iobserve.mobile.analysis.impl.ProtocolAnalyzer;
import org.iobserve.mobile.analysis.impl.WifiAnalyzer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

import privacy_ext.CommunicationLinkPrivacy;
import privacy_ext.Privacy_extFactory;

/**
 * Test for the functionality of the PCM analysis.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class AnalysisTest {

	/**
	 * Creates a new test instance.
	 */
	public AnalysisTest() {
	}

	/**
	 * Setup method.
	 * 
	 * @throws Exception
	 *             if setup fails
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Performs a test analysis.
	 */
	@Test
	public void analysisTest() {
		final File whitelist = new File(AnalysisTest.class.getResource("/whitelist.txt").getFile());
		final PalladioInstance instance = new PalladioInstance();

		final ProtocolAnalyzer protocolAnalyzer = new ProtocolAnalyzer();
		final WifiAnalyzer wifiAnalyzer = new WifiAnalyzer(whitelist.getAbsolutePath(), whitelist.getAbsolutePath());
		final CarrierAnalyzer carrierAnalyzer = new CarrierAnalyzer(whitelist.getAbsolutePath());

		final ResourceEnvironment env = ResourceenvironmentFactory.eINSTANCE.createResourceEnvironment();

		final ResourceContainer r1 = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
		final ResourceContainer r2 = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();

		r1.setEntityName("R1");
		r2.setEntityName("R2");

		env.getResourceContainer_ResourceEnvironment().add(r1);
		env.getResourceContainer_ResourceEnvironment().add(r2);

		final LinkingResource link = ResourceenvironmentFactory.eINSTANCE.createLinkingResource();
		final CommunicationLinkPrivacy privacyInfo = Privacy_extFactory.eINSTANCE.createCommunicationLinkPrivacy();

		link.setCommunicationLinkResourceSpecifications_LinkingResource(privacyInfo);
		link.getConnectedResourceContainers_LinkingResource().add(r1);
		link.getConnectedResourceContainers_LinkingResource().add(r2);

		env.getLinkingResources__ResourceEnvironment().add(link);

		instance.environment = env;

		privacyInfo.setConnectionType("mobile");
		privacyInfo.setProtocol("edge");
		privacyInfo.setCarrier("o2");
		final int viols1 = protocolAnalyzer.analyze(instance).size();
		final int viols1a = carrierAnalyzer.analyze(instance).size();

		privacyInfo.setCarrier("tele");
		final int viols1b = carrierAnalyzer.analyze(instance).size();

		privacyInfo.setProtocol("3g");
		final int viols2 = protocolAnalyzer.analyze(instance).size();

		privacyInfo.setConnectionType("wifi");
		privacyInfo.setProtocol("wep");
		privacyInfo.setSsid("a");
		privacyInfo.setBssid("a");
		final int viols3 = protocolAnalyzer.analyze(instance).size();

		privacyInfo.setProtocol("wpa2");
		final int viols4 = protocolAnalyzer.analyze(instance).size();

		final int viols5 = wifiAnalyzer.analyze(instance).size();

		privacyInfo.setSsid("z");
		privacyInfo.setBssid("z");
		final int viols6 = wifiAnalyzer.analyze(instance).size();

		privacyInfo.setSsid("a");
		privacyInfo.setBssid("a");
		privacyInfo.setProtocol("none");
		final int viols7 = wifiAnalyzer.analyze(instance).size();

		// PROTOCOL
		assertTrue(viols1 == 1 && viols3 == 1 && viols1b == 1);
		assertTrue(viols2 == 0 && viols4 == 0 && viols1a == 0);

		// WIFI
		assertTrue(viols5 == 0);
		assertTrue(viols6 == 1 && viols7 == 1);

	}

}
