package rocks.inspectit.android;

/**
 * Class for holding additional monitoring data.
 * 
 * @author David Monschein
 */
public class Tag {
	/**
	 * Name of the tag.
	 */
	private String name;

	/**
	 * Value of the tag.
	 */
	private String value;

	/**
	 * Indicates whether it is a dynamic tag or a static one.
	 */
	private boolean dynamic;

	/**
	 * Creates a new tag.
	 * 
	 * @param n
	 *            name of the tag
	 * @param v
	 *            value of the tag
	 * @param dyn
	 *            true if it is a dynamic tag - false otherwise
	 */
	public Tag(final String n, final String v, final boolean dyn) {
		this.name = n;
		this.value = v;
		this.dynamic = dyn;
	}

	/**
	 * Creates a static tag.
	 * 
	 * @param n
	 *            name of the tag
	 * @param v
	 *            value of the tag
	 */
	public Tag(final String n, final String v) {
		this(n, v, false);
	}

	/**
	 * Creates a new static tag with empty name and empty value.
	 */
	public Tag() {
		this("", "", false);
	}

	/**
	 * Gets the name of the tag.
	 * 
	 * @return name of the tag
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the tag.
	 * 
	 * @param name
	 *            name which should be set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Gets the value of the tag.
	 * 
	 * @return value of the tag.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the tag.
	 * 
	 * @param value
	 *            the value which should be set
	 */
	public void setValue(final String value) {
		this.value = value;
	}

	/**
	 * Returns whether the tag is dynamic or not.
	 * 
	 * @return true if it is a dynamic tag, false otherwise
	 */
	public boolean isDynamic() {
		return dynamic;
	}

	/**
	 * Sets whether the tag is dynamic or not.
	 * 
	 * @param dynamic
	 *            the new value
	 */
	public void setDynamic(final boolean dynamic) {
		this.dynamic = dynamic;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Tag tag = (Tag) o;

		if (dynamic != tag.dynamic)
			return false;
		if (!name.equals(tag.name))
			return false;
		return value.equals(tag.value);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + value.hashCode();
		result = 31 * result + (dynamic ? 1 : 0);
		return result;
	}
}
