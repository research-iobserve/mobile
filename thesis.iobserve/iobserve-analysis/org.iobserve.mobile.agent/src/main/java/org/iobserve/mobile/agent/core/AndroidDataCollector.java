package org.iobserve.mobile.agent.core;

import java.io.IOException;
import java.io.RandomAccessFile;

import org.iobserve.mobile.agent.util.CacheValue;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * This class is a proxy for accessing Android device informations, because we
 * don't want all modules to collect data on their own.
 * 
 * @author David Monschein
 */
public class AndroidDataCollector {
	/**
	 * {@link TelephonyManager} for retrieving information
	 */
	private TelephonyManager telephonyManager;

	/**
	 * {@link LocationManager} for retrieving information
	 */
	private LocationManager locationManager;

	/**
	 * {@link ConnectivityManager} for retrieving information
	 */
	private ConnectivityManager connectivityManager;

	/**
	 * {@link ActivityManager} for retrieving information
	 */
	private ActivityManager activityManager;

	/**
	 * {@link WifiManager} for retrieving information
	 */
	private WifiManager wifiManager;

	/**
	 * Context of the application which is needed to create the managers above
	 */
	private Context context;

	// CACHE VALUES
	/**
	 * Cache value for the location.
	 */
	private CacheValue<Location> locationCache = new CacheValue<Location>(15000L);

	/**
	 * Cache value for the network information.
	 */
	private CacheValue<NetworkInfo> networkInfoCache = new CacheValue<NetworkInfo>(30000L);

	/**
	 * Cache value for the cpu usage.
	 */
	private CacheValue<Float> cpuUsage = new CacheValue<Float>(5000L);

	/**
	 * Cache value for the ram usage.
	 */
	private CacheValue<Double> ramUsage = new CacheValue<Double>(5000L);

	/**
	 * Cache value for the wifi information.
	 */
	private CacheValue<WifiInfo> wifiInfoCache = new CacheValue<WifiInfo>(30000L);

	/**
	 * Cache value for the wifi configuration.
	 */
	private CacheValue<WifiConfiguration> wifiConfigCache = new CacheValue<WifiConfiguration>(60000L);

	/**
	 * Cache value for the network carrier.
	 */
	private CacheValue<String> networkCarrierCache = new CacheValue<String>();

	/**
	 * Cache value for the provided id of the device.
	 */
	private CacheValue<String> deviceIdCache = new CacheValue<String>();

	/**
	 * Creates a data collector and needs a given context as parameter.
	 * 
	 * @param ctx
	 *            context of the application
	 */
	protected void initDataCollector(Context ctx) {
		context = ctx;

		locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * Gets the version name of the application.
	 * 
	 * @return version name of the application or the value 'unknown' if the
	 *         version isn't set
	 */
	public String getVersionName() {
		PackageInfo pInfo = null;
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			return "unknown";
		}
	}

	/**
	 * Gets the name of the application.
	 * 
	 * @return the name of the application
	 */
	public String resolveAppName() {
		ApplicationInfo applicationInfo = context.getApplicationInfo();
		int stringId = applicationInfo.labelRes;
		return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
	}

	/**
	 * Gets the last known location (Needs permissions for accessing location).
	 * 
	 * @return the location of the device
	 */
	public Location getLastKnownLocation() {
		return getLastKnownLocation(false);
	}

	/**
	 * Gets the last known location and provides a parameter for forcing the
	 * collector to reload the value.
	 * 
	 * @param force
	 *            if true, the current location will be reloaded and the value
	 *            from the cache won'T be used
	 * @return the last known location of the user
	 */
	public Location getLastKnownLocation(boolean force) {
		if (locationCache != null && locationCache.valid() && !force)
			return locationCache.value();

		return locationCache.set(getLocation());
	}

	/**
	 * Gets network information of the device.
	 * 
	 * @return network information of the device
	 */
	public NetworkInfo getNetworkInfo() {
		return getNetworkInfo(false);
	}

	/**
	 * Gets network information and provides a parameter for forcing the reload
	 * of the info.
	 * 
	 * @param force
	 *            true if the collector should reload the network information
	 * @return current network informations from the device
	 */
	public NetworkInfo getNetworkInfo(boolean force) {
		if (networkInfoCache != null && networkInfoCache.valid() && !force)
			return networkInfoCache.value();

		return networkInfoCache.set(_getNetworkInfo());
	}

	/**
	 * Gets the current cpu usage.
	 * 
	 * @return the current cpu usage as float.
	 */
	public float getCpuUsage() {
		return getCpuUsage(false);
	}

	/**
	 * Gets the current cpu usage and provides a parameter to force a reload.
	 * 
	 * @param force
	 *            true if the collector should reload the current usage and not
	 *            take it from the cache
	 * @return current cpu usage as float
	 */
	public float getCpuUsage(boolean force) {
		if (cpuUsage != null && cpuUsage.valid() && !force)
			return cpuUsage.value();

		return cpuUsage.set(readCPUUsage());
	}

