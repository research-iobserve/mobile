package rocks.inspectit.android.callback.kieker;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

/**
 * @author David Monschein
 */
public class MobileNetworkEventRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {

    public static final int SIZE = (2 * TYPE_SIZE_STRING) + (TYPE_SIZE_BYTE * 3);
    public static final Class<?>[] TYPES = {boolean.class, boolean.class, byte.class, String.class, String.class};

    private static final byte BYTE_TRUE = 1;
    private static final byte BYTE_FALSE = 0;

    /**
     * Generated UID.
     */
    private static final long serialVersionUID = 3359535165327219540L;

    private boolean wifi;
    private boolean mobile;

    private byte encryptionLevel;

    private String wSSID;
    private String wBSSID;

    public MobileNetworkEventRecord(String ssid, String bssid, byte level) {
        this(true, false, level, ssid, bssid);
    }

    public MobileNetworkEventRecord(byte level) {
        this(false, true, level, "", "");
    }

    private MobileNetworkEventRecord(boolean wifi, boolean mobile, byte level, String ssid, String bssid) {
        this.wifi = wifi;
        this.mobile = mobile;
        this.encryptionLevel = level;
        this.wSSID = ssid;
        this.wBSSID = bssid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        return new Object[]{wifi, mobile, encryptionLevel, wSSID, wBSSID};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeBytes(ByteBuffer buffer, IRegistry<String> stringRegistry) throws BufferOverflowException {
        buffer.put(wifi ? BYTE_TRUE : BYTE_FALSE);
        buffer.put(mobile ? BYTE_TRUE : BYTE_FALSE);
        buffer.putInt(encryptionLevel);
        buffer.put(wSSID.getBytes());
        buffer.put(wBSSID.getBytes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void initFromBytes(ByteBuffer buffer, IRegistry<String> stringRegistry) throws BufferUnderflowException {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void initFromArray(Object[] values) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?>[] getValueTypes() {
        return TYPES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public void registerStrings(IRegistry<String> stringRegistry) {
        //
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public byte getEncryptionLevel() {
        return encryptionLevel;
    }

    public void setEncryptionLevel(byte encryptionLevel) {
        this.encryptionLevel = encryptionLevel;
    }

    public String getwBSSID() {
        return wBSSID;
    }

    public void setwBSSID(String wBSSID) {
        this.wBSSID = wBSSID;
    }

    public String getwSSID() {
        return wSSID;
    }

    public void setwSSID(String wSSID) {
        this.wSSID = wSSID;
    }
}
