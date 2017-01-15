package rocks.inspectit.android;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import rocks.inspectit.android.util.DependencyInjector;

/**
 * Created by David on 24.10.16.
 */

public class TagCollector {

    private static final long TAG_DYNAMIC_VALID_TIME = 30000L;

    private Set<Tag> staticTags;
    private Set<Tag> dynamicTags;

    private long dynamicTimestamp;

    private AndroidDataCollector androidDataCollector = DependencyInjector.getAndroidDataCollector();

    protected TagCollector(Context ctx) {
        this.staticTags = new HashSet<Tag>();
        this.dynamicTags = new HashSet<Tag>();
        dynamicTimestamp = -1;
    }

    public Set<Tag> getStaticTags() {
        return staticTags;
    }

    public Set<Tag> getDynamicTags() {
        return dynamicTags;
    }

    public void collectStaticTags() {
        staticTags.clear();

        // COLLECT
        staticTags.add(new Tag("app.version", androidDataCollector.getVersionName()));
        staticTags.add(new Tag("device.name", Build.MODEL));
        staticTags.add(new Tag("device.os", Build.VERSION.RELEASE));
        staticTags.add(new Tag("device.lang", Locale.getDefault().getDisplayLanguage()));
    }

    public void collectDynamicTags() {
        if (System.currentTimeMillis() - dynamicTimestamp > TAG_DYNAMIC_VALID_TIME) {
            dynamicTags.clear();

            // Location tag
            Location location = androidDataCollector.getLastKnownLocation();

            if (location != null) {
                Tag locationTag1 = new Tag("device.location.lon", String.valueOf(location.getLongitude()), true);
                Tag locationTag2 = new Tag("device.location.lat", String.valueOf(location.getLatitude()), true);
                Tag locationTag3 = new Tag("device.location.alt", String.valueOf(location.getAltitude()), true);

                dynamicTags.add(locationTag1);
                dynamicTags.add(locationTag2);
                dynamicTags.add(locationTag3);
            }

            // Network info tag
            NetworkInfo networkInfo = androidDataCollector.getNetworkInfo();

            if (networkInfo != null) {
                Tag networkTag1 = new Tag("device.network.connected", String.valueOf(networkInfo.isConnected()), true);
                Tag networkTag2 = new Tag("device.network.wifi", String.valueOf(networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI), true);
                Tag networkTag3 = new Tag("device.network.mobile", String.valueOf(networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE), true);

                dynamicTags.add(networkTag1);
                dynamicTags.add(networkTag2);
                dynamicTags.add(networkTag3);
            }

            Tag batteryTag = new Tag("device.battery.pct", String.valueOf(androidDataCollector.getBatteryPct()), true);
            Tag batteryCapTag = new Tag("device.battery.cap", String.valueOf(androidDataCollector.getBatteryCapacity()), true);

            dynamicTags.add(batteryTag);
            dynamicTags.add(batteryCapTag);

            dynamicTimestamp = System.currentTimeMillis();
        }
    }

}
