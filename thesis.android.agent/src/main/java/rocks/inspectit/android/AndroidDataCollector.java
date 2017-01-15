package rocks.inspectit.android;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.os.BatteryManager;

import java.io.IOException;
import java.io.RandomAccessFile;

import rocks.inspectit.android.util.CacheValue;

/**
 * This class is a proxy for accessing Android device informations, because we don't want all modules to collect data on their own.
 * Created by David on 26.10.16.
 */

public class AndroidDataCollector {

    private static final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

    private IntentFilter itFilter;
    private Intent batteryStatus;
    private LocationManager locationManager;
    private ConnectivityManager connectivityManager;
    private ActivityManager activityManager;
    private WifiManager wifiManager;
    private Object mPowerProfile;

    private Context context;

    // CACHE VALUES
    private CacheValue<Float> batteryCache = new CacheValue<Float>(30000L);
    private double batteryCapacityCache = -1;
    private CacheValue<Location> locationCache = new CacheValue<Location>(15000L);
    private CacheValue<NetworkInfo> networkInfoCache = new CacheValue<NetworkInfo>(30000L);
    private CacheValue<Boolean> chargingCache = new CacheValue<Boolean>(30000L);
    private CacheValue<Float> cpuUsage = new CacheValue<Float>(5000L);
    private CacheValue<Double> ramUsage = new CacheValue<Double>(5000L);
    private CacheValue<WifiInfo> wifiInfoCache = new CacheValue<WifiInfo>(30000L);
    private CacheValue<WifiConfiguration> wifiConfigCache = new CacheValue<WifiConfiguration>(60000L);

    protected void initDataCollector(Context ctx) {
        context = ctx;

        itFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = ctx.registerReceiver(null, itFilter);

        locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(context);
        } catch (Exception e) {
            // TODO log
        }
    }

    public String getVersionName() {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "unknown";
        }
    }

    public String resolveAppName() {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public boolean isCharging() {
        return isCharging(false);
    }

    public boolean isCharging(boolean force) {
        if (chargingCache != null && chargingCache.valid() && !force) return chargingCache.value();

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        return chargingCache.set(isCharging);
    }

    public Location getLastKnownLocation() {
        return getLastKnownLocation(false);
    }

    public Location getLastKnownLocation(boolean force) {
        if (locationCache != null && locationCache.valid() && !force) return locationCache.value();

        return locationCache.set(getLocation());
    }

    public float getBatteryPct() {
        return getBatteryPct(false);
    }

    public float getBatteryPct(boolean force) {
        if (batteryCache != null && batteryCache.valid() && !force) return batteryCache.value();

        return batteryCache.set(getBatteryLevel() / (float) getBatteryScale());
    }

    public NetworkInfo getNetworkInfo() {
        return getNetworkInfo(false);
    }

    public NetworkInfo getNetworkInfo(boolean force) {
        if (networkInfoCache != null && networkInfoCache.valid() && !force)
            return networkInfoCache.value();

        return networkInfoCache.set(_getNetworkInfo());
    }

    public float getCpuUsage() {
        return getCpuUsage(false);
    }

    public float getCpuUsage(boolean force) {
        if (cpuUsage != null && cpuUsage.valid() && !force) return cpuUsage.value();

        return cpuUsage.set(readCPUUsage());
    }

    public double getRamUsage() {
        return getRamUsage(false);
    }

    public double getRamUsage(boolean force) {
        if (ramUsage != null && ramUsage.valid() && !force) return ramUsage.value();

        return ramUsage.set(readRamUsage());
    }

    public WifiInfo getWifiInfo() {
        return getWifiInfo(false);
    }

    public WifiInfo getWifiInfo(boolean force) {
        if (wifiInfoCache != null && wifiInfoCache.valid() && !force) return wifiInfoCache.value();

        return wifiInfoCache.set(wifiManager.getConnectionInfo());
    }

    public WifiConfiguration getWifiConfiguration() {
        return getWifiConfiguration(true, false);
    }

    public WifiConfiguration getWifiConfiguration(boolean preCheck, boolean force) {
        if (wifiConfigCache != null && wifiConfigCache.valid() && !force)
            return wifiConfigCache.value();

        if (preCheck) {
            if (getWifiInfo(true) == null) return null;
        }
        
        for (WifiConfiguration conf : wifiManager.getConfiguredNetworks()) {
            if (conf.status == WifiConfiguration.Status.CURRENT) return wifiConfigCache.set(conf);
        }
        return null;
    }

    public double getBatteryCapacity() {
        if (batteryCapacityCache == -1) {
            try {
                double batteryCapacity = (Double) Class
                        .forName(POWER_PROFILE_CLASS)
                        .getMethod("getAveragePower", java.lang.String.class)
                        .invoke(mPowerProfile, "battery.capacity");
                batteryCapacityCache = batteryCapacity;
                return batteryCapacity;
            } catch (Exception e) {
                return -1.0D;
            }
        } else {
            return batteryCapacityCache;
        }
    }

    // HELP FCTS
    private int getBatteryLevel() {
        return batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
    }

    private int getBatteryScale() {
        return batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    }

    private Location getLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return null;
    }

    private NetworkInfo _getNetworkInfo() {
        if (checkPermission(Manifest.permission.ACCESS_NETWORK_STATE)) {
            return connectivityManager.getActiveNetworkInfo();
        }
        return null;
    }

    private boolean checkPermission(String perm) {
        return context.checkCallingOrSelfPermission(perm) == PackageManager.PERMISSION_GRANTED;
    }

    private double readRamUsage() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        // double availableMegs = memoryInfo.availMem / 1048576L;
        double percentAvail = (double) memoryInfo.availMem / (double) memoryInfo.totalMem;

        return 1.0D - percentAvail;
    }

    private float readCPUUsage() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();
            String[] toks = load.split(" +");  // Split on one or more spaces

            long idle1 = Long.parseLong(toks[4]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {}

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" +");

            long idle2 = Long.parseLong(toks[4]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            return (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return 0;
    }
}
