package org.iobserve.mobile.instrument.bytecode;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iobserve.mobile.agent.core.AndroidAgent;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Class which instruments the SystemWebViewClient of Apache cordova. This
 * allows us to intercept javascript ajax calls and therefore get more network
 * data.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class SystemWebViewInstrumenter implements IBytecodeInstrumenter {

	/** Logger for this class. */
	private static final Logger LOG = LogManager.getLogger(SystemWebViewInstrumenter.class);

	/** Type of {@link AndroidAgent}. */
	private static final Type ANDROIDAGENT_TYPE = Type.getType(AndroidAgent.class);

	/** Method name for the targeted method. */
	private static final String SYSTEMWEBVIEW_INTERCEPT = "shouldInterceptRequest";

	/**
	 * Method link to {@link AndroidAgent#webViewLoad(String)}.
	 */
	private Method webViewLoadMethod;

	/**
	 * Type of method {@link AndroidAgent#webViewLoad(String)}.
	 */
	private Type webViewLoadType;

	/**
	 * Creates a new instance and resolves related agent methods.
	 */
	public SystemWebViewInstrumenter() {
		try {
			webViewLoadMethod = AndroidAgent.class.getMethod("webViewLoad", String.class);
			webViewLoadType = Type.getType(webViewLoadMethod);
		} catch (NoSuchMethodException | SecurityException e) {
			LOG.warn("Couldn't find all corresponding methods!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMethodEnter(String owner, String name, String desc, AdviceAdapter parent, MethodVisitor mv) {
		if (name.equals(SYSTEMWEBVIEW_INTERCEPT)) {
			mv.visitVarInsn(Opcodes.ALOAD, 2);

			// write own
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(), webViewLoadMethod.getName(),
					webViewLoadType.getDescriptor(), false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMethodExit(int opcode, String owner, String name, String desc, AdviceAdapter parent,
			MethodVisitor mv) {
	}

}
