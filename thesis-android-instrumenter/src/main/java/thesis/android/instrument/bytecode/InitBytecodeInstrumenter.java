package thesis.android.instrument.bytecode;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

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

	/**
	 * Information about the server which persists the monitoring data. -> This
	 * has to be set at instrumentation time!
	 */
	private ConnectionInfo connectionConfig;

	/**
	 * Mapping between methods of the Activity and methods which should be
	 * called in the agent.
	 */
	private Map<String, String[]> activityPointMapping;

	/**
	 * Creates a instance of the InitBytecodeInstrumenter.
	 * 
	 * @param configuration
	 *            configuration which specifies the mapping between activity
	 *            methods and agent methods.
	 */
	public InitBytecodeInstrumenter(InstrumentationConfiguration configuration) {
		this.connectionConfig = configuration.getXmlConfiguration().getConnectionInfo();
		this.activityPointMapping = new HashMap<String, String[]>();

		for (InstrumentationPointConfiguration point : configuration.getXmlConfiguration()
				.getInstrumentationRootConfiguration().getInstrPointConfigs()) {
			this.activityPointMapping.put(point.getType(), point.getValue().split("\\."));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMethodEnter(String owner, String name, String desc, AdviceAdapter parent, MethodVisitor mv) {
		if (name.equals("onCreate")) {
			String[] belAgentPoint = activityPointMapping.get("onCreate");

			// INSERT CONFIG CALLS
			mv.visitLdcInsn(connectionConfig.getBeaconUrl());
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "rocks/inspectit/android/ExternalConfiguration", "setBeaconUrl",
					"(Ljava/lang/String;)V", false);
			mv.visitLdcInsn(connectionConfig.getHelloUrl());
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "rocks/inspectit/android/ExternalConfiguration", "setHelloUrl",
					"(Ljava/lang/String;)V", false);

			// INIT AGENT
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, belAgentPoint[0], belAgentPoint[1], "(Landroid/app/Activity;)V",
					false);
		} else if (name.equals("onDestroy")) {
			String[] belAgentPoint = activityPointMapping.get("onDestroy");

			mv.visitMethodInsn(Opcodes.INVOKESTATIC, belAgentPoint[0], belAgentPoint[1], "()V", false);
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
