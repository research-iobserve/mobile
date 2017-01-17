package thesis.android.instrument.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import thesis.android.instrument.bytecode.BytecodeInstrumentationManager;
import thesis.android.instrument.bytecode.IBytecodeInstrumenter;
import thesis.android.instrument.config.InstrumentationConfiguration;

public class APKInstrumenter {

	private static final Logger LOG = LogManager.getLogger(APKInstrumenter.class);

	private static final File AGENT_CURRENT = new File("../thesis.android.agent/build/classes/main");
	private static final File AGENT_SHARED = new File("../thesis.shared/build/classes/main");
	private static final File AGENT_LIB_CURRENT = new File("../thesis.android.agent/build/libs");
	private static final File modifManifest = new File("modified_manifest.xml");

	private static final double MB_FACTOR = 1024d * 1024d;

	private boolean override;
	private IBytecodeInstrumenter instrumenter;
	private File keystore;
	private String alias;
	private String pass;
	private List<String> neededRights;

	private boolean buildAgent = true;
	private boolean cleanBefore = true;
	private boolean cleanAfter = true;
	private boolean adjustManifest = true;

	public APKInstrumenter(boolean override, IBytecodeInstrumenter instr, File keystore, String alias, String pass) {
		this.setOverride(override);
		this.setInstrumenter(instr);
		this.setKeystore(keystore);
		this.setAlias(alias);
		this.setPass(pass);
	}

