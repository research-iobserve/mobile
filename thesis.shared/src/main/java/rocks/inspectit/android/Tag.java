package rocks.inspectit.android;

/**
 * Created by David on 24.10.16.
 */

public class Tag {

    private String name;
    private String value;
    private boolean dynamic;

    public Tag(String n, String v, boolean dyn) {
        this.name = n;
        this.value = v;
        this.dynamic = dyn;
    }

    public Tag(String n, String v) {
        this(n, v, false);
    }

    public Tag() {
        this("", "", false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (dynamic != tag.dynamic) return false;
        if (!name.equals(tag.name)) return false;
        return value.equals(tag.value);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + (dynamic ? 1 : 0);
        return result;
    }
}
