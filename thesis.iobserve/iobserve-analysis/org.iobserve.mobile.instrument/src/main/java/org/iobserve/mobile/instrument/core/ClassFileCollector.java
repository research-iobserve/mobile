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
package org.iobserve.mobile.instrument.core;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Help class which searches for all .class files in a folder recursive.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class ClassFileCollector {

	/**
	 * Used filter to determine class files.
	 */
	private static final FilenameFilter FILTER = new FilenameFilter() {
		public boolean accept(final File dir, final String filename) {
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
	public ClassFileCollector(final File baseDirectory) {
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
	public void setBaseDirectory(final File baseDirectory) {
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
	private List<File> getAllClassFiles(final File parent) {
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
	private List<File> getAllClassFiles(final File parent, final List<File> base) {
		final File[] childs = parent.listFiles();
		final File[] classfiles = parent.listFiles(FILTER);
		for (File f : classfiles) {
			base.add(f);
		}
		List<File> baseIterator = base;
		for (File f : childs) {
			if (f.isDirectory()) {
				baseIterator = getAllClassFiles(f, baseIterator);
			}
		}

		return base;
	}

}
