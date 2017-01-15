package rocks.inspectit.android.broadcast;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.telephony.TelephonyManager;

import java.util.BitSet;

import rocks.inspectit.android.callback.kieker.NetInfoResponse;

/**
 * Created by DMO on 04.01.2017.
 */

public class NetworkBroadcastReceiver extends AbstractBroadcastReceiver {

    private static final byte SECURITY_NONE = 0;
    private static final byte SECURITY_WEP = 1;
    private static final byte SECURITY_WPA = 2;
    private static final byte SECURITY_WPA2 = 3;

    private static final String[] ACTIONS = new String[]{"android.net.conn.CONNECTIVITY_CHANGE", "android.net.wifi.WIFI_STATE_CHANGED"};

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo currentNetwork = androidDataCollector.getNetworkInfo(true); // force reload
        if (currentNetwork != null && currentNetwork.isConnected()) {
            // connected
            boolean wifi = currentNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            boolean mobile = currentNetwork.getType() == ConnectivityManager.TYPE_MOBILE;

            // WIFI PART
            if (wifi) {
                WifiInfo wifiInfo = androidDataCollector.getWifiInfo(true);
                WifiConfiguration currentConfig = androidDataCollector.getWifiConfiguration(false, true);

                if (wifiInfo != null && currentConfig != null) {
                    String wlanSSID = currentConfig.SSID;
                    String wlanBSSID = currentConfig.BSSID;

                    byte security = getSecurityLevel(currentConfig);

                    this.pushData(new NetInfoResponse(wifi, mobile, security, wlanSSID, wlanBSSID));
                }
            }

            // MOBILE PART
            if (mobile) {
                byte security = getSecurityLevelMobile(currentNetwork.getSubtype());
                this.pushData(new NetInfoResponse(wifi, mobile, security, "", ""));
            }

        } else {
            // not connected anymore
        }
    }

    @Override
    public String[] getFilterActions() {
        return ACTIONS;
    }

    private byte getSecurityLevel(WifiConfiguration config) {
        BitSet allowedKeyMng = config.allowedKeyManagement;

        if (allowedKeyMng.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            // DIFFER WPA AND WPA2
            if (config.allowedProtocols.get(WifiConfiguration.Protocol.RSN)) {
                return SECURITY_WPA2;
            } else {
                return SECURITY_WPA;
            }
        } else if (allowedKeyMng.get(WifiConfiguration.KeyMgmt.WPA_EAP) || allowedKeyMng.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return SECURITY_WPA;
        } else {
            if (config.wepKeys[0] != null) {
                return SECURITY_WEP;
            } else {
                return SECURITY_NONE;
            }
        }
    }

    private byte getSecurityLevelMobile(int subtype) {
        switch (subtype) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return 1; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return 1; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return 1; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return 2; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return 2; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return 1; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return 2; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return 2; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return 3; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return 2; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                return 2; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                return 2; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                return 3; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                return 0; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                return 3; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                return 0;
        }
    }
}
