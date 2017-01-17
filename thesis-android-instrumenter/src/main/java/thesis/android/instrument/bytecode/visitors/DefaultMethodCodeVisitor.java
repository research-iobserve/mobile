package thesis.android.instrument.bytecode.visitors;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import thesis.android.instrument.config.InstrumentationConfiguration;

public class DefaultMethodCodeVisitor extends AdviceAdapter {

	private InstrumentationConfiguration configuration;
	private StandardClassVisitor parent;

	protected DefaultMethodCodeVisitor(InstrumentationConfiguration config, StandardClassVisitor parent, int api,
			MethodVisitor mv, int access, String name, String desc) {
		super(api, mv, access, name, desc);
		this.configuration = config;
		this.parent = parent;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		if (configuration.matchesHttpInstrumentationPoint(owner, desc)) {
			if (configuration.getNetworkBytecodeInstrumenter().visit(mv, opcode, owner, name, desc, itf)) {
				parent.setWritten(true);
			}
		} else {
			mv.visitMethodInsn(opcode, owner, name, desc, itf);
		}
	}

}
