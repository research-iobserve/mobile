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

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Interface which provides methods for instrumenting a method.
 * 
 * @author David Monschein
 *
 */
public interface IBytecodeInstrumenter {
	/**
	 * Executed at the method enter point.
	 * 
	 * @param owner
	 *            class name
	 * @param name
	 *            method name
	 * @param desc
	 *            method description
	 * @param parent
	 *            belonging AdviceAdapter which can be used to insert bytecode
	 * @param mv
	 *            belonging MethodVisitor instance
	 */
	void onMethodEnter(String owner, String name, String desc, AdviceAdapter parent, MethodVisitor mv);

	/**
	 * Executed at the method exit point.
	 * 
	 * @param opcode
	 *            opcode of the exit
	 * @param owner
	 *            class name
	 * @param name
	 *            method name
	 * @param desc
	 *            method description
	 * @param parent
	 *            belonging AdviceAdapter which can be used to insert bytecode
	 * @param mv
	 *            belonging MethodVisitor instance
	 */
	void onMethodExit(int opcode, String owner, String name, String desc, AdviceAdapter parent, MethodVisitor mv);
}
