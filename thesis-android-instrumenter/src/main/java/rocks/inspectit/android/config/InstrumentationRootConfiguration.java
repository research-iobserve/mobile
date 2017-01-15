package rocks.inspectit.android.config;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class InstrumentationRootConfiguration {

	@XmlElementWrapper(name = "sensors")
	@XmlElement(name = "sensor")
	private List<SensorConfig> sensors;

	@XmlElementWrapper(name = "mapping")
	@XmlElement(name = "point")
	private List<InstrumentationPointConfiguration> instrPointConfigs;

	public List<SensorConfig> getSensors() {
		return sensors;
	}

	public List<InstrumentationPointConfiguration> getInstrPointConfigs() {
		return instrPointConfigs;
	}

}
