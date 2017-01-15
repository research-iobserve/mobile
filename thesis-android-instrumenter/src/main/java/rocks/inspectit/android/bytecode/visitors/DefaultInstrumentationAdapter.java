package rocks.inspectit.android.bytecode.visitors;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

import rocks.inspectit.android.bytecode.IBytecodeInstrumenter;

public class DefaultInstrumentationAdapter extends AdviceAdapter {

	private IBytecodeInstrumenter instrumenter;
	private String name;
	private String desc;
	private String owner;

	public DefaultInstrumentationAdapter(int api, String owner, int access, String name, String desc, MethodVisitor mv,
			IBytecodeInstrumenter instrumenter) {

		super(api, mv, access, name, desc);
		this.name = name;
		this.desc = desc;
		this.owner = owner;

		this.instrumenter = instrumenter;
	}

	@Override
	protected void onMethodEnter() {
		super.onMethodEnter();
		instrumenter.onMethodEnter(owner, name, desc, this, mv);
	}

	@Override
	protected void onMethodExit(int opcode) {
		super.onMethodExit(opcode);
		instrumenter.onMethodExit(opcode, owner, name, desc, this, mv);
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		super.visitMaxs(maxStack + 3, maxLocals + 1);
	}

}
