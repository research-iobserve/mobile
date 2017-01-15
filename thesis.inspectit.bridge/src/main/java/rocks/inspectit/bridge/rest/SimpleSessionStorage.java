package rocks.inspectit.bridge.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleSessionStorage<T> implements ISessionStorage<T> {

	private Map<String, T> dataMapping;

	public SimpleSessionStorage() {
		dataMapping = new HashMap<>();
	}

	@Override
	public boolean exists(String id) {
		return dataMapping.containsKey(id);
	}

	@Override
	public String create(T data) {
		String id = UUID.randomUUID().toString();
		dataMapping.put(id, data);
		return id;
	}

	@Override
	public T get(String id) {
		return dataMapping.get(id);
	}

}
