package thesis.android.instrument.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JarSigner {

	private static final Logger LOG = LogManager.getLogger(JarSigner.class);

	public JarSigner() {
	}

	public void signJar(File jarFile, String keyStore, String alias, String pass) throws IOException {
		ProcessBuilder processBuilder = new ProcessBuilder("jarsigner", "-sigalg", "SHA1withRSA", "-digestalg", "SHA1",
				"-keystore", keyStore, jarFile.getAbsolutePath(), alias);

		Process signJarProcess = processBuilder.start();

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(signJarProcess.getOutputStream()));

		writer.write(pass, 0, pass.length());
		writer.newLine();
		writer.close();

		try {
			signJarProcess.waitFor();
		} catch (InterruptedException e) {
			LOG.error("JarSigner failed to resign file '" + jarFile.getAbsolutePath() + "' with keystore '" + keyStore
					+ "'.");
			return;
		}

	}

	public void signJar(File jarFile, File keyStore, String alias, String pass) throws IOException {
		signJar(jarFile, keyStore.getAbsolutePath(), alias, pass);
	}

}