	/**
	 * Gets the current ram usage of the device.
	 * 
	 * @return the ram usage of the device
	 */
	public double getRamUsage() {
		return getRamUsage(false);
	}

	/**
	 * Gets the current ram usage and provides a parameter to force a reload of
	 * the current usage.
	 * 
	 * @param force
	 *            true if you want the collector to force a reload of the
	 *            current usage
	 * @return the current ram usage of the device
	 */
	public double getRamUsage(boolean force) {
		if (ramUsage != null && ramUsage.valid() && !force)
			return ramUsage.value();

		return ramUsage.set(readRamUsage());
	}

	/**
	 * Gets informations about the wifi of the device.
	 * 
	 * @return wifi informations from the device
	 */
	public WifiInfo getWifiInfo() {
		return getWifiInfo(false);
	}

	/**
	 * Gets informations about the wifi of the device and provides a parameter
	 * to force a reload of the current information
	 * 
	 * @param force
	 *            true if you want the collector to force a reload of the wifi
	 *            informations
	 * @return the wifi informations for the device
	 */
	public WifiInfo getWifiInfo(boolean force) {
		if (wifiInfoCache != null && wifiInfoCache.valid() && !force)
			return wifiInfoCache.value();

		return wifiInfoCache.set(wifiManager.getConnectionInfo());
	}

	/**
	 * Gets the wifi configuration for the wifi to which the device is connected
	 * 
	 * @return wifi configuration for the current configured and connected wifi
	 *         of the device
	 */
	public WifiConfiguration getWifiConfiguration() {
		return getWifiConfiguration(true, false);
	}

	/**
	 * Gets the wifi configuration for the connected wifi.
	 * 
	 * @param preCheck
	 *            previously check if we have a connection
	 * @param force
	 *            true if you want the collector to reload data about the
	 *            configuration
	 * @return wifi configuration for the current configured and connected wifi
	 *         of the device
	 */
	public WifiConfiguration getWifiConfiguration(boolean preCheck, boolean force) {
		if (wifiConfigCache != null && wifiConfigCache.valid() && !force)
			return wifiConfigCache.value();

		if (preCheck) {
			if (getWifiInfo(true) == null)
				return null;
		}

		for (WifiConfiguration conf : wifiManager.getConfiguredNetworks()) {
			if (conf.status == WifiConfiguration.Status.CURRENT)
				return wifiConfigCache.set(conf);
		}
		return null;
	}

	/**
	 * Gets the name of the mobile network carrier.
	 * 
	 * @return name of the mobile network carrier
	 */
	public String getNetworkCarrierName() {
		if (networkCarrierCache != null && networkCarrierCache.valid())
			return networkCarrierCache.value();

		return networkCarrierCache.set(telephonyManager.getNetworkOperatorName());
	}

	/**
	 * Gets the id of the device. See {@link TelephonyManager} for detailed
	 * information.
	 * 
	 * @return id of the device with model of the device as prefix
	 */
	public String getDeviceId() {
		if (deviceIdCache != null && deviceIdCache.valid())
			return deviceIdCache.value();

		return deviceIdCache.set(android.os.Build.MODEL + "-" + telephonyManager.getDeviceId());
	}

	// HELP FCTS
	/**
	 * Accesses the current location of the device when the application has the
	 * permissions to do so.
	 * 
	 * @return the location of the device and null if we don't have enough
	 *         permissions
	 */
	private Location getLocation() {
		if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
			return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		return null;
	}

	/**
	 * Gets network informations and previously checks whether we have the
	 * permission for that
	 * 
	 * @return current network information for the device and null if we don't
	 *         have enough permissions
	 */
	private NetworkInfo _getNetworkInfo() {
		if (checkPermission(Manifest.permission.ACCESS_NETWORK_STATE)) {
			return connectivityManager.getActiveNetworkInfo();
		}
		return null;
	}

	/**
	 * Checks whether the application has a specific permission.
	 * 
	 * @param perm
	 *            name of the permission
	 * @return true if the application has the requested permission - false
	 *         otherwise
	 */
	private boolean checkPermission(String perm) {
		return context.checkCallingOrSelfPermission(perm) == PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * Collects the current ram usage.
	 * 
	 * @return current ram usage
	 */
	private double readRamUsage() {
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);

		// double availableMegs = memoryInfo.availMem / 1048576L;
		double percentAvail = (double) memoryInfo.availMem / (double) memoryInfo.totalMem;

		return 1.0D - percentAvail;
	}

	/**
	 * Collects the current cpu usage.
	 * 
	 * @return current cpu usage
	 */
	private float readCPUUsage() {
		try {
			RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
			String load = reader.readLine();
			String[] toks = load.split(" +"); // Split on one or more spaces

			long idle1 = Long.parseLong(toks[4]);
			long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
					+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

			try {
				Thread.sleep(360);
			} catch (Exception e) {
			}

			reader.seek(0);
			load = reader.readLine();
			reader.close();

			toks = load.split(" +");

			long idle2 = Long.parseLong(toks[4]);
			long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
					+ Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

			return (float) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return 0;
	}
}
