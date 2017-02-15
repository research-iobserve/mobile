package thesis.android.instrument.core;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Help class which searches for all .class files in a folder recursive.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class ClassFileCollector {

	/**
	 * Used filter to determine class files.
	 */
	private static final FilenameFilter FILTER = new FilenameFilter() {
		public boolean accept(File dir, String filename) {
			return filename.endsWith(".class");
		}
	};

	/**
	 * List of all class files.
	 */
	private List<File> classFiles;

	/**
	 * Parent directory to start the search from.
	 */
	private File baseDirectory;

	/**
	 * Creates a class file collector with a given base directory.
	 * 
	 * @param baseDirectory
	 *            base directory
	 */
	public ClassFileCollector(File baseDirectory) {
		this.setBaseDirectory(baseDirectory);
	}

	/**
	 * Collects all class files recursively.
	 */
	public void collectClassFiles() {
		classFiles = getAllClassFiles(baseDirectory);
	}

	/**
	 * @return the baseDirectory
	 */
	public File getBaseDirectory() {
		return baseDirectory;
	}

	/**
	 * @param baseDirectory
	 *            the baseDirectory to set
	 */
	public void setBaseDirectory(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	/**
	 * @return the class files
	 */
	public List<File> getClassFiles() {
		return classFiles;
	}

	/**
	 * Recursive method for collecting class files.
	 * 
	 * @param parent
	 *            start folder
	 * @return all child class files.
	 */
	private List<File> getAllClassFiles(File parent) {
		return getAllClassFiles(parent, new ArrayList<File>());
	}

	/**
	 * Recursive method for collecting class files.
	 * 
	 * @param parent
	 *            start folder
	 * @param base
	 *            current files
	 * @return all collected class files
	 */
	private List<File> getAllClassFiles(File parent, List<File> base) {
		File[] childs = parent.listFiles();
		File[] classfiles = parent.listFiles(FILTER);
		for (File f : classfiles) {
			base.add(f);
		}
		for (File f : childs) {
			if (f.isDirectory()) {
				base = getAllClassFiles(f, base);
			}
		}

		return base;
	}

}
