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
import org.iobserve.mobile.instrument.bytecode.BytecodeInstrumentationManager;
import org.iobserve.mobile.instrument.config.InstrumentationConfiguration;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;

/**
 * Main class which schedules and performs the instrumentation of an Android
 * application.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class APKInstrumenter {
	// TEMPORARY USED FILES
	/** Temporary file for the rezipped JAR file. */
	private static final File JAR_REZIP = new File("jar-rezip.jar");

	/** Temporary file for building the current agent version. */
	private static final File AGENT_BUILD = new File("agent-current.zip");

	/** Temporary output folder for rezipping the application. */
	private static final File OUTPUT_TEMP = new File("temp");

	/** Temporary output folder for unzipping the application. */
	private static final File OUTPUT_TEMPO = new File("tempo");

	/** New created dex file. */
	private static final File TEMP_DEX_NEW = new File("temp-new.dex");

	/** Temporary extracted dex file. */
	private static final File TEMP_DEX_OLD = new File("temp-dex.dex");

	/** Temporary transformed JAR file created by dex2jar. */
	private static final File TEMP_JAR = new File("temp-jar.jar");

	// _________________________________________________ //

	/** Logger. */
	private static final Logger LOG = LogManager.getLogger(APKInstrumenter.class);
	
	/** Project name for the shared project. */
	private static final String PROJECTNAME_SHARED = "mobile.shared";
	
	/** Project name for the agent project. */
	private static final String PROJECTNAME_AGENT = "mobile.agent";
	
	/** Project name for the iObserve common porject. */
	private static final String PROJECTNAME_COMMON = "common";

	/** Path to the agent files. */
	private static final File AGENT_CURRENT = new File("../" + PROJECTNAME_AGENT + "/build/classes/main");

	/** Path to the agent shared files. */
	private static final File AGENT_SHARED = new File("../" + PROJECTNAME_SHARED + "/build/classes/main");

	/** Path to the common project records. */
	private static final File AGENT_RECORDS_COMMON = new File("../" + PROJECTNAME_COMMON + "/build/classes/main");

	/** Path to the libs used by the agent. */
	private static final File AGENT_LIB_CURRENT = new File("../" + PROJECTNAME_AGENT + "/build/libs");

	/** Path to build the agent libs. */
	private static final File AGENT_LIB_BUILD_TEMP = new File(AGENT_LIB_CURRENT.getAbsolutePath() + "/temp/");

	/** Path which is used to save a modified manifest file. */
	private static final File MODIFIED_MANIFEST = new File("modified_manifest.xml");

	/** Byte to Megabyte factor. */
	private static final double MB_FACTOR = 1024d * 1024d;

	/** Flags if we override an existing output or not. */
	private boolean override;

	/** Path to the keystore used to resign the application. */
	private File keystore;

	/** Alias of the keystore. */
	private String alias;

	/** Password for the keystore. */
	private String pass;

	/** Permissions which are needed by the agent. */
	private List<String> neededRights;

	/** Flags whether to build the agent or not. */
	private boolean buildAgent = true;

	/** Flags whether to cleanup before instrumentation. */
	private boolean cleanBefore = true;

	/** Flags whether to cleanup after instrumentation. */
	private boolean cleanAfter = true;

	/** Flags whether to adjust the manifest or not. (recommended) */
	private boolean adjustManifest = true;

	/**
	 * Creates a new instance for instrumenting Android applications.
	 * 
	 * @param override
	 *            whether to override existing files or not
	 * @param keystore
	 *            the path to the keystore
	 * @param alias
	 *            the alias of the keystore
	 * @param pass
	 *            the password of the keystore
	 */
	public APKInstrumenter(final boolean override, final File keystore, final String alias, final String pass) {
		this.setOverride(override);
		this.setKeystore(keystore);
		this.setAlias(alias);
		this.setPass(pass);
	}

	/**
	 * Instruments an android application.
	 * 
	 * @param input
	 *            the input application
	 * @param output
	 *            the path where to save the instrumented application
	 * @return true if success - false otherwise
	 * @throws IOException
	 *             if there is an I/O problem
	 * @throws ZipException
	 *             if zip4j can't zip/unzip your application files
	 * @throws KeyStoreException
	 *             if there is a problem with the used keystore
	 * @throws NoSuchAlgorithmException
	 *             algorithm not found
	 * @throws CertificateException
	 *             certificate error
	 * @throws URISyntaxException
	 *             URI syntax problem
	 */
	public boolean instrumentAPK(final File input, final File output) throws IOException, ZipException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, URISyntaxException {
		// CHECK IF INPUT EXISTS AND OUTPUT DOESNT
		if (!input.exists() || (output.exists() && !override)) {
			return false;
		}

		// LOAD CONFIGURATION
		final InstrumentationConfiguration instrConfig = new InstrumentationConfiguration();
		final File instrConfigFile = new File(APKInstrumenter.class.getResource("/config/default.xml").toURI());
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
			cleanUpAll();
		}

		// ZIP CURRENT AGENT VERSION
		if (buildAgent) {
			buildAgentInner(instrConfig);
		}

		// ADD MANIFEST ADJUSTMENTS
		if (adjustManifest) {
			LOG.info("Decoding application with APKTool.");

			final APKToolProxy apkTool = new APKToolProxy(input);
			final boolean b1 = apkTool.decodeAPK("intermediate");
			final boolean b2 = apkTool.adjustManifest(neededRights, MODIFIED_MANIFEST);

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

		if (!AGENT_BUILD.exists()) {
			return false;
		}

		// COPY INPUT TO OUTPUT
		final File tempOutputFolder = OUTPUT_TEMPO;
		tempOutputFolder.mkdir();
		final ZipFile tempInputZip = new ZipFile(input);
		tempInputZip.extractAll(tempOutputFolder.getAbsolutePath() + "/");

		LOG.info("Created copy of original APK file.");

		// TEMP WRITE DEX
		final File tempDex = TEMP_DEX_OLD;
		unzipDex(input, tempDex);

		LOG.info("Searched and extracted dex files.");

		// DEX 2 JAR
		LOG.info("Executing dex2jar to transform DEX files to JAR files.");
		final File tempJar = new File("temp-jar.jar");
		Dex2JarProxy.createJarFromDex(tempDex, tempJar);
		LOG.info("Dex2jar finished transformation.");

		// UNZIP JAR
		final File jarFilesContainer = OUTPUT_TEMP;
		jarFilesContainer.mkdir();

		final ZipFile jarZip = new ZipFile(tempJar);
		jarZip.extractAll(jarFilesContainer.getAbsolutePath() + "/");

		LOG.info("Extracted JAR file for accessing class files.");

		LOG.info("Started instrumentation of class files.");
		final ClassFileCollector cfColl = new ClassFileCollector(jarFilesContainer);
		cfColl.collectClassFiles();

		final BytecodeInstrumentationManager manager = new BytecodeInstrumentationManager(cfColl.getClassFiles());
		manager.executeInstrumentation(instrConfig);
		LOG.info("Successfully instrumented class files.");

		// INJECT AGENT
		LOG.info("Injecting agent files into rezipped JAR.");
		final ZipFile agentZipfile = new ZipFile(AGENT_BUILD);
		agentZipfile.extractAll(jarFilesContainer.getAbsolutePath() + "/");
		LOG.info("Agent has been injected.");

		// CREATE ZIP
		LOG.info("Rebuilding JAR file from modified class files.");
		final File tempRezipFile = rebuildJar(jarFilesContainer);
		LOG.info("Finished building new JAR file.");

		// JAR 2 DEX
		final File tempNewDex = TEMP_DEX_NEW;
		LOG.info("Executing jar2dex to transform new JAR file to DEX.");
		Dex2JarProxy.createDexFromJar(tempRezipFile, tempNewDex);
		LOG.info("jar2dex has been successfully executed.");

		// EDIT OLD classes.dex and remove META INF
		LOG.info("Removing old signature.");
		final File oldMeta = new File(tempOutputFolder.getAbsolutePath() + "/META-INF");
		FileUtils.deleteDirectory(oldMeta);

		final File oClasses = new File(tempOutputFolder.getAbsolutePath() + "/classes.dex");
		oClasses.delete();
		Files.copy(tempNewDex.toPath(), oClasses.toPath());

		// MODIFY MANIFEST
		if (adjustManifest) {
			LOG.info("Adjusting Android manifest.");
			final File oManifest = new File(tempOutputFolder.getAbsolutePath() + "/AndroidManifest.xml");
			oManifest.delete();
			Files.copy(MODIFIED_MANIFEST.toPath(), oManifest.toPath());
		}

		// REZIP
		LOG.info("Rezip all files to output APK.");
		rezipOutput(output, tempOutputFolder);

		// RESIGN
		LOG.info("Resigning the output APK.");
		final JarSigner signer = new JarSigner();
		signer.signJar(output, getKeystore(), getAlias(), getPass());

		// REMOVE ALL TEMP FILES
		if (cleanAfter) {
			cleanUpAll();
		}

		LOG.info("Instrumentation finished.");
		LOG.info("Instrumented application is located at '" + output.getAbsolutePath() + "'.");

		return true;
	}

	/**
	 * @return the override
	 */
	public boolean isOverride() {
		return override;
	}

	/**
	 * @param override
	 *            the override to set
	 */
	public void setOverride(final boolean override) {
		this.override = override;
	}

	/**
	 * @return the keystore
	 */
	public File getKeystore() {
		return keystore;
	}

	/**
	 * @param keystore
	 *            the keystore to set
	 */
	public void setKeystore(final File keystore) {
		this.keystore = keystore;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(final String alias) {
		this.alias = alias;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass
	 *            the pass to set
	 */
	public void setPass(final String pass) {
		this.pass = pass;
	}

	/**
	 * @return the buildAgent
	 */
	public boolean isBuildAgent() {
		return buildAgent;
	}

	/**
	 * @param buildAgent
	 *            the buildAgent to set
	 */
	public void setBuildAgent(final boolean buildAgent) {
		this.buildAgent = buildAgent;
	}

	/**
	 * @return the cleanBefore
	 */
	public boolean isCleanBefore() {
		return cleanBefore;
	}

	/**
	 * @param cleanBefore
	 *            the cleanBefore to set
	 */
	public void setCleanBefore(final boolean cleanBefore) {
		this.cleanBefore = cleanBefore;
	}

	/**
	 * @return the cleanAfter
	 */
	public boolean isCleanAfter() {
		return cleanAfter;
	}

	/**
	 * @param cleanAfter
	 *            the cleanAfter to set
	 */
	public void setCleanAfter(final boolean cleanAfter) {
		this.cleanAfter = cleanAfter;
	}

	/**
	 * Builds the agent with all class files and libraries.
	 * 
	 * @param instrConfig
	 *            the configuration of the instrumentation
	 * @throws IOException
	 *             if not all files could be resolved
	 * @throws ZipException
	 *             if zipping to agent build fails
	 */
	private void buildAgentInner(final InstrumentationConfiguration instrConfig) throws IOException, ZipException {
		LOG.info("Bundling current agent version.");
		final File agentZipOld = AGENT_BUILD;
		agentZipOld.delete();
		FileUtils.deleteDirectory(AGENT_LIB_BUILD_TEMP);

		final ZipFile agentZipNew = new ZipFile(AGENT_BUILD);

		// TO NOT ADD INCONSISTENT FOLDERS
		FileUtils.copyDirectory(AGENT_SHARED, AGENT_CURRENT);
		FileUtils.copyDirectory(AGENT_RECORDS_COMMON, AGENT_CURRENT);

		final File[] libs = AGENT_LIB_CURRENT.listFiles();
		for (File lib : libs) {
			if (lib.isFile() && !lib.getName().contains("org.iobserve.mobile.agent")) {
				final ZipFile zipF = new ZipFile(lib);
				zipF.extractAll(AGENT_LIB_BUILD_TEMP.getAbsolutePath());
			}
		}

		final File toAdd = AGENT_LIB_BUILD_TEMP;
		final List<String> includedFolders = instrConfig.getXmlConfiguration().getAgentBuildConfiguration()
				.getLibraryFolders();

		for (String includedFolder : includedFolders) {
			final File[] toAddArray = new File(toAdd.getAbsolutePath() + "/" + includedFolder).listFiles();
			if (toAddArray != null) {
				final File containingFolder = new File(AGENT_CURRENT.getAbsolutePath() + "/" + includedFolder);
				if (!containingFolder.exists()) {
					containingFolder.mkdir();
				}
				for (File ff : toAddArray) {
					if (ff.isDirectory()) {
						FileUtils.copyDirectoryToDirectory(ff, containingFolder);
					} else {
						FileUtils.copyFile(ff, new File(containingFolder.getAbsolutePath() + "/" + ff.getName()));
					}
				}
			}
		}

		addFolderToZipNoParent(agentZipNew, AGENT_CURRENT);

		LOG.info("Bundled agent with an actual size of " + round(agentZipNew.getFile().length() / MB_FACTOR) + "MB.");
	}

	/**
	 * Creates a jar file from a folder which contains the bytecode files.
	 * 
	 * @param jarFilesContainer
	 *            folder which contains bytecode files
	 * @return path to the created jar file
	 * @throws ZipException
	 *             if zip4j fails
	 */
	private File rebuildJar(final File jarFilesContainer) throws ZipException {
		this.rezipOutput(JAR_REZIP, jarFilesContainer);

		return JAR_REZIP;
	}

	/**
	 * Rezips a folder to a zip file.
	 * 
	 * @param output
	 *            the zip file to create
	 * @param tempOutputFolder
	 *            the folder which should be zipped
	 * @throws ZipException
	 *             if zip4j fails
	 */
	private void rezipOutput(final File output, final File tempOutputFolder) throws ZipException {
		final ZipFile zipOutput = new ZipFile(output);
		addFolderToZipNoParent(zipOutput, tempOutputFolder);
	}

	/**
	 * Adds a folder to a zip file without the parent folder.
	 * 
	 * @param zip
	 *            the zip file
	 * @param folder
	 *            the folder to add
	 * @throws ZipException
	 *             if there is an I/O problem
	 */
	private void addFolderToZipNoParent(final ZipFile zip, final File folder) throws ZipException {
		final ZipParameters parameters = new ZipParameters();
		for (File fInner : folder.listFiles()) {
			if (fInner.isDirectory()) {
				zip.addFolder(fInner, parameters);
			} else {
				zip.addFile(fInner, parameters);
			}
		}
	}

	/**
	 * Cleans all folders and files temporary used for instrumentation.
	 * 
	 * @throws IOException
	 *             if not all files and folders could be removed successfully
	 */
	private void cleanUpAll() throws IOException {
		LOG.info("Cleaning up all folders.");

		FileUtils.deleteDirectory(OUTPUT_TEMP);
		FileUtils.deleteDirectory(OUTPUT_TEMPO);

		TEMP_JAR.delete();
		TEMP_DEX_NEW.delete();
		JAR_REZIP.delete();
		TEMP_DEX_OLD.delete();
		MODIFIED_MANIFEST.delete();
	}

	/**
	 * Extracts only a the classes.dex file from an Android application.
	 * 
	 * @param apk
	 *            the Android application
	 * @param dex
	 *            the path where the dex should be saved
	 * @throws ZipException
	 *             if zip4j fails
	 * @throws IOException
	 *             if there is an I/O problem
	 */
	private void unzipDex(final File apk, final File dex) throws ZipException, IOException {
		if (apk.exists() && !dex.exists()) {
			final ZipFile parent = new ZipFile(apk);

			@SuppressWarnings("unchecked")
			final List<FileHeader> headerList = parent.getFileHeaders();

			for (FileHeader header : headerList) {
				if (header.getFileName().equals("classes.dex")) {
					final ZipInputStream in = parent.getInputStream(header);
					final FileOutputStream os = new FileOutputStream(dex);
					int readLen = -1;
					final byte[] buff = new byte[4096];
					while ((readLen = in.read(buff)) != -1) {
						os.write(buff, 0, readLen);
					}
					closeStreams(in, os);
					break;
				}
			}
		}
	}

	/**
	 * Closes two input streams.
	 * 
	 * @param a
	 *            a zip input stream
	 * @param b
	 *            a file output stream
	 * @throws IOException
	 *             if one of the streams can't be closed
	 */
	private void closeStreams(final ZipInputStream a, final FileOutputStream b) throws IOException {
		if (a != null) {
			a.close();
		}

		if (b != null) {
			b.close();
		}
	}

	/**
	 * Rounds a double to two decimal places.
	 * 
	 * @param a
	 *            input double
	 * @return rounded double
	 */
	private double round(final double a) {
		return Math.round(a * 100d) / 100d;
	}
}
