package rocks.inspectit.android.util;

/**
 * Created by David on 26.10.16.
 */

public class CacheValue<T> {

	private T value;
	private long timestamp;
	private long validity;
	private boolean ever;

	public CacheValue(T val, long validity) {
		this.set(val);
		this.validity = validity;
		this.ever = false;
	}

	public CacheValue(long val) {
		this.timestamp = 0;
		this.validity = val;
		this.ever = false;
	}

	public CacheValue() {
		this.timestamp = 0;
		this.ever = true;
	}

	public boolean valid() {
		return ever || System.currentTimeMillis() - timestamp <= this.validity;
	}

	public T value() {
		return value;
	}

	public T set(T val) {
		this.value = val;
		this.timestamp = System.currentTimeMillis();
		return val;
	}

}
