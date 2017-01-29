package rocks.inspectit.android.util;

/**
 * Created by David on 26.10.16.
 */

public class CacheValue<T> {

	private T value;
	private long timestamp;
	private long validity;

	private boolean ever;
	private boolean inited;

	public CacheValue(T val, long validity) {
		this.set(val);
		this.validity = validity;
		this.ever = false;
		this.inited = true;
	}

	public CacheValue(long val) {
		this.timestamp = 0;
		this.validity = val;
		this.ever = false;
		this.inited = false;
	}

	public CacheValue() {
		this.timestamp = 0;
		this.ever = true;
		this.inited = false;
	}

	public boolean valid() {
		return (ever && inited) || System.currentTimeMillis() - timestamp <= this.validity;
	}

	public T value() {
		return value;
	}

	public T set(T val) {
		this.inited = true;
		this.value = val;
		this.timestamp = System.currentTimeMillis();
		return val;
	}

}