	public boolean instrumentAPK(File input, File output) throws IOException, ZipException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, URISyntaxException {
		// CHECK IF INPUT EXISTS AND OUTPUT DOESNT
		if (!input.exists() || (output.exists() && !override)) {
			return false;
		}

		// LOAD CONFIGURATION
		InstrumentationConfiguration instrConfig = new InstrumentationConfiguration();
		File instrConfigFile = new File(APKInstrumenter.class.getResource("/config/default.xml").toURI());
		try {
			instrConfig.parseConfigFile(instrConfigFile);
		} catch (JAXBException e) {
			LOG.error("Failed to load configuration.");
			return false;
		}

		LOG.info("Successfully loaded instrumentation config '" + instrConfigFile.getAbsolutePath() + "'.");

		// INSERT RIGHTS NEEDED
		this.neededRights = instrConfig.getXmlConfiguration().getManifestTransformer().getPermissions();

		// CLEAN
		if (cleanBefore) {
			LOG.info("Cleaning up all folders.");
			File tempOutputFolder = new File("tempo");
			File jarFilesContainer = new File("temp");
			File tempNewDex = new File("temp-new.dex");
			File tempJar = new File("temp-jar.jar");

			FileUtils.deleteDirectory(jarFilesContainer);
			FileUtils.deleteDirectory(tempOutputFolder);

			File tempRezipFile = new File("jar-rezip.jar");
			File tempDex = new File("temp-dex.dex");

			tempJar.delete();
			tempNewDex.delete();
			tempRezipFile.delete();
			tempDex.delete();
			modifManifest.delete();
		}

		// ZIP CURRENT AGENT VERSION
		if (buildAgent) {
			LOG.info("Bundling current agent version.");
			File agentZipOld = new File("rocks.zip");
			agentZipOld.delete();
			ZipFile agentZipNew = new ZipFile("rocks.zip");
			
			FileUtils.copyDirectory(AGENT_SHARED, AGENT_CURRENT);
			
			agentZipNew.addFolder(AGENT_CURRENT, new ZipParameters());

			File[] libs = AGENT_LIB_CURRENT.listFiles();
			for (File lib : libs) {
				if (lib.isFile() && !lib.getName().contains("thesis.android.agent")) {
					ZipFile zipF = new ZipFile(lib);
					zipF.extractAll(AGENT_LIB_CURRENT.getAbsolutePath() + "\\temp\\");
				}
			}

			File toAdd = new File(AGENT_LIB_CURRENT.getAbsolutePath() + "\\temp\\");
			File[] toAddArray = new File(toAdd.getAbsolutePath() + "\\rocks\\").listFiles();

			// TODO FIX
			File[] toAddArray2 = new File(toAdd.getAbsolutePath() + "\\org\\").listFiles();
			File[] toAddArray3 = new File(toAdd.getAbsolutePath() + "\\kieker\\").listFiles();

			ZipParameters paras = new ZipParameters();
			paras.setRootFolderInZip("rocks");
			for (File ff : toAddArray) {
				if (ff.isDirectory()) {
					agentZipNew.addFolder(ff, paras);
				} else {
					agentZipNew.addFile(ff, paras);
				}
			}

			paras.setRootFolderInZip("org");
			for (File ff : toAddArray2) {
				if (ff.isDirectory()) {
					agentZipNew.addFolder(ff, paras);
				} else {
					agentZipNew.addFile(ff, paras);
				}
			}

			paras.setRootFolderInZip("kieker");
			for (File ff : toAddArray3) {
				if (ff.isDirectory()) {
					agentZipNew.addFolder(ff, paras);
				} else {
					agentZipNew.addFile(ff, paras);
				}
			}

			LOG.info("Bundled agent with an actual size of " + round(agentZipNew.getFile().length() / MB_FACTOR)
					+ "MB.");
		}

		// ADD MANIFEST ADJUSTMENTS
		// APKTOOL
		if (adjustManifest) {
			LOG.info("Decoding application with APKTool.");

			APKToolProxy apkTool = new APKToolProxy(input);
			boolean b1 = apkTool.decodeAPK("intermediate");
			boolean b2 = apkTool.adjustManifest(neededRights, modifManifest);

			if (!b1 || !b2) {
				adjustManifest = false;
			} else {
				instrConfig.setApplicationPackage(apkTool.getPackageName());
			}
			apkTool.cleanup();

			LOG.info("APKTool finished decoding the application.");
		}

		// IF OVERRIDE DELETE IT
		if (output.exists()) {
			if (!output.delete()) {
				return false;
			}
		}

		File agentZip = new File("rocks.zip");
		if (!agentZip.exists()) {
			return false;
		}

		// COPY INPUT TO OUTPUT
		File tempOutputFolder = new File("tempo");
		tempOutputFolder.mkdir();
		ZipFile tempInputZip = new ZipFile(input);
		tempInputZip.extractAll(tempOutputFolder.getAbsolutePath() + "/");

		LOG.info("Created copy of original APK file.");

		// TEMP WRITE DEX
		File tempDex = new File("temp-dex.dex");
		unzipDex(input, tempDex);

		LOG.info("Searched and extracted dex files.");

		// DEX 2 JAR
		LOG.info("Executing dex2jar to transform DEX files to JAR files.");
		File tempJar = new File("temp-jar.jar");
		Dex2JarProxy.createJarFromDex(tempDex, tempJar);
		LOG.info("Dex2jar finished transformation.");

		// UNZIP JAR
		File jarFilesContainer = new File("temp");
		jarFilesContainer.mkdir();

		ZipFile jarZip = new ZipFile(tempJar);
		jarZip.extractAll(jarFilesContainer.getAbsolutePath() + "/");

		LOG.info("Extracted JAR file for accessing class files.");

		LOG.info("Started instrumentation of class files.");
		ClassFileCollector cfColl = new ClassFileCollector(jarFilesContainer);
		cfColl.collectClassFiles();

		BytecodeInstrumentationManager manager = new BytecodeInstrumentationManager(cfColl.getClassFiles());
		manager.executeInstrumentation(instrConfig);
		LOG.info("Successfully instrumented class files.");

		// INJECT AGENT
		LOG.info("Injecting agent files into rezipped JAR.");
		ZipFile agentZipfile = new ZipFile(agentZip);
		agentZipfile.extractAll(jarFilesContainer.getAbsolutePath() + "/");
		LOG.info("Agent has been injected.");

		// CREATE ZIP
		LOG.info("Rebuilding JAR file from modified class files.");
		File tempRezipFile = new File("jar-rezip.jar");
		ZipFile nZip = new ZipFile(tempRezipFile);
		ZipParameters parameters = new ZipParameters();

		for (File fileEntry : jarFilesContainer.listFiles()) {
			if (fileEntry.isDirectory()) {
				nZip.addFolder(fileEntry, parameters);
			} else {
				nZip.addFile(fileEntry, parameters);
			}
		}
		LOG.info("Finished building new JAR file.");

		// JAR 2 DEX
		File tempNewDex = new File("temp-new.dex");
		LOG.info("Executing jar2dex to transform new JAR file to DEX.");
		Dex2JarProxy.createDexFromJar(tempRezipFile, tempNewDex);
		LOG.info("jar2dex has been successfully executed.");

		// EDIT OLD classes.dex and remove META INF
		LOG.info("Removing old signature.");
		File oldMeta = new File(tempOutputFolder.getAbsolutePath() + "/META-INF");
		FileUtils.deleteDirectory(oldMeta);

		File oClasses = new File(tempOutputFolder.getAbsolutePath() + "/classes.dex");
		oClasses.delete();
		Files.copy(tempNewDex.toPath(), oClasses.toPath());

		// MODIFY MANIFEST
		if (adjustManifest) {
			LOG.info("Adjusting Android manifest.");
			File oManifest = new File(tempOutputFolder.getAbsolutePath() + "/AndroidManifest.xml");
			oManifest.delete();
			Files.copy(modifManifest.toPath(), oManifest.toPath());
		}

		// REZIP
		LOG.info("Rezip all files to output APK.");
		ZipFile zipOutput = new ZipFile(output);
		File[] fList = tempOutputFolder.listFiles();
		for (File fInner : fList) {
			if (fInner.isDirectory()) {
				zipOutput.addFolder(fInner, parameters);
			} else {
				zipOutput.addFile(fInner, parameters);
			}
		}

		// RESIGN
		LOG.info("Resigning the output APK.");
		JarSigner signer = new JarSigner();
		signer.signJar(output, getKeystore(), getAlias(), getPass());

		// REMOVE ALL TEMP FILES
		if (cleanAfter) {
			LOG.info("Cleanup of all temporary files and folders.");

			FileUtils.deleteDirectory(jarFilesContainer);
			FileUtils.deleteDirectory(tempOutputFolder);

			tempJar.delete();
			tempNewDex.delete();
			tempRezipFile.delete();
			tempDex.delete();
			agentZip.delete();
			modifManifest.delete();
		}

		LOG.info("Instrumentation finished.");
		LOG.info("Instrumented application is located at '" + output.getAbsolutePath() + "'.");

		return true;
	}

