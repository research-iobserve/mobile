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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Implements a simple session storage with a {@link HashMap}.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 * @param <T>
 *            the type of the value which is assigned to a session
 */
public class SimpleSessionStorage<T> implements ISessionStorage<T> {

	/**
	 * Maps session id's to corresponding values.
	 */
	private Map<String, T> dataMapping;

	/**
	 * Creates a new session storage which an empty mapping.
	 */
	public SimpleSessionStorage() {
		dataMapping = new HashMap<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exists(final String id) {
		return dataMapping.containsKey(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String create(final T data) {
		final String id = UUID.randomUUID().toString();
		dataMapping.put(id, data);
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get(final String id) {
		return dataMapping.get(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close(final String id) {
		dataMapping.remove(id);
	}

}
