package rocks.inspectit.android.instrument;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import net.lingala.zip4j.exception.ZipException;

public class InstrumenterTest {

	private boolean selectApk = false;

	@Test
	public void test() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
			ZipException, URISyntaxException {

		File toInstrument;
		if (selectApk) {
			final JFileChooser fc = new JFileChooser();
			fc.addChoosableFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return null;
				}

				@Override
				public boolean accept(File f) {
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
			int returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				toInstrument = fc.getSelectedFile();
			} else {
				return;
			}
		} else {
			toInstrument = new File("app-debug.apk");
		}

		if (toInstrument.exists()) {
			APKInstrumenter instr = new APKInstrumenter(true, null, new File("lib/release.keystore"), "androiddebugkey",
					"android");
			instr.instrumentAPK(toInstrument, new File("instr-output.apk"));
		} else {
			fail("APK doesn't exist.");
		}
	}

}
