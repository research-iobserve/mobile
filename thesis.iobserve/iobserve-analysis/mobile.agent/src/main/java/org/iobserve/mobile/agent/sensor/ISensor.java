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
package org.iobserve.mobile.agent.sensor;

import org.iobserve.mobile.agent.callback.CallbackManager;

/**
 * Interface for the sensor concept of the agent.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public interface ISensor {
	/**
	 * Is executed before the original method gets executed.
	 */
	void beforeBody(long id);

	/**
	 * Gets executed if the real method throws an exception.
	 * 
	 * @param clazz
	 *            the class of the exception
	 */
	void exceptionThrown(long id, String clazz);

	/**
	 * Gets executed after the original method was executed.
	 */
	void firstAfterBody(long id);

	/**
	 * Gets executed after the original method and the
	 * {@link ISensor#firstAfterBody()} were executed.
	 */
	void secondAfterBody(long id);

	/**
	 * Sets the name of the class which owns the method.
	 * 
	 * @param owner
	 *            name of the class which owns the method
	 */
	void setOwner(long id, String owner);

	/**
	 * Sets the signature of the method for which this sensor is responsible.
	 * 
	 * @param methodSignature
	 *            the signature of the method
	 */
	void setSignature(long id, String methodSignature);

	/**
	 * Sets the callback manager for the sensor.
	 * 
	 * @param callbackManager
	 *            the {@link CallbackManager}
	 */
	void setCallbackManager(CallbackManager callbackManager);
}
