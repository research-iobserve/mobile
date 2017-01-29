package rocks.inspectit.android.broadcast;

import java.util.BitSet;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.telephony.TelephonyManager;
import rocks.inspectit.android.callback.kieker.NetInfoResponse;

/**
 * Created by DMO on 04.01.2017.
 */

public class NetworkBroadcastReceiver extends AbstractBroadcastReceiver {
	private static final String[] ACTIONS = new String[] { "android.net.conn.CONNECTIVITY_CHANGE",
			"android.net.wifi.WIFI_STATE_CHANGED" };

	private String deviceId;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (deviceId == null) {
			deviceId = androidDataCollector.getDeviceId();
		}

		NetworkInfo currentNetwork = androidDataCollector.getNetworkInfo(true); // force
																				// reload
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
					String protocolName = getProtocolName(currentConfig);

					this.pushData(new NetInfoResponse(deviceId, wifi, mobile, protocolName, "", wlanSSID, wlanBSSID));
				}
			}

			// MOBILE PART
			if (mobile) {
				String provider = androidDataCollector.getNetworkOperatorName();
				String protocolType = this.getProtocolName(currentNetwork.getSubtype());

				this.pushData(new NetInfoResponse(deviceId, wifi, mobile, protocolType, provider, "", ""));
			}

		} else {
			// no connection (anymore)
			this.pushData(new NetInfoResponse(deviceId, false, false, null, null, null, null));
		}
	}

	@Override
	public String[] getFilterActions() {
		return ACTIONS;
	}

	private String getProtocolName(WifiConfiguration config) {
		BitSet allowedKeyMng = config.allowedKeyManagement;

		if (allowedKeyMng.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
			// DIFFER WPA AND WPA2
			if (config.allowedProtocols.get(WifiConfiguration.Protocol.RSN)) {
				return "WPA2";
			} else {
				return "WPA";
			}
		} else if (allowedKeyMng.get(WifiConfiguration.KeyMgmt.WPA_EAP)
				|| allowedKeyMng.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
			return "WPA";
		} else {
			if (config.wepKeys[0] != null) {
				return "WEP";
			} else {
				return "NONE";
			}
		}
	}

	private String getProtocolName(int subtype) {
		switch (subtype) {
		case TelephonyManager.NETWORK_TYPE_1xRTT:
			return "1xrtt"; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_CDMA:
			return "cdma"; // ~ 14-64 kbps
		case TelephonyManager.NETWORK_TYPE_EDGE:
			return "edge"; // ~ 50-100 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
			return "evdo0"; // ~ 400-1000 kbps
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
			return "evdoa"; // ~ 600-1400 kbps
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return "gprs"; // ~ 100 kbps
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			return "hsdpa"; // ~ 2-14 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPA:
			return "hspa"; // ~ 700-1700 kbps
		case TelephonyManager.NETWORK_TYPE_HSUPA:
			return "hsupa"; // ~ 1-23 Mbps
		case TelephonyManager.NETWORK_TYPE_UMTS:
			return "umts"; // ~ 400-7000 kbps
		case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
			return "ehrpd"; // ~ 1-2 Mbps
		case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
			return "evdob"; // ~ 5 Mbps
		case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
			return "hspap"; // ~ 10-20 Mbps
		case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
			return "iden"; // ~25 kbps
		case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
			return "lte"; // ~ 10+ Mbps
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			return "unknown";
		default:
			return "unknown";
		}
	}
}
