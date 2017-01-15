package rocks.inspectit.android.sensor;

/**
 * Created by David on 25.10.16.
 */

public interface ISensor {
    void beforeBody();

    void exceptionThrown(String clazz);
    void firstAfterBody();
    void secondAfterBody();
    void setOwner(String owner);
    void setSignature(String methodSignature);
}
