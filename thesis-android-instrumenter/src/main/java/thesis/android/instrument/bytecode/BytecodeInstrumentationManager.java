package thesis.android.instrument.bytecode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import thesis.android.instrument.bytecode.visitors.StandardClassVisitor;
import thesis.android.instrument.config.InstrumentationConfiguration;

/**
 * Class, which helps to instrument Java bytecode.s
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
	public BytecodeInstrumentationManager(List<File> classFiles) {
		this.classFiles = classFiles;
	}

	/**
	 * Executes the instrumentation of the class files.
	 * 
	 * @param configuration
	 *            which should be used
	 * @throws IOException
	 *             happens if the reading or writing of class files fails.
	 */
	public void executeInstrumentation(InstrumentationConfiguration config) throws IOException {
		this.configuration = config;
		// EXECUTE INSTRUMENTATION
		for (File classFile : classFiles) {
			FileInputStream inputStream = new FileInputStream(classFile);

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
					StandardClassVisitor classVisitor = new StandardClassVisitor(Opcodes.ASM5, writer, configuration);
					reader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

					if (classVisitor.isWritten())
						written = true;
				}

				// WRITE BACK TO FILE
				if (written) {
					byte[] bytes = writer.toByteArray();
					FileOutputStream fos;
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
