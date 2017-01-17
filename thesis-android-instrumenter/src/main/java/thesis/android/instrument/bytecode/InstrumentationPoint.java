package thesis.android.instrument.bytecode;

public class InstrumentationPoint {
	private String className;
	private String methodName;
	private String description;

	private boolean isSuper;
	private boolean isInterface;

	public InstrumentationPoint(String className, String methodName, String description) {
		this(className, methodName, description, false, false);
	}

	public InstrumentationPoint(String className, String methodWithDescription) {
		this(className, methodWithDescription, null);

		String[] sp = methodWithDescription.split("\\(");
		if (sp.length == 2) {
			setMethodName(sp[0]);
			setDescription("(" + sp[1]);
		}
	}

	public InstrumentationPoint(String className, String methodName, String description, boolean sup, boolean inter) {
		this.description = description;
		this.className = className;
		this.methodName = methodName;
		this.isSuper = sup;
		this.isInterface = inter;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public boolean isSuper() {
		return isSuper;
	}

	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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
