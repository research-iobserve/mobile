/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
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
 * @author Robert Heinrich
 * @author David Monschein
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
	public SensorBytecodeInstrumenter(final String sensClass) {
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
	public void onMethodEnter(final String owner, final String name, final String desc, final AdviceAdapter parent,
			final MethodVisitor mv) {
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
	public void onMethodExit(final int opcode, final String owner, final String name, final String desc,
			final AdviceAdapter parent, final MethodVisitor mv) {
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
