package thesis.inspectit.bridge.rest;

/**
 * Interface for a simple session storage.
 * 
 * @author David Monschein
 * @author Robert Heinrich
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
}
