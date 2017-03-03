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
package org.iobserve.mobile.instrument.start;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;
import org.iobserve.mobile.instrument.core.APKInstrumenter;

import net.lingala.zip4j.exception.ZipException;

/**
 * Class which instruments a selected Android application.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public final class InstrumenterTest {

	/**
	 * Whether we should open a prompt to select the application or use a
	 * predefined application.
	 */
	private static final boolean SELECTAPK = true;

	/**
	 * No instance creation allowed because static main execution.
	 */
	private InstrumenterTest() {
	}

	/**
	 * Executes the instrumentation.
	 * 
	 * @param argv
	 *            not needed
	 * @throws KeyStoreException
	 *             caused by the instrumentation
	 * @throws NoSuchAlgorithmException
	 *             caused by the instrumentation
	 * @throws CertificateException
	 *             caused by the instrumentation
	 * @throws IOException
	 *             caused by the instrumentation
	 * @throws ZipException
	 *             caused by the instrumentation
	 * @throws URISyntaxException
	 *             caused by the instrumentation
	 */
	public static void main(final String[] argv) throws KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException, ZipException, URISyntaxException {
		final File toInstrument;
		if (SELECTAPK) {
			final JFileChooser fc = new JFileChooser();
			fc.addChoosableFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return null;
				}

				@Override
				public boolean accept(final File f) {
					if (f.isDirectory()) {
						return true;
					}

					if (FilenameUtils.getExtension(f.getAbsolutePath()).equals("apk")) {
						return true;
					}
					return false;
				}
			});
			fc.setAcceptAllFileFilterUsed(false);
			final int returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				toInstrument = fc.getSelectedFile();
			} else {
				return;
			}
		} else {
			toInstrument = new File("app-debug.apk");
		}

		if (toInstrument.exists()) {
			final APKInstrumenter instr = new APKInstrumenter(true, new File("lib/release.keystore"), "androiddebugkey",
					"android");
			instr.instrumentAPK(toInstrument, new File("instr-output.apk"));
		} else {
			System.err.println("APK doesn't exist.");
		}
	}

}
