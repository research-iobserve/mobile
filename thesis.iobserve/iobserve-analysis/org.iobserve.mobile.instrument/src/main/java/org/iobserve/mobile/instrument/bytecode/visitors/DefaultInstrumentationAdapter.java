package org.iobserve.mobile.instrument.bytecode.visitors;

import org.iobserve.mobile.instrument.bytecode.IBytecodeInstrumenter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Class which implements an advice adapter for visiting class methods.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class DefaultInstrumentationAdapter extends AdviceAdapter {

	/**
	 * Belonging instrumenter which performs code adjustments.
	 */
	private IBytecodeInstrumenter instrumenter;

	/**
	 * Name of the method.
	 */
	private String name;

	/**
	 * Description of the method.
	 */
	private String desc;

	/**
	 * Class which provides the method.
	 */
	private String owner;

	/**
	 * Creates a new instrumentation adapter for a method.
	 * 
	 * @param api
	 *            the api version
	 * @param owner
	 *            the class
	 * @param access
	 *            the access code
	 * @param name
	 *            the name of the method
	 * @param desc
	 *            the description of the method
	 * @param mv
	 *            the method visitor
	 * @param instrumenter
	 *            instrumenter which performs code adjustments
	 */
	public DefaultInstrumentationAdapter(int api, String owner, int access, String name, String desc, MethodVisitor mv,
			IBytecodeInstrumenter instrumenter) {

		super(api, mv, access, name, desc);
		this.name = name;
		this.desc = desc;
		this.owner = owner;

		this.instrumenter = instrumenter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onMethodEnter() {
		super.onMethodEnter();
		instrumenter.onMethodEnter(owner, name, desc, this, mv);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onMethodExit(int opcode) {
		super.onMethodExit(opcode);
		instrumenter.onMethodExit(opcode, owner, name, desc, this, mv);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		super.visitMaxs(maxStack + 3, maxLocals + 1); // to fit our needs
	}

}
