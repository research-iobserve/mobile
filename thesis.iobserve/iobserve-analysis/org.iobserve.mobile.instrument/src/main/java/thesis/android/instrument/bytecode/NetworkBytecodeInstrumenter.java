package thesis.android.instrument.bytecode;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import android.webkit.WebView;
import rocks.inspectit.android.AndroidAgent;
import thesis.android.instrument.bytecode.visitors.DefaultMethodCodeVisitor;

/**
 * Class which is responsible for instrumenting calls which result in a network
 * activity.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class NetworkBytecodeInstrumenter {

	/** Logger for this class. */
	private static final Logger LOG = LogManager.getLogger(NetworkBytecodeInstrumenter.class);

	/** Type of {@link HttpURLConnection}. */
	private static final Type HTTPURLCONNECTION_TYPE = Type.getType(HttpURLConnection.class);

	/** Type of {@link URLConnection}. */
	private static final Type URLCONNECTION_TYPE = Type.getType(URLConnection.class);

	/** Type of {@link AndroidAgent}. */
	private static final Type ANDROIDAGENT_TYPE = Type.getType(AndroidAgent.class);

	/** Type of {@link WebView}. */
	private static final Type ANDROIDWEBVIEW_TYPE = Type.getType(android.webkit.WebView.class);

	// METHODS AND THEIR TYPES
	/**
	 * Method link to {@link AndroidAgent#httpConnect(HttpURLConnection)}.
	 */
	private Method httpConnectMethod;

	/**
	 * Type of method {@link AndroidAgent#httpConnect(HttpURLConnection)}.
	 */
	private Type httpConnectType;

	/**
	 * Method link to {@link AndroidAgent#httpConnect(URLConnection)}.
	 */
	private Method httpConnectMethod2;

	/**
	 * Type of method {@link AndroidAgent#httpConnect(URLConnection)}.
	 */
	private Type httpConnectType2;

	/**
	 * Method link to {@link AndroidAgent#httpOutputStream(HttpURLConnection)}.
	 */
	private Method getOutputStreamMethod;

	/**
	 * Type of method {@link AndroidAgent#httpOutputStream(HttpURLConnection)}.
	 */
	private Type getOutputStreamType;

	/**
	 * Method link to {@link AndroidAgent#httpResponseCode(HttpURLConnection)}.
	 */
	private Method getResponseCodeMethod;

	/**
	 * Type of method {@link AndroidAgent#httpResponseCode(HttpURLConnection)}.
	 */
	private Type getResponseCodeType;

	/**
	 * Method link to {@link AndroidAgent#webViewLoad(String)}.
	 */
	private Method webViewLoadMethod;

	/**
	 * Type of method {@link AndroidAgent#webViewLoad(String)}.
	 */
	private Type webViewLoadType;

	/**
	 * Creates a new instance which resolves all corresponding agent methods.
	 */
	public NetworkBytecodeInstrumenter() {
		try {
			httpConnectMethod = AndroidAgent.class.getMethod("httpConnect", HttpURLConnection.class);
			httpConnectType = Type.getType(httpConnectMethod);

			httpConnectMethod2 = AndroidAgent.class.getMethod("httpConnect", URLConnection.class);
			httpConnectType2 = Type.getType(httpConnectMethod2);

			getOutputStreamMethod = AndroidAgent.class.getMethod("httpOutputStream", HttpURLConnection.class);
			getOutputStreamType = Type.getType(getOutputStreamMethod);

			getResponseCodeMethod = AndroidAgent.class.getMethod("httpResponseCode", HttpURLConnection.class);
			getResponseCodeType = Type.getType(getResponseCodeMethod);

			webViewLoadMethod = AndroidAgent.class.getMethod("webviewLoad", String.class);
			webViewLoadType = Type.getType(webViewLoadMethod);
		} catch (NoSuchMethodException | SecurityException e) {
			LOG.warn("Couldn't find all corresponding methods!");
		}
	}

	/**
	 * Visits an operation within a method and if it is a network action it will
	 * be instrumented.
	 * 
	 * @param mv
	 *            the belonging method code visitor
	 * @param mv_internal
	 *            the method visitor which is used internally
	 * @param opcode
	 *            the opcode
	 * @param owner
	 *            the owner
	 * @param name
	 *            the name
	 * @param desc
	 *            the description
	 * @param itf
	 *            if it is an interface method or not
	 * @return true if it was a network action and it is instrumented now -
	 *         false otherwise
	 */
	public boolean visit(DefaultMethodCodeVisitor mv, MethodVisitor mv_internal, int opcode, String owner, String name,
			String desc, boolean itf) {
		// BYTECODE MAGIC
		if (descReturnType(desc).equalsIgnoreCase(HTTPURLCONNECTION_TYPE.getDescriptor())) {
			mv.increaseStack(1);
			// write old
			mv_internal.visitMethodInsn(opcode, owner, name, desc, itf);
			// DUP
			mv.dup();
			// write own
			mv_internal.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(),
					httpConnectMethod.getName(), httpConnectType.getDescriptor(), false);
		} else if (descReturnType(desc).equalsIgnoreCase(URLCONNECTION_TYPE.getDescriptor())) {
			mv.increaseStack(1);
			// write old
			mv_internal.visitMethodInsn(opcode, owner, name, desc, itf);
			// DUP
			mv.dup();
			// write own
			mv_internal.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(),
					httpConnectMethod2.getName(), httpConnectType2.getDescriptor(), false);
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
				if (!desc.contains("java/util/Map")) {
					mv.increaseStack(2);

					mv.dup2();
					// write old
					mv_internal.visitMethodInsn(opcode, owner, name, desc, itf);

					// write own
					mv_internal.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(),
							webViewLoadMethod.getName(), webViewLoadType.getDescriptor(), false);

					mv.pop();
				}
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
