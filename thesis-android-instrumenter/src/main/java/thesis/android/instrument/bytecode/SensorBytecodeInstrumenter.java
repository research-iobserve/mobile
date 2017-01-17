package thesis.android.instrument.bytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class SensorBytecodeInstrumenter implements IBytecodeInstrumenter {
	private String sensor;
	private int index;

	public SensorBytecodeInstrumenter(String sensClass) {
		this.sensor = sensClass;
	}

	@Override
	public void onMethodEnter(String owner, String name, String desc, AdviceAdapter parent, MethodVisitor mv) {
		// add sensor call
		mv.visitLdcInsn(sensor);
		mv.visitLdcInsn(name + desc);
		mv.visitLdcInsn(owner);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "rocks/inspectit/android/AndroidAgent", "enterBody",
				"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)J", false);
		index = parent.newLocal(Type.LONG_TYPE);
		mv.visitVarInsn(Opcodes.LSTORE, index);
	}

	@Override
	public void onMethodExit(int opcode, String owner, String name, String desc, AdviceAdapter parent,
			MethodVisitor mv) {
		if (opcode == Opcodes.ATHROW) {
			mv.visitInsn(Opcodes.DUP);
			mv.visitVarInsn(Opcodes.LLOAD, index);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "rocks/inspectit/android/AndroidAgent", "exitErrorBody",
					"(Ljava/lang/Throwable;J)V", false);
		} else {
			mv.visitVarInsn(Opcodes.LLOAD, index);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "rocks/inspectit/android/AndroidAgent", "exitBody", "(J)V", false);
		}
	}
}
