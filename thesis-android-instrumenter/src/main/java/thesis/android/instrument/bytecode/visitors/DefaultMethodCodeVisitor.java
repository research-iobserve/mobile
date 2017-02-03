package thesis.android.instrument.bytecode.visitors;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import thesis.android.instrument.config.InstrumentationConfiguration;

public class DefaultMethodCodeVisitor extends AdviceAdapter {

	private InstrumentationConfiguration configuration;
	private StandardClassVisitor parent;

	private int k;

	protected DefaultMethodCodeVisitor(InstrumentationConfiguration config, StandardClassVisitor parent, int api,
			MethodVisitor mv, int access, String name, String desc) {
		super(api, mv, access, name, desc);
		this.configuration = config;
		this.parent = parent;
		this.k = 0;
	}

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

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		super.visitMaxs(maxStack + k, maxLocals);
	}

	public void increaseStack(int k) {
		this.k += k;
	}

}
