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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iobserve.mobile.instrument.bytecode.visitors.StandardClassVisitor;
import org.iobserve.mobile.instrument.config.InstrumentationConfiguration;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * Class, which helps to instrument Java bytecode.
 * 
 * @author David Monschein
 *
 */
public class BytecodeInstrumentationManager {

	/**
	 * Logger for output.
	 */
	private static final Logger LOG = LogManager.getLogger(BytecodeInstrumentationManager.class);

	/**
	 * Class files which should be instrumented.
	 */
	private List<File> classFiles;

	/**
	 * The configuration which is used in the instrumentation process.
	 */
	private InstrumentationConfiguration configuration;

	/**
	 * Creates a new BytecodeInstrumentionManager instance from a list of class
	 * files.
	 * 
	 * @param classFiles
	 *            a list of class files which should be instrumented
	 */
	public BytecodeInstrumentationManager(final List<File> classFiles) {
		this.classFiles = classFiles;
	}

	/**
	 * Executes the instrumentation of the class files.
	 * 
	 * @param config
	 *            which should be used
	 * @throws IOException
	 *             happens if the reading or writing of class files fails.
	 */
	public void executeInstrumentation(final InstrumentationConfiguration config) throws IOException {
		this.configuration = config;
		// EXECUTE INSTRUMENTATION
		for (File classFile : classFiles) {
			final FileInputStream inputStream = new FileInputStream(classFile);

			ClassReader reader = null;
			ClassWriter writer = null;
			try {
				reader = new ClassReader(inputStream);
				writer = new ClassWriter(reader, ClassReader.EXPAND_FRAMES);
			} catch (IOException e) {
				LOG.warn("Failed to read class file '" + classFile.getAbsolutePath() + "' with ASM.");
			}

			inputStream.close();
			if (reader != null && writer != null) {
				// INSTRUMENTATION PROCESS
				boolean written = false;

				// WATCH EVERYTHING WITH METHOD VISITOR
				if (!written) {
					final StandardClassVisitor classVisitor = new StandardClassVisitor(Opcodes.ASM5, writer,
							configuration);
					reader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

					if (classVisitor.isWritten()) {
						written = true;
					}
				}

				// WRITE BACK TO FILE
				if (written) {
					final byte[] bytes = writer.toByteArray();
					final FileOutputStream fos;
					try {
						fos = new FileOutputStream(classFile);
						fos.write(bytes);
						fos.close();
					} catch (IOException e) {
						LOG.warn("Failed to write modified class file '" + classFile.getAbsolutePath() + "'.");
					}
				}
			}
		}
	}

}
