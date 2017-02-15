package thesis.android.instrument.bytecode;

/**
 * Class which specifies a certain instrumentation point.
 * 
 * @author David Monschein
 *
 */
public class InstrumentationPoint {
	/**
	 * Name of the class.
	 */
	private String className;

	/**
	 * Name of the method.
	 */
	private String methodName;

	/**
	 * Signature of the method.
	 */
	private String description;

	/**
	 * Whether the class is the super class.
	 */
	private boolean isSuper;

	/**
	 * Whether the class is the interface.
	 */
	private boolean isInterface;

	/**
	 * Creates an instance.
	 * 
	 * @param className
	 *            name of the class
	 * @param methodName
	 *            name of the method
	 * @param description
	 *            description of the method
	 */
	public InstrumentationPoint(String className, String methodName, String description) {
		this(className, methodName, description, false, false);
	}

	/**
	 * Creates a method out of the class name and the full method signature.
	 * 
	 * @param className
	 *            name of the class
	 * @param methodWithDescription
	 *            full method signature including method name and method
	 *            signature
	 */
	public InstrumentationPoint(String className, String methodWithDescription) {
		this(className, methodWithDescription, null);

		String[] sp = methodWithDescription.split("\\(");
		if (sp.length == 2) {
			setMethodName(sp[0]);
			setDescription("(" + sp[1]);
		}
	}

	/**
	 * Creates a new instrumentation point.
	 * 
	 * @param className
	 *            name of the class
	 * @param methodName
	 *            name of the method
	 * @param description
	 *            description of the method
	 * @param sup
	 *            whether superclass or not
	 * @param inter
	 *            whether interface or not
	 */
	public InstrumentationPoint(String className, String methodName, String description, boolean sup, boolean inter) {
		this.description = description;
		this.className = className;
		this.methodName = methodName;
		this.isSuper = sup;
		this.isInterface = inter;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param methodName
	 *            the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the isSuper
	 */
	public boolean isSuper() {
		return isSuper;
	}

	/**
	 * @param isSuper
	 *            the isSuper to set
	 */
	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}

	/**
	 * @return the isInterface
	 */
	public boolean isInterface() {
		return isInterface;
	}

	/**
	 * @param isInterface
	 *            the isInterface to set
	 */
	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (isInterface ? 1231 : 1237);
		result = prime * result + (isSuper ? 1231 : 1237);
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstrumentationPoint other = (InstrumentationPoint) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (isInterface != other.isInterface)
			return false;
		if (isSuper != other.isSuper)
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		return true;
	}

}
