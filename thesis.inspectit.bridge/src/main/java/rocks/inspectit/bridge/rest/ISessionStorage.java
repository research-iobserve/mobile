package rocks.inspectit.bridge.rest;

public interface ISessionStorage<T> {
	public boolean exists(String id);

	public String create(T data);

	public T get(String id);
}
