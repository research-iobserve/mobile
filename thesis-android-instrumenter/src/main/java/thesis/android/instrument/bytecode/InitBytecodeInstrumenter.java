package thesis.android.instrument.bytecode;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import rocks.inspectit.android.AndroidAgent;
import rocks.inspectit.android.ExternalConfiguration;
import thesis.android.instrument.config.ConnectionInfo;
import thesis.android.instrument.config.InstrumentationConfiguration;
import thesis.android.instrument.config.InstrumentationPointConfiguration;

/**
 * Bytecode instrumenter which is responsible for instrumenting Android Activity
 * classes.
 * 
 * @author David Monschein
 *
 */
public class InitBytecodeInstrumenter implements IBytecodeInstrumenter {

	private static final Logger LOG = LogManager.getLogger(InitBytecodeInstrumenter.class);

	private static final Type EXTCONFIGURATION_TYPE = Type.getType(ExternalConfiguration.class);
	private static final Type ANDROIDAGENT_TYPE = Type.getType(AndroidAgent.class);

	/**
	 * Information about the server which persists the monitoring data. -> This
	 * has to be set at instrumentation time!
	 */
	private ConnectionInfo connectionConfig;

	/**
	 * Mapping between methods of the Activity and methods which should be
	 * called in the agent.
	 */
	private Map<String, String> activityPointMapping;

	// CORRESPONDENT METHODS
	private Method setBeaconUrlMethod;
	private Type setBeaconUrlType;

	private Method setHelloUrlMethod;
	private Type setHelloUrlType;

	public InitBytecodeInstrumenter() {
		try {
			setBeaconUrlMethod = ExternalConfiguration.class.getMethod("setBeaconUrl", String.class);
			setBeaconUrlType = Type.getType(setBeaconUrlMethod);

			setHelloUrlMethod = ExternalConfiguration.class.getMethod("setHelloUrl", String.class);
			setHelloUrlType = Type.getType(setHelloUrlMethod);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			LOG.warn("Couldn't find all corresponding methods!");
		}
	}

	/**
	 * Creates a instance of the InitBytecodeInstrumenter.
	 * 
	 * @param configuration
	 *            configuration which specifies the mapping between activity
	 *            methods and agent methods.
	 */
	public InitBytecodeInstrumenter(InstrumentationConfiguration configuration) {
		this();
		this.connectionConfig = configuration.getXmlConfiguration().getConnectionInfo();
		this.activityPointMapping = new HashMap<String, String>();

		for (InstrumentationPointConfiguration point : configuration.getXmlConfiguration()
				.getInstrumentationRootConfiguration().getInstrPointConfigs()) {
			this.activityPointMapping.put(point.getType(), point.getValue());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMethodEnter(String owner, String name, String desc, AdviceAdapter parent, MethodVisitor mv) {
		if (name.equals("onCreate")) {
			String belAgentPoint = activityPointMapping.get("onCreate");

			// INSERT CONFIG CALLS
			mv.visitLdcInsn(connectionConfig.getBeaconUrl());
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, EXTCONFIGURATION_TYPE.getInternalName(),
					setBeaconUrlMethod.getName(), setBeaconUrlType.getDescriptor(), false);

			mv.visitLdcInsn(connectionConfig.getHelloUrl());
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, EXTCONFIGURATION_TYPE.getInternalName(),
					setHelloUrlMethod.getName(), setHelloUrlType.getDescriptor(), false);

			// INIT AGENT
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(), belAgentPoint,
					"(Landroid/app/Activity;)V", false);
		} else if (name.equals("onDestroy")) {
			String belAgentPoint = activityPointMapping.get("onDestroy");

			mv.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(), belAgentPoint, "()V", false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMethodExit(int opcode, String owner, String name, String desc, AdviceAdapter parent,
			MethodVisitor mv) {
	}

}
