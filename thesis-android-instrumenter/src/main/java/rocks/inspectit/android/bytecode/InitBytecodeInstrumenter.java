package rocks.inspectit.android.bytecode;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

import rocks.inspectit.android.config.ConnectionInfo;
import rocks.inspectit.android.config.InstrumentationConfiguration;
import rocks.inspectit.android.config.InstrumentationPointConfiguration;

public class InitBytecodeInstrumenter implements IBytecodeInstrumenter {

	private ConnectionInfo connectionConfig;
	private Map<String, String[]> activityPointMapping;

	public InitBytecodeInstrumenter(InstrumentationConfiguration configuration) {
		this.connectionConfig = configuration.getXmlConfiguration().getConnectionInfo();
		this.activityPointMapping = new HashMap<String, String[]>();

		for (InstrumentationPointConfiguration point : configuration.getXmlConfiguration()
				.getInstrumentationRootConfiguration().getInstrPointConfigs()) {
			this.activityPointMapping.put(point.getType(), point.getValue().split("\\."));
		}
	}

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

	@Override
	public void onMethodExit(int opcode, String owner, String name, String desc, AdviceAdapter parent,
			MethodVisitor mv) {
	}

}
