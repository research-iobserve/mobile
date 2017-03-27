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
package org.iobserve.mobile.server.rest;

/**
 * Interface for a simple session storage.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 * @param <T>
 *            the type of the value which is assigned to a session
 */
public interface ISessionStorage<T> {
	/**
	 * Determines whether a specified id exists or not.
	 * 
	 * @param id
	 *            the id to check
	 * @return true if it exists - false otherwise
	 */
	public boolean exists(String id);

	/**
	 * Creates a new session and returns the generated session id.
	 * 
	 * @param data
	 *            the data which should be assigned to the session
	 * @return the generated session id
	 */
	public String create(T data);

	/**
	 * Gets the value which is assigned to a certain session.
	 * 
	 * @param id
	 *            the id of the session
	 * @return the value which is assigned to the passed session id
	 */
	public T get(String id);

	/**
	 * Closes a session with a given session id.
	 * 
	 * @param id
	 *            the id of the session which should be removed.
	 */
	public void close(String id);
}
