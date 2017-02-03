package thesis.android.instrument.core;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class APKToolProxy {

	private static final Logger LOG = LogManager.getLogger(APKToolProxy.class);

	private static final String LIB_PATH = "lib\\apktool_2.2.1.jar";
	private static final String MANIFEST_FILE = "AndroidManifest.xml";

	private File input;
	private String folderName;

	private String packageName;

	public APKToolProxy(File inputAPK) {
		this.input = inputAPK;
		this.folderName = null;
		this.packageName = null;
	}

	public boolean decodeAPK(String folderName) {
		this.folderName = folderName;
		// apktool d bar.apk -o baz
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", LIB_PATH, "d", input.getAbsolutePath(), "-o",
				folderName);

		try {
			pb.start().waitFor();
			return true;
		} catch (InterruptedException | IOException e) {
			LOG.error("Failed to execute APKTool to decode application.");
			return false;
		}
	}

	public boolean adjustManifest(List<String> rights, File modifiedManifest) {
		if (folderName == null)
			return false;

		// read all rights
		File manifestFile = new File(folderName + "/" + MANIFEST_FILE);
		if (!manifestFile.exists())
			return false;

		Set<String> exRights = new HashSet<String>();

		// PARSING XML
		Document dom;
		// Make an instance of the DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();

			dom = db.parse(manifestFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			return false;
		}

		Node manifestNode = dom.getElementsByTagName("manifest").item(0);
		setPackageName(manifestNode.getAttributes().getNamedItem("package").getTextContent());
		NodeList permissionNodes = manifestNode.getChildNodes();

		for (int k = 0; k < permissionNodes.getLength(); k++) {
			Node permNode = permissionNodes.item(k);
			if (permNode.getNodeName().equals("uses-permission")) {
				exRights.add(permNode.getAttributes().getNamedItem("android:name").getTextContent());
			}
		}

		// determine which to add
		for (String right : rights) {
			if (!exRights.contains(right)) {
				Element nNode = dom.createElement("uses-permission");
				nNode.setAttribute("android:name", right);
				manifestNode.appendChild(nNode);
			}
		}

		// write content back to file
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(manifestFile);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			return false;
		}

		// RECOMPILE
		// apktool b bar -o new_bar.apk
		File rebuildFile = new File(folderName + "/rebuild.apk");
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", LIB_PATH, "b", folderName, "-o",
				rebuildFile.getAbsolutePath());

		pb.redirectOutput(Redirect.INHERIT);
		pb.redirectError(Redirect.INHERIT);

		try {
			pb.start().waitFor();
		} catch (InterruptedException | IOException e) {
			LOG.error("Failed to rebuild apk with APKTool.");
			return false;
		}

		// UNZIP IT
		try {
			ZipFile rebuildZip = new ZipFile(rebuildFile);
			rebuildZip.extractFile(MANIFEST_FILE, folderName + "/" + "manifest_new");
			Files.copy(new File(folderName + "/" + "manifest_new" + "/" + MANIFEST_FILE).toPath(),
					modifiedManifest.toPath());
		} catch (ZipException | IOException e) {
			LOG.error("Failed to extract the manifest from the rebuilt application.");
			return false;
		}

		return true;
	}

	public void cleanup() throws IOException {
		FileUtils.deleteDirectory(new File(folderName));
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
