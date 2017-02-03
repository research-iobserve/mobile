package thesis.android.instrument.bytecode;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import rocks.inspectit.android.AndroidAgent;
import thesis.android.instrument.bytecode.visitors.DefaultMethodCodeVisitor;

// TODO refactor
public class NetworkBytecodeInstrumenter {

	private static final Logger LOG = LogManager.getLogger(NetworkBytecodeInstrumenter.class);

	private static final Type HTTPURLCONNECTION_TYPE = Type.getType(HttpURLConnection.class);
	private static final Type ANDROIDAGENT_TYPE = Type.getType(AndroidAgent.class);

	private static final Type ANDROIDWEBVIEW_TYPE = Type.getType(android.webkit.WebView.class);

	// METHODS AND THEIR TYPES
	private Method httpConnectMethod;
	private Type httpConnectType;

	private Method getOutputStreamMethod;
	private Type getOutputStreamType;

	private Method getResponseCodeMethod;
	private Type getResponseCodeType;

	public NetworkBytecodeInstrumenter() {
		try {
			httpConnectMethod = AndroidAgent.class.getMethod("httpConnect", HttpURLConnection.class);
			httpConnectType = Type.getType(httpConnectMethod);

			getOutputStreamMethod = AndroidAgent.class.getMethod("httpOutputStream", HttpURLConnection.class);
			getOutputStreamType = Type.getType(getOutputStreamMethod);

			getResponseCodeMethod = AndroidAgent.class.getMethod("httpResponseCode", HttpURLConnection.class);
			getResponseCodeType = Type.getType(getResponseCodeMethod);
		} catch (NoSuchMethodException | SecurityException e) {
			LOG.warn("Couldn't find all corresponding methods!");
		}
	}

	public boolean visit(DefaultMethodCodeVisitor mv, MethodVisitor mv_internal, int opcode, String owner, String name,
			String desc, boolean itf) {
		if (descReturnType(desc).equalsIgnoreCase(HTTPURLCONNECTION_TYPE.getDescriptor())) {
			mv.increaseStack(1);
			// write old
			mv_internal.visitMethodInsn(opcode, owner, name, desc, itf);
			// DUP
			mv.dup();
			// write own
			mv_internal.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(),
					httpConnectMethod.getName(), httpConnectType.getDescriptor(), false);

		} else if (owner.equalsIgnoreCase(HTTPURLCONNECTION_TYPE.getInternalName())) {
			if (name.equals("getOutputStream")) {
				// write own
				mv_internal.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(),
						getOutputStreamMethod.getName(), getOutputStreamType.getDescriptor(), false);
			} else if (name.equals("getResponseCode")) {
				// write own
				mv_internal.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(),
						getResponseCodeMethod.getName(), getResponseCodeType.getDescriptor(), false);
			} else {
				return false;
			}
		} else if (owner.equalsIgnoreCase(ANDROIDWEBVIEW_TYPE.getInternalName())) {
			if (name.equals("loadUrl")) {
				if (desc.contains("java/util/Map")) {
					// TODO
					// write old
					mv.visitMethodInsn(opcode, owner, name, desc, itf);
				} else {
					mv.increaseStack(2);

					mv.dup2();
					// write old
					mv_internal.visitMethodInsn(opcode, owner, name, desc, itf);

					// write own
					mv_internal.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(),
							"webViewLoad", "(Ljava/lang/String;)V", false);

					mv.pop();
				}
			} else if (name.equals("postUrl")) {
				// TODO
				mv.visitMethodInsn(opcode, owner, name, desc, itf);
			} else {
				return false;
			}
		} else {
			return false;
		}

		return true;
	}

	/**
	 * Helping method to determine the return type from a full method signature.
	 * 
	 * @param desc
	 *            full description of a method
	 * @return return type of that method
	 */
	private String descReturnType(String desc) {
		String[] sp = desc.split("\\)");
		return sp[1];
	}

}
