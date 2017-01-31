package thesis.android.instrument.bytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Interface which provides methods for instrumenting a method.
 * 
 * @author David Monschein
 *
 */
public interface IBytecodeInstrumenter {
	/**
	 * Executed at the method enter point.
	 * 
	 * @param owner
	 *            class name
	 * @param name
	 *            method name
	 * @param desc
	 *            method description
	 * @param parent
	 *            belonging AdviceAdapter which can be used to insert bytecode
	 * @param mv
	 *            belonging MethodVisitor instance
	 */
	void onMethodEnter(String owner, String name, String desc, AdviceAdapter parent, MethodVisitor mv);

	/**
	 * Executed at the method exit point.
	 * 
	 * @param opcode
	 *            opcode of the exit
	 * @param owner
	 *            class name
	 * @param name
	 *            method name
	 * @param desc
	 *            method description
	 * @param parent
	 *            belonging AdviceAdapter which can be used to insert bytecode
	 * @param mv
	 *            belonging MethodVisitor instance
	 */
	void onMethodExit(int opcode, String owner, String name, String desc, AdviceAdapter parent, MethodVisitor mv);
}
