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
package org.iobserve.mobile.instrument.bytecode;

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
	public InstrumentationPoint(final String className, final String methodName, final String description) {
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
	public InstrumentationPoint(final String className, final String methodWithDescription) {
		this(className, methodWithDescription, null);

		final String[] sp = methodWithDescription.split("\\(");
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
	public InstrumentationPoint(final String className, final String methodName, final String description,
			final boolean sup, final boolean inter) {
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
	public void setClassName(final String className) {
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
	public void setMethodName(final String methodName) {
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
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the isSuper
	 */
	public boolean isSuper() {
		return isSuper;
	}

	/**
	 * @param izSuper
	 *            the isSuper to set
	 */
	public void setSuper(final boolean izSuper) {
		this.isSuper = izSuper;
	}

	/**
	 * @return the isInterface
	 */
	public boolean isInterface() {
		return isInterface;
	}

	/**
	 * @param izInterface
	 *            the isInterface to set
	 */
	public void setInterface(final boolean izInterface) {
		this.isInterface = izInterface;
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
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final InstrumentationPoint other = (InstrumentationPoint) obj;
		if (className == null) {
			if (other.className != null) {
				return false;
			}
		} else if (!className.equals(other.className)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (isInterface != other.isInterface) {
			return false;
		}
		if (isSuper != other.isSuper) {
			return false;
		}
		if (methodName == null) {
			if (other.methodName != null) {
				return false;
			}
		} else if (!methodName.equals(other.methodName)) {
			return false;
		}
		return true;
	}

}