	public boolean isOverride() {
		return override;
	}

	public void setOverride(boolean override) {
		this.override = override;
	}

	public IBytecodeInstrumenter getInstrumenter() {
		return instrumenter;
	}

	public void setInstrumenter(IBytecodeInstrumenter instrumenter) {
		this.instrumenter = instrumenter;
	}

	public File getKeystore() {
		return keystore;
	}

	public void setKeystore(File keystore) {
		this.keystore = keystore;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	private void unzipDex(File apk, File dex) throws ZipException, IOException {
		if (apk.exists() && !dex.exists()) {
			ZipFile parent = new ZipFile(apk);

			@SuppressWarnings("unchecked")
			List<FileHeader> headerList = parent.getFileHeaders();

			for (FileHeader header : headerList) {
				if (header.getFileName().equals("classes.dex")) {
					ZipInputStream in = parent.getInputStream(header);
					FileOutputStream os = new FileOutputStream(dex);
					int readLen = -1;
					byte[] buff = new byte[4096];
					while ((readLen = in.read(buff)) != -1) {
						os.write(buff, 0, readLen);
					}
					closeStreams(in, os);
					break;
				}
			}
		}
	}

	private void closeStreams(ZipInputStream a, FileOutputStream b) throws IOException {
		if (a != null)
			a.close();
		if (b != null)
			b.close();
	}

	private double round(double a) {
		return Math.round(a * 100d) / 100d;
	}
}
