package rocks.inspectit.android.bytecode;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class NetworkBytecodeInstrumenter {

	public boolean visit(MethodVisitor mv, int opcode, String owner, String name, String desc, boolean itf) {
		if (descReturnType(desc).equalsIgnoreCase("Ljava/net/HttpURLConnection;")) {
			// write old
			mv.visitMethodInsn(opcode, owner, name, desc, itf);
			// DUP
			mv.visitInsn(Opcodes.DUP);
			// write own
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "rocks/inspectit/android/AndroidAgent", "httpConnect",
					"(Ljava/net/HttpURLConnection;)V", false);
		} else if (owner.equalsIgnoreCase("java/net/HttpURLConnection") && name.equals("getOutputStream")) {
			// write own
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "rocks/inspectit/android/AndroidAgent", "httpOutputStream",
					"(Ljava/net/HttpURLConnection;)Ljava/io/OutputStream", false);
		} else if (owner.equalsIgnoreCase("java/net/HttpURLConnection") && name.equals("getResponseCode")) {
			// write own
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "rocks/inspectit/android/AndroidAgent", "httpResponseCode",
					"(Ljava/net/HttpURLConnection;)I", false);
		} else {
			mv.visitMethodInsn(opcode, owner, name, desc, itf);
			return false;
		}

		return true;
	}

	private String descReturnType(String desc) {
		String[] sp = desc.split("\\)");
		return sp[1];
	}

}
