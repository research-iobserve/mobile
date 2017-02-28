package org.iobserve.mobile.instrument.config;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.iobserve.mobile.agent.sensor.KiekerSensor;
import org.iobserve.mobile.instrument.bytecode.NetworkBytecodeInstrumenter;
import org.iobserve.mobile.instrument.config.xml.RootConfigurationXml;
import org.objectweb.asm.Type;

import android.webkit.WebView;

/**
 * Configuration class for the instrumentation process of Android applications.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class InstrumentationConfiguration {
	/** Type for {@link KiekerSensor}. */
	private static final String KIEKER_SENSOR = Type.getType(KiekerSensor.class).getClassName();
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
	public boolean matchesHttpInstrumentationPoint(String owner, String desc) {
		return owner.equalsIgnoreCase(HTTPURLCONNECTION_TYPE.getInternalName())
				|| owner.equalsIgnoreCase(URL_TYPE.getInternalName())
				|| descReturnType(desc).equalsIgnoreCase(HTTPURLCONNECTION_TYPE.getDescriptor())
				|| owner.equalsIgnoreCase(WEBVIEW_TYPE.getInternalName());
	}

	/**
	 * Parses a XML configuration file.
	 * 
	 * @param config
	 *            configuration file
	 * @throws JAXBException
	 *             if the configuration couldn't be parsed
	 */
	public void parseConfigFile(File config) throws JAXBException {
		if (config.exists()) {
			JAXBContext jc = JAXBContext.newInstance(RootConfigurationXml.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			RootConfigurationXml sc = (RootConfigurationXml) unmarshaller.unmarshal(config);
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
	private String descReturnType(String desc) {
		String[] sp = desc.split("\\)");
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
	 * Sets the application package name
	 * 
	 * @param applicationPackage
	 *            application package name
	 */
	public void setApplicationPackage(String applicationPackage) {
		this.applicationPackage = applicationPackage.replaceAll("\\.", "/");
	}

	/**
	 * Gets the {@link KiekerSensor}.
	 * 
	 * @return the kieker sensor
	 */
	public String getKiekerSensor() {
		return KIEKER_SENSOR;
	}

}
