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
package org.iobserve.mobile.instrument.bytecode.visitors;

import org.iobserve.mobile.instrument.config.InstrumentationConfiguration;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Default method code visitor which inspects every method call.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class DefaultMethodCodeVisitor extends AdviceAdapter {

	/**
	 * Configuration of the instrumentation.
	 */
	private InstrumentationConfiguration configuration;

	/**
	 * Belonging class visitor.
	 */
	private StandardClassVisitor parent;

	/**
	 * Counter which holds the number of stack places needed by the instrumented
	 * code.
	 */
	private int k;

	/**
	 * Creates a new default method code visitor.
	 * 
	 * @param config
	 *            the instrumentation configuration
	 * @param parent
	 *            the class visitor parent
	 * @param api
	 *            the api version
	 * @param mv
	 *            the method visitor
	 * @param access
	 *            the access code
	 * @param name
	 *            the name of the method
	 * @param desc
	 *            the description of the method
	 */
	protected DefaultMethodCodeVisitor(final InstrumentationConfiguration config, final StandardClassVisitor parent,
			final int api, final MethodVisitor mv, final int access, final String name, final String desc) {
		super(api, mv, access, name, desc);
		this.configuration = config;
		this.parent = parent;
		this.k = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc,
			final boolean itf) {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitMaxs(final int maxStack, final int maxLocals) {
		super.visitMaxs(maxStack + k, maxLocals);
	}

	/**
	 * Increases the stack size by a given value.
	 * 
	 * @param kk
	 *            the size which should be added to the max stack size
	 */
	public void increaseStack(final int kk) {
		this.k += kk;
	}

}
