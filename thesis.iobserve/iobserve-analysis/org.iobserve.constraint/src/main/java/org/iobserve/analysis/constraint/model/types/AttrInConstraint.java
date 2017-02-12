package org.iobserve.analysis.constraint.model.types;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttrInConstraint extends AbstractTargetConstraint {

	private static final Pattern PATTERN = Pattern.compile("(.*?)\\s*in\\s*\\[(.*?)\\]");

	protected String attributeName;
	protected List<String> valueList;

	@Override
	public boolean verify(Object instance) throws ConstraintViolationException {
		Object value = resolvePCMFieldValue(instance, attributeName);

		if (value != null) {
			// check
			if (value instanceof String) {
				if (!_contains(valueList, (String) value, true)) {
					throw new ConstraintViolationException("'" + ((String) value) + "' isn't in the list of values.");
				}
			} else {
				if (!_contains(valueList, String.valueOf(value), true)) {
					throw new ConstraintViolationException(
							"'" + String.valueOf(value) + "' isn't in the list of values.");
				}
			}
		}
		return true;
	}

	@Override
	public void load(String info) {
		Matcher matcher = PATTERN.matcher(info);
		if (matcher.find()) {
			attributeName = this.parseString(matcher.group(1));
			valueList = new ArrayList<>();

			String[] values = matcher.group(2).split(",");
			for (String val : values) {
				valueList.add(this.parseString(val));
			}
		}
	}

	/**
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attributeName
	 *            the attributeName to set
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	/**
	 * @return the valueList
	 */
	public List<String> getValueList() {
		return valueList;
	}

	/**
	 * @param valueList
	 *            the valueList to set
	 */
	public void setValueList(List<String> valueList) {
		this.valueList = valueList;
	}

	private boolean _contains(List<String> list, String v, boolean ignoreCase) {
		for (String s : list) {
			if (ignoreCase) {
				if (s.equalsIgnoreCase(v)) {
					return true;
				}
			} else {
				if (s.equals(v)) {
					return true;
				}
			}
		}
		return false;
	}

}
