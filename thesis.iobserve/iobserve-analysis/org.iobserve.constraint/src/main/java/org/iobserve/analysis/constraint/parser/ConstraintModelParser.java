package org.iobserve.analysis.constraint.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;
import org.iobserve.analysis.constraint.model.ConstraintModel;
import org.iobserve.analysis.constraint.model.PCMConstraint;
import org.iobserve.analysis.constraint.model.PCMProvider;
import org.iobserve.analysis.constraint.model.ProvideExpression;
import org.iobserve.analysis.constraint.model.types.AbstractTargetConstraint;
import org.iobserve.analysis.constraint.model.types.AttrInConstraint;
import org.iobserve.analysis.constraint.model.types.AttrInFileConstraint;

public class ConstraintModelParser {

	private static final Pattern PROVIDE_PATTERN = Pattern.compile("provide\\s*\\{(.*?)\\}", Pattern.DOTALL);
	private static final Pattern CONSTRAINT_PATTERN = Pattern.compile("constraint\\s*?\\[(.*?)\\]\\s*\\{(.*?)\\}",
			Pattern.DOTALL);

	private static final Pattern PAIR_PATTERN = Pattern.compile("(.*?)\\s*\\:\\s*(.*)");
	private static final Pattern STRING_PATTERN = Pattern.compile("\"(.*?)\"");
	private static final Pattern ARRAY_PATTERN = Pattern.compile("\\[(.*?)\\]");

	private static final Map<String, Class<? extends AbstractTargetConstraint>> CONSTRAINT_TYPE_MAPPING;

	static {
		CONSTRAINT_TYPE_MAPPING = new HashMap<>();
		CONSTRAINT_TYPE_MAPPING.put("attr_in", AttrInConstraint.class);
		CONSTRAINT_TYPE_MAPPING.put("attr_in_file", AttrInFileConstraint.class);
	}

	public ConstraintModelParser() {
	}

	public ConstraintModel parseModel(String model) {
		Matcher provideMatcher = PROVIDE_PATTERN.matcher(model);
		Matcher constraintMatcher = CONSTRAINT_PATTERN.matcher(model);

		PCMProvider provider;
		if (provideMatcher.find()) {
			provider = new PCMProvider();
			Map<String, ProvideExpression> mapping = provider.getProvideMapping();

			String expression = provideMatcher.group(1);
			String[] seperation = expression.split(",");
			for (String item : seperation) {
				Pair<String, String> keyValuePair = formatPair(item);
				ProvideExpression expr = new ProvideExpression();
				expr.setName(keyValuePair.getLeft().substring(1));

				Matcher arrayMatcher = ARRAY_PATTERN.matcher(keyValuePair.getRight());
				if (arrayMatcher.find()) {
					expr.setPcmPath(arrayMatcher.group(1));

					mapping.put(keyValuePair.getLeft(), expr);
				}
			}
		} else {
			System.err.println("Couldn't find the PCM provider statement.");
			return null;
		}

		List<PCMConstraint> constraints = new ArrayList<>();
		while (constraintMatcher.find()) {
			String target = constraintMatcher.group(1);
			String expression = constraintMatcher.group(2);

			Map<String, ProvideExpression> providing = provider.getProvideMapping();
			if (providing.containsKey(target)) {
				PCMConstraint constraint = new PCMConstraint();
				constraint.setTarget(providing.get(target));

				List<AbstractTargetConstraint> concreteConstraints = new ArrayList<>();

				String[] seperation = expression.split(";");
				for (String item : seperation) {
					Pair<String, String> keyVal = formatPair(item);

					String key = keyVal.getLeft();
					if (key.equalsIgnoreCase("name")) {
						constraint.setName(parseString(keyVal.getRight()));
					} else {
						if (CONSTRAINT_TYPE_MAPPING.containsKey(key)) {
							Class<? extends AbstractTargetConstraint> constraintClass = CONSTRAINT_TYPE_MAPPING
									.get(key);
							try {
								AbstractTargetConstraint tconstr = constraintClass.newInstance();
								tconstr.load(keyVal.getRight());

								concreteConstraints.add(tconstr);
							} catch (InstantiationException | IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					}
				}

				constraint.setConcreteConstraints(concreteConstraints);
				constraints.add(constraint);
			} else {
				System.err.println("Couldn't find target '" + target + "'.");
			}
		}

		ConstraintModel dmodel = new ConstraintModel();
		dmodel.setConstraints(constraints);
		dmodel.setPcmProvider(provider);

		return dmodel;
	}

	public ConstraintModel parseModel(File file) throws IOException {
		byte[] encoded = Files.readAllBytes(file.toPath());
		String data = new String(encoded, "UTF-8");

		return this.parseModel(data);
	}

	private Pair<String, String> formatPair(String pair) {
		Matcher pairMatcher = PAIR_PATTERN.matcher(pair.trim());
		if (pairMatcher.find()) {
			return Pair.of(pairMatcher.group(1), pairMatcher.group(2));
		} else {
			return null;
		}
	}

	private String parseString(String string) {
		Matcher matcher = STRING_PATTERN.matcher(string);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}

}
