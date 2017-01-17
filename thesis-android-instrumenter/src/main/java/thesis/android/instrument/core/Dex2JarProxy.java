package thesis.android.instrument.core;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dex2JarProxy {

	private static final Logger LOG = LogManager.getLogger(Dex2JarProxy.class);

	private static final String DEX2JAR_BASEPATH = "lib/dex2jar-2.0/";

	private static final String JAR2DEX_BATCH = "d2j-jar2dex.bat";
	private static final String JAR2DEX_SH = "d2j-jar2dex.sh";

	private static final String DEX2JAR_SH = "d2j-dex2jar.sh";
	private static final String DEX2JAR_BATCH = "d2j-dex2jar.bat";

	public static boolean createJarFromDex(File dex, File output) {
		if (!dex.exists() || output.exists())
			return false;

		return executeTool(DEX2JAR_BATCH, DEX2JAR_SH, dex, output);
	}

	public static boolean createDexFromJar(File jar, File dex) {
		if (!jar.exists() || dex.exists())
			return false;

		return executeTool(JAR2DEX_BATCH, JAR2DEX_SH, jar, dex);
	}

	private static boolean executeTool(String windows, String unix, File input, File output) {
		ProcessBuilder pb = null;
		if (SystemUtils.IS_OS_WINDOWS) {
			// use the batch
			pb = new ProcessBuilder(DEX2JAR_BASEPATH + windows, input.getAbsolutePath(), "-o",
					output.getAbsolutePath());
		} else if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC) {
			// use the sh
			pb = new ProcessBuilder(DEX2JAR_BASEPATH + unix, input.getAbsolutePath(), "-o", output.getAbsolutePath());
		}

		if (pb != null) {
			try {
				pb.start().waitFor();
			} catch (IOException | InterruptedException e) {
				LOG.error("Failed to execute dex2jar or other similar subtooling.");
				return false;
			}

			return true;
		} else {
			return false;
		}
	}

}
