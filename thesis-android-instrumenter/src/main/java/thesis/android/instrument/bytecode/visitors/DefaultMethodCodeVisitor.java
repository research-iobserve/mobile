package thesis.android.instrument.bytecode.visitors;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import thesis.android.instrument.config.InstrumentationConfiguration;

/**
 * Default method code visitor which inspects every method call.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class DefaultMethodCodeVisitor extends AdviceAdapter {

	/**
	 * Configuration of the instrumentation.
	 */
	private InstrumentationConfiguration configuration;

	/**
	 * Belonging class visitor.
	 */
	private StandardClassVisitor parent;

	/**
	 * Counter which holds the number of stack places needed by the instrumented
	 * code.
	 */
	private int k;

	/**
	 * Creates a new default method code visitor.
	 * 
	 * @param config
	 *            the instrumentation configuration
	 * @param parent
	 *            the class visitor parent
	 * @param api
	 *            the api version
	 * @param mv
	 *            the method visitor
	 * @param access
	 *            the access code
	 * @param name
	 *            the name of the method
	 * @param desc
	 *            the description of the method
	 */
	protected DefaultMethodCodeVisitor(InstrumentationConfiguration config, StandardClassVisitor parent, int api,
			MethodVisitor mv, int access, String name, String desc) {
		super(api, mv, access, name, desc);
		this.configuration = config;
		this.parent = parent;
		this.k = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		if (configuration.matchesHttpInstrumentationPoint(owner, desc)) {
			if (configuration.getNetworkBytecodeInstrumenter().visit(this, mv, opcode, owner, name, desc, itf)) {
				parent.setWritten(true);
			} else {
				super.visitMethodInsn(opcode, owner, name, desc, itf);
			}
		} else {
			super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		super.visitMaxs(maxStack + k, maxLocals);
	}

	/**
	 * Increases the stack size by a given value.
	 * 
	 * @param k
	 *            the size which should be added to the max stack size
	 */
	public void increaseStack(int k) {
		this.k += k;
	}

}
