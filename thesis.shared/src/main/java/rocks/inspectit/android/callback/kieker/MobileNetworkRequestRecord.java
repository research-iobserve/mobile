package rocks.inspectit.android.callback.kieker;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

/**
 * Created by DMO on 10.01.2017.
 */

public class MobileNetworkRequestRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {

    public static final int SIZE = (2 * TYPE_SIZE_STRING) + TYPE_SIZE_LONG + TYPE_SIZE_INT;
    public static final Class<?>[] TYPES = {String.class, String.class, long.class, int.class};

    private String url;
    private String method;
    private int responseCode;
    private long duration;

    public MobileNetworkRequestRecord(String url, String method, long duration, int responsecode) {
        this.url = url;
        this.method = method;
        this.responseCode = responsecode;
        this.duration = duration;
    }

    @Override
    public Object[] toArray() {
        return new Object[]{url, method, responseCode, duration};
    }

    @Override
    public void writeBytes(ByteBuffer byteBuffer, IRegistry<String> iRegistry) throws BufferOverflowException {
        byteBuffer.put(url.getBytes());
        byteBuffer.put(method.getBytes());
        byteBuffer.putLong(duration);
        byteBuffer.putInt(responseCode);
    }

    @Override
    public void initFromBytes(ByteBuffer byteBuffer, IRegistry<String> iRegistry) throws BufferUnderflowException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void initFromArray(Object[] objects) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<?>[] getValueTypes() {
        return TYPES;
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public long getDuration() {
        return duration;
    }
}
