package rocks.inspectit.android;

/**
 * The data in this class gets set external by code inserted into the original application.
 */
public class ExternalConfiguration {

    private static String beaconUrl;
    private static String helloUrl;

    private static final String LOG_TAG = "Android Agent";

    public static String getBeaconUrl() {
        return beaconUrl;
    }

    public static String getHelloUrl() {
        return helloUrl;
    }

    public static void setBeaconUrl(String beaconUrl) {
        ExternalConfiguration.beaconUrl = beaconUrl;
    }

    public static void setHelloUrl(String helloUrl) {
        ExternalConfiguration.helloUrl = helloUrl;
    }

    public static String getLogTag() {
        return LOG_TAG;
    }
}
