package rocks.inspectit.android.bytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

public interface IBytecodeInstrumenter {
	public void onMethodEnter(String owner, String name, String desc, AdviceAdapter parent, MethodVisitor mv);

	public void onMethodExit(int opcode, String owner, String name, String desc, AdviceAdapter parent,
			MethodVisitor mv);
}
