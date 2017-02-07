package thesis.inspectit.bridge.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Implements a simple session storage with a {@link HashMap}.
 * 
 * @author David Monschein
 * @author Robert Heinrich
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
	public boolean exists(String id) {
		return dataMapping.containsKey(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String create(T data) {
		String id = UUID.randomUUID().toString();
		dataMapping.put(id, data);
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get(String id) {
		return dataMapping.get(id);
	}

}
