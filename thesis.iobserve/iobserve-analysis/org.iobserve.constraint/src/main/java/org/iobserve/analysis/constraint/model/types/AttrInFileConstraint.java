package org.iobserve.analysis.constraint.model.types;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttrInFileConstraint extends AttrInConstraint {

	private static final Pattern PATTERN = Pattern.compile("(.*?)\\s*in\\s*\\[(.*?)\\]");

	@Override
	public void load(String info) {
		Matcher matcher = PATTERN.matcher(info);
		if (matcher.find()) {
			attributeName = this.parseString(matcher.group(1));
			valueList = new ArrayList<>();

			String fileName = matcher.group(2);
			File valueFile = new File(fileName);
			if (valueFile.exists()) {
				try {
					byte[] encoded = Files.readAllBytes(valueFile.toPath());
					String data = new String(encoded, "UTF-8");

					for (String val : data.split(";")) {
						valueList.add(val);
					}
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("Couldn't read file '" + fileName + "'.");
				}
			} else {
				System.err.println("Couldn't find file '" + fileName + "'.");
			}
		}
	}

}
