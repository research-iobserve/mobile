package org.iobserve.analysis.constraint.model.types;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractTargetConstraint {
	private static final Pattern STRING_PATTERN = Pattern.compile("\"(.*?)\"");

	public abstract void load(String info);

	public abstract boolean verify(Object instance);

	protected String parseString(String string) {
		Matcher matcher = STRING_PATTERN.matcher(string.trim());
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}

	protected Object resolvePCMFieldValue(Object target, String attr) {
		Method meth = resolveGetMethod(target, attr);
		if (meth != null) {
			try {
				return meth.invoke(target);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private Method resolveGetMethod(Object target, String attr) {
		for (Method method : target.getClass().getMethods()) {
			if (method.getName().startsWith("get") && method.getName().substring(3).equalsIgnoreCase(attr)) {
				return method;
			}
		}

		return null;
	}
}
