package org.iobserve.analysis.constraint.analysis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.iobserve.analysis.constraint.model.ConstraintModel;
import org.iobserve.analysis.constraint.model.PCMConstraint;
import org.iobserve.analysis.constraint.model.ProvideExpression;
import org.iobserve.analysis.constraint.model.types.AbstractTargetConstraint;

public class PCMAnalyzer {

	private PalladioInstance target;

	public PCMAnalyzer(PalladioInstance target) {
		this.target = target;
	}

	public List<ConstraintViolation> analyze(ConstraintModel ruleset) {
		List<ConstraintViolation> violations = new ArrayList<>();

		// load providings
		Map<String, Object[]> varPCMMapping = new HashMap<>();
		Map<String, ProvideExpression> provideMapping = ruleset.getPcmProvider().getProvideMapping();

		for (String provideKey : provideMapping.keySet()) {
			ProvideExpression expr = provideMapping.get(provideKey);
			String[] pathSplit = expr.getPcmPath().split("->");
			varPCMMapping.put(expr.getName(), providePath(pathSplit));
		}

		// check constraints
		for (PCMConstraint constraint : ruleset.getConstraints()) {
			if (varPCMMapping.containsKey(constraint.getTarget().getName())) {
				Object[] toVerify = varPCMMapping.get(constraint.getTarget().getName());
				for (AbstractTargetConstraint childs : constraint.getConcreteConstraints()) {
					for (Object obj : toVerify) {
						if (!childs.verify(obj)) {
							System.err.println("Found constraint violation at object '" + obj.toString()
									+ "' with constraint name '" + constraint.getName() + "'.");
							ConstraintViolation violation = new ConstraintViolation();
							violation.setConstraintName(constraint.getName());
							violation.setLocation(obj.toString());

							violations.add(violation);
						}
					}
				}
			} else {
				System.err.println("Couldn't find target with name '" + constraint.getTarget().getName() + "'.");
			}
		}

		return violations;
	}

	// REFLECTION
	private Object[] providePath(String[] pathComponents) {
		String basePath = pathComponents[0];
		Object base;
		switch (basePath) {
		case "ResourceEnvironment":
			base = target.environment;
			break;
		default:
			base = null;
		}

		if (base == null) {
			return null;
		}

		if (pathComponents.length == 1) {
			return new Object[] { base };
		}

		return provideRecursive(base, Arrays.copyOfRange(pathComponents, 1, pathComponents.length));

	}

	private Object[] provideRecursive(Object base, String[] components) {
		String co = components[0];

		EList<?> childs = this.searchWithReflection(base, co.startsWith("!") ? co.substring(1) : co,
				co.startsWith("!"));

		if (components.length == 1) {
			Object[] a = new Object[childs.size()];
			childs.toArray(a);
			return a;
		} else {
			Object[] a = new Object[childs.size()];
			childs.toArray(a);

			Object[] c = new Object[0];
			for (Object b : a) {
				c = (Object[]) ArrayUtils.addAll(c,
						this.provideRecursive(b, Arrays.copyOfRange(components, 1, components.length)));
			}

			return c;
		}
	}

	private EList<?> searchWithReflection(Object parent, String type, boolean isMethodName) {
		Method[] methods = parent.getClass().getMethods();
		for (Method method : methods) {
			if (isMethodName) {
				if (method.getName().equals(type)) {
					try {
						Object result = method.invoke(parent);
						if (result instanceof EList<?>) {
							return (EList<?>) result;
						}
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
						return null;
					}
				}
			} else {
				if (method.getName().startsWith("get") && method.getReturnType().getSimpleName().equals(type)) {
					EList<Object> singleItemList = new BasicEList<>();
					try {
						singleItemList.add(method.invoke(parent));
						return singleItemList;
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
						return null;
					}
				}
			}
		}

		return null;
	}

}
