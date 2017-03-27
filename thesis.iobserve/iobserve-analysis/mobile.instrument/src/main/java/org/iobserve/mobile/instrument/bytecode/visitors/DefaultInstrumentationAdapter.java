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

import org.iobserve.mobile.instrument.bytecode.IBytecodeInstrumenter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Class which implements an advice adapter for visiting class methods.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class DefaultInstrumentationAdapter extends AdviceAdapter {

	/**
	 * Belonging instrumenter which performs code adjustments.
	 */
	private IBytecodeInstrumenter instrumenter;

	/**
	 * Name of the method.
	 */
	private String name;

	/**
	 * Description of the method.
	 */
	private String desc;

	/**
	 * Class which provides the method.
	 */
	private String owner;

	/**
	 * Creates a new instrumentation adapter for a method.
	 * 
	 * @param api
	 *            the api version
	 * @param owner
	 *            the class
	 * @param access
	 *            the access code
	 * @param name
	 *            the name of the method
	 * @param desc
	 *            the description of the method
	 * @param mv
	 *            the method visitor
	 * @param instrumenter
	 *            instrumenter which performs code adjustments
	 */
	public DefaultInstrumentationAdapter(final int api, final String owner, final int access, final String name,
			final String desc, final MethodVisitor mv, final IBytecodeInstrumenter instrumenter) {

		super(api, mv, access, name, desc);
		this.name = name;
		this.desc = desc;
		this.owner = owner;

		this.instrumenter = instrumenter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onMethodEnter() {
		super.onMethodEnter();
		instrumenter.onMethodEnter(owner, name, desc, this, mv);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onMethodExit(final int opcode) {
		super.onMethodExit(opcode);
		instrumenter.onMethodExit(opcode, owner, name, desc, this, mv);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitMaxs(final int maxStack, final int maxLocals) {
		super.visitMaxs(maxStack + 3, maxLocals + 1); // to fit our needs
	}

}
