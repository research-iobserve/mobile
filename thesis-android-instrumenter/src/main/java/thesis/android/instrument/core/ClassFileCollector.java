package thesis.android.instrument.core;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class ClassFileCollector {

	private static final FilenameFilter FILTER = new FilenameFilter() {
		public boolean accept(File dir, String filename) {
			return filename.endsWith(".class");
		}
	};

	private List<File> classFiles;
	private File baseDirectory;

	public ClassFileCollector(File baseDirectory) {
		this.setBaseDirectory(baseDirectory);
	}

	public void collectClassFiles() {
		classFiles = getAllClassFiles(baseDirectory);
	}

	public File getBaseDirectory() {
		return baseDirectory;
	}

	public void setBaseDirectory(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}

	public List<File> getClassFiles() {
		return classFiles;
	}

	private List<File> getAllClassFiles(File parent) {
		return getAllClassFiles(parent, new ArrayList<File>());
	}

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
