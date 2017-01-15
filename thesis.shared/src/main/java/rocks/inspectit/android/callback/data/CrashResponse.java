package rocks.inspectit.android.callback.data;

/**
 * Created by DMO on 16.12.2016.
 */

public class CrashResponse extends MobileDefaultData {

    private String exceptionName;
    private String exceptionMessage;
    private long exceptionTimestamp;

    public CrashResponse() {
        this.exceptionTimestamp = System.currentTimeMillis();
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public long getExceptionTimestamp() {
        return exceptionTimestamp;
    }
}
