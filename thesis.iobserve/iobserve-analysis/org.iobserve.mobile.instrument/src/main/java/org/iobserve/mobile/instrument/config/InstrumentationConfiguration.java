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
package org.iobserve.mobile.instrument.config;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.iobserve.mobile.agent.sensor.TraceSensor;
import org.iobserve.mobile.instrument.bytecode.NetworkBytecodeInstrumenter;
import org.iobserve.mobile.instrument.config.xml.RootConfigurationXml;
import org.objectweb.asm.Type;

import android.webkit.WebView;

/**
 * Configuration class for the instrumentation process of Android applications.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class InstrumentationConfiguration {

	/**
	 * Pattern which is replaced with the application package for the
	 * instrumentation rules.
	 */
	private static final String PATTERN_APPLICATION_PACKAGE = "{application}";

	/** Type for {@link TraceSensor}. */
	private static final String KIEKER_SENSOR = Type.getType(TraceSensor.class).getClassName();
	// HTTP POINTS
	/** Type for {@link HttpURLConnection}. */
	private static final Type HTTPURLCONNECTION_TYPE = Type.getType(HttpURLConnection.class);

	/** Type for {@link URL}. */
	private static final Type URL_TYPE = Type.getType(URL.class);

	/** Type for {@link WebView}. */
	private static final Type WEBVIEW_TYPE = Type.getType(android.webkit.WebView.class);

	/** Link to the network bytecode instrumentation manager. */
	private NetworkBytecodeInstrumenter networkBytecodeInstrumenter;

	/** Name of the main package from the application. */
	private String applicationPackage;

	/**
	 * Configuration as XML tree.
	 */
	private RootConfigurationXml xmlConfiguration;

	/**
	 * Creates a new instrumentation configuration with default values.
	 */
	public InstrumentationConfiguration() {
		this.networkBytecodeInstrumenter = new NetworkBytecodeInstrumenter();
		this.applicationPackage = null;
	}

	/**
	 * Get the {@link NetworkBytecodeInstrumenter}.
	 * 
	 * @return the network bytecode instrumenter
	 */
	public NetworkBytecodeInstrumenter getNetworkBytecodeInstrumenter() {
		return networkBytecodeInstrumenter;
	}

	/**
	 * Determines whether a method call causes a network activity.
	 * 
	 * @param owner
	 *            class which contains the method
	 * @param desc
	 *            description of the method
	 * @return true if it is a network call - false otherwise
	 */
	public boolean matchesHttpInstrumentationPoint(final String owner, final String desc) {
		return owner.equalsIgnoreCase(HTTPURLCONNECTION_TYPE.getInternalName())
				|| owner.equalsIgnoreCase(URL_TYPE.getInternalName())
				|| descReturnType(desc).equalsIgnoreCase(HTTPURLCONNECTION_TYPE.getDescriptor())
				|| owner.equalsIgnoreCase(WEBVIEW_TYPE.getInternalName());
	}

	/**
	 * Checks whether within a single class traces should be collected or not.
	 * 
	 * @param clazzFull
	 *            the name of the class
	 * @return if the traces should get collected
	 */
	public boolean isTraceRelevantClass(final String clazzFull) {
		for (String rule : xmlConfiguration.getTraceCollectionList().getPackages()) {
			String ruleAdjust = rule;
			if (rule.contains(PATTERN_APPLICATION_PACKAGE)) {
				ruleAdjust = rule.replace(PATTERN_APPLICATION_PACKAGE, getApplicationPackage());
			}

			// CHECK IF RULE MATCHES
			final String[] packageSplit1 = clazzFull.replaceAll("/", ".").split("\\.");
			final String[] packageSplit2 = ruleAdjust.replaceAll("/", ".").split("\\.");

			boolean match = true;
			for (int i = 0; i < packageSplit1.length; i++) {
				if (packageSplit2[i].equals("**") && match) {
					return true;
				} else if (!packageSplit2[i].equals("*")) {
					if (!packageSplit1[i].equals(packageSplit2[i])) {
						match = false;
						break;
					}
				}
			}

			if (match) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Parses a XML configuration file.
	 * 
	 * @param config
	 *            configuration file
	 * @throws JAXBException
	 *             if the configuration couldn't be parsed
	 */
	public void parseConfigFile(final File config) throws JAXBException {
		if (config.exists()) {
			final JAXBContext jc = JAXBContext.newInstance(RootConfigurationXml.class);
			final Unmarshaller unmarshaller = jc.createUnmarshaller();

			final RootConfigurationXml sc = (RootConfigurationXml) unmarshaller.unmarshal(config);
			this.xmlConfiguration = sc;
		}
	}

	/**
	 * Extracts the return type from a description.
	 * 
	 * @param desc
	 *            method description
	 * @return extracted return type
	 */
	private String descReturnType(final String desc) {
		final String[] sp = desc.split("\\)");
		return sp[1];
	}

	/**
	 * Gets the XML configuration tree.
	 * 
	 * @return XML configuration
	 */
	public RootConfigurationXml getXmlConfiguration() {
		return xmlConfiguration;
	}

	/**
	 * Gets the application package name.
	 * 
	 * @return application package name
	 */
	public String getApplicationPackage() {
		return applicationPackage;
	}

	/**
	 * Sets the application package name.
	 * 
	 * @param applicationPackage
	 *            application package name
	 */
	public void setApplicationPackage(final String applicationPackage) {
		this.applicationPackage = applicationPackage.replaceAll("\\.", "/");
	}

	/**
	 * Gets the {@link TraceSensor}.
	 * 
	 * @return the kieker sensor
	 */
	public String getKiekerSensor() {
		return KIEKER_SENSOR;
	}

}
