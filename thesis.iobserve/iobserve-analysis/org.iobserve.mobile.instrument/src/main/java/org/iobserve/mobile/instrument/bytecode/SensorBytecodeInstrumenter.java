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
 * Class for instrumenting a certain method. It inserts a call at the start of
 * the method and a call at the end of the method body.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class SensorBytecodeInstrumenter implements IBytecodeInstrumenter {
	/** Logger for the class. */
	private static final Logger LOG = LogManager.getLogger(SensorBytecodeInstrumenter.class);

	/** Type of {@link AndroidAgent}. */
	private static final Type ANDROIDAGENT_TYPE = Type.getType(AndroidAgent.class);

	/**
	 * Name of the belonging sensor class.
	 */
	private String sensor;

	/**
	 * Index for local variable which stores the mapping entry of the call.
	 */
	private int index;

	// METHOD CORRESPONDENTS
	/**
	 * Method link to {@link AndroidAgent#enterBody(String, String, String)}.
	 */
	private Method enterBodyMethod;

	/**
	 * Type of method {@link AndroidAgent#enterBody(String, String, String)}.
	 */
	private Type enterBodyType;

	/**
	 * Method link to {@link AndroidAgent#exitBody(long)}.
	 */
	private Method exitBodyMethod;

	/**
	 * Type of method {@link AndroidAgent#exitBody(long)}.
	 */
	private Type exitBodyType;

	/**
	 * Method link to {@link AndroidAgent#exitErrorBody(Throwable, long)}.
	 */
	private Method exitBodyErrorMethod;

	/**
	 * Type of method {@link AndroidAgent#exitErrorBody(Throwable, long)}.
	 */
	private Type exitBodyErrorType;

	/**
	 * Creates a new sensor instrumenter with a given sensor class name.
	 * 
	 * @param sensClass
	 *            name of the sensor class which is responsible for processing
	 *            the instrumented method call
	 */
	public SensorBytecodeInstrumenter(String sensClass) {
		this.sensor = sensClass;

		try {
			enterBodyMethod = AndroidAgent.class.getMethod("enterBody", String.class, String.class, String.class);
			enterBodyType = Type.getType(enterBodyMethod);

			exitBodyMethod = AndroidAgent.class.getMethod("exitBody", long.class);
			exitBodyType = Type.getType(exitBodyMethod);

			exitBodyErrorMethod = AndroidAgent.class.getMethod("exitErrorBody", Throwable.class, long.class);
			exitBodyErrorType = Type.getType(exitBodyErrorMethod);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			LOG.warn("Couldn't find all methods of the agent which are needed in the instrumentation.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMethodEnter(String owner, String name, String desc, AdviceAdapter parent, MethodVisitor mv) {
		// add sensor call
		mv.visitLdcInsn(sensor);
		mv.visitLdcInsn(name + desc);
		mv.visitLdcInsn(owner);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(), enterBodyMethod.getName(),
				enterBodyType.getDescriptor(), false);
		index = parent.newLocal(Type.LONG_TYPE);
		mv.visitVarInsn(Opcodes.LSTORE, index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMethodExit(int opcode, String owner, String name, String desc, AdviceAdapter parent,
			MethodVisitor mv) {
		if (opcode == Opcodes.ATHROW) {
			mv.visitInsn(Opcodes.DUP);
			mv.visitVarInsn(Opcodes.LLOAD, index);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(), exitBodyErrorMethod.getName(),
					exitBodyErrorType.getDescriptor(), false);
		} else {
			mv.visitVarInsn(Opcodes.LLOAD, index);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(), exitBodyMethod.getName(),
					exitBodyType.getDescriptor(), false);
		}
	}
}
