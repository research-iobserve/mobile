package thesis.android.instrument.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import thesis.android.instrument.bytecode.InstrumentationPoint;
import thesis.android.instrument.bytecode.NetworkBytecodeInstrumenter;

public class InstrumentationConfiguration {

	// Fully qualified name of kieker sensor class
	private static final String KIEKER_SENSOR = "rocks.inspectit.android.sensor.KiekerSensor";

	private List<InstrumentationPoint> instrumentationPoints;
	private Map<InstrumentationPoint, String> sensorMapping;

	private NetworkBytecodeInstrumenter networkBytecodeInstrumenter;

	private String applicationPackage;

	// main configuration refactored
	private RootConfiguration xmlConfiguration;

	public InstrumentationConfiguration() {
		this.instrumentationPoints = new ArrayList<InstrumentationPoint>();
		this.sensorMapping = new HashMap<InstrumentationPoint, String>();
		this.networkBytecodeInstrumenter = new NetworkBytecodeInstrumenter();
		this.applicationPackage = null;
	}

	public NetworkBytecodeInstrumenter getNetworkBytecodeInstrumenter() {
		return networkBytecodeInstrumenter;
	}

	public boolean matchesHttpInstrumentationPoint(String owner, String desc) {
		return owner.equalsIgnoreCase("java/net/HttpURLConnection") || owner.equalsIgnoreCase("java/net/URL")
				|| descReturnType(desc).equalsIgnoreCase("Ljava/net/HttpURLConnection;");
	}

	public Set<InstrumentationPoint> matchesInstrumentationPoint(String className, String superClass,
			String[] interfaces) {
		Set<InstrumentationPoint> ret = new HashSet<InstrumentationPoint>();

		for (InstrumentationPoint instrPoint : instrumentationPoints) {
			if (instrPoint.isSuper() && superClass.equalsIgnoreCase(instrPoint.getClassName())) {
				ret.add(instrPoint);
			} else if (instrPoint.isInterface()) {
				for (String inf : interfaces) {
					if (inf.equalsIgnoreCase(instrPoint.getClassName()))
						ret.add(instrPoint);
				}
			} else {
				if (className.equalsIgnoreCase(instrPoint.getClassName())) {
					ret.add(instrPoint);
				}
			}
		}

		return ret;
	}

	public void parseConfigFile(File config) throws JAXBException {
		if (config.exists()) {
			JAXBContext jc = JAXBContext.newInstance(RootConfiguration.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			RootConfiguration sc = (RootConfiguration) unmarshaller.unmarshal(config);
			this.xmlConfiguration = sc;

			// PARSE IN THIS FILE
			for (SensorConfig sensor : sc.getInstrumentationRootConfiguration().getSensors()) {
				InstrumentationPoint instrPoint = new InstrumentationPoint(sensor.getParent(), sensor.getMethod());
				instrPoint.setDescription(sensor.getSignature());
				instrPoint.setSuper(true);

				this.instrumentationPoints.add(instrPoint);
				this.sensorMapping.put(instrPoint, sensor.getHandler());
			}
		}
	}

	public String getSensor(InstrumentationPoint instrPt) {
		return sensorMapping.containsKey(instrPt) ? sensorMapping.get(instrPt) : null;
	}

	private String descReturnType(String desc) {
		String[] sp = desc.split("\\)");
		return sp[1];
	}

	public RootConfiguration getXmlConfiguration() {
		return xmlConfiguration;
	}

	public String getApplicationPackage() {
		return applicationPackage;
	}

	public void setApplicationPackage(String applicationPackage) {
		this.applicationPackage = applicationPackage.replaceAll("\\.", "/");
	}

	public String getKiekerSensor() {
		return KIEKER_SENSOR;
	}

}
