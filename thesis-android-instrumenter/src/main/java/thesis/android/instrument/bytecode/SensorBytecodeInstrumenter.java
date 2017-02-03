package thesis.android.instrument.bytecode;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import rocks.inspectit.android.AndroidAgent;

public class SensorBytecodeInstrumenter implements IBytecodeInstrumenter {

	private static final Logger LOG = LogManager.getLogger(SensorBytecodeInstrumenter.class);

	private static final Type ANDROIDAGENT_TYPE = Type.getType(AndroidAgent.class);

	private String sensor;
	private int index;

	// METHOD CORRESPONDENTS
	private Method enterBodyMethod;
	private Type enterBodyType;

	private Method exitBodyMethod;
	private Type exitBodyType;

	private Method exitBodyErrorMethod;
	private Type exitBodyErrorType;

	public SensorBytecodeInstrumenter(String sensClass) {
		this.sensor = sensClass;

		try {
			enterBodyMethod = AndroidAgent.class.getMethod("enterBody", String.class, String.class, String.class);
			enterBodyType = Type.getType(enterBodyMethod);

			exitBodyMethod = AndroidAgent.class.getMethod("exitBody", long.class);
			exitBodyType = Type.getType(exitBodyMethod);

			exitBodyErrorMethod = AndroidAgent.class.getMethod("exitErrorBody", Throwable.class, long.class);
			exitBodyErrorType = Type.getType(exitBodyErrorMethod);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			LOG.warn("Couldn't find all methods of the agent which are needed in the instrumentation.");
		}
	}

	@Override
	public void onMethodEnter(String owner, String name, String desc, AdviceAdapter parent, MethodVisitor mv) {
		// add sensor call
		mv.visitLdcInsn(sensor);
		mv.visitLdcInsn(name + desc);
		mv.visitLdcInsn(owner);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(), enterBodyMethod.getName(),
				enterBodyType.getDescriptor(), false);
		index = parent.newLocal(Type.LONG_TYPE);
		mv.visitVarInsn(Opcodes.LSTORE, index);
	}

	@Override
	public void onMethodExit(int opcode, String owner, String name, String desc, AdviceAdapter parent,
			MethodVisitor mv) {
		if (opcode == Opcodes.ATHROW) {
			mv.visitInsn(Opcodes.DUP);
			mv.visitVarInsn(Opcodes.LLOAD, index);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(), exitBodyErrorMethod.getName(),
					exitBodyErrorType.getDescriptor(), false);
		} else {
			mv.visitVarInsn(Opcodes.LLOAD, index);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(), exitBodyMethod.getName(),
					exitBodyType.getDescriptor(), false);
		}
	}
}
