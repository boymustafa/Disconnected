package com.mustafa.silentplease.utils;

/**
 * Created by Mustafa.Gamesterz on 24/05/16.
 *
 *  put all constants here
 */
public abstract class Constants {

    public static final String PREF_KEY_WIFI_STATUS = "pref_status_wifi";
    public static final String PREF_KEY_MOBILE_STATUS = "pref_status_mobile";
    public static final String PREF_KEY_WIFI_OFF_TIME = "pref_wifi_at_time_off";
    public static final String PREF_KEY_WIFI_OFF_INTERVAL = "pref_wifi_with_interval_off";
    public static final String PREF_KEY_MOBILE_OFF_TIME = "pref_mobile_data_at_time_off";
    public static final String PREF_KEY_MOBILE_OFF_INTERVAL = "pref_mobile_data_with_interval_off";
    public static final String PREF_KEY_WIFI_ON_TIME = "pref_wifi_at_time_on";
    public static final String PREF_KEY_WIFI_ON_INTERVAL = "pref_wifi_with_interval_on";
    public static final String PREF_KEY_MOBILE_ON_TIME = "pref_mobile_data_at_time_on";
    public static final String PREF_KEY_MOBILE_ON_INTERVAL = "pref_mobile_data_with_interval_on";
    public static final String PREF_KEY_SWITCH_SUFFIX = "_switch";

    public static final String EXTRA_TIME = "extra_time_event_key";
    public static final String EXTRA_IS_TIME = "extra_is_time_event_key";
    public static final String EXTRA_KEY = "extra_key";

    public static final int PENDING_INTENT_WIFI_OFF_TIME_CODE = 1234;
    public static final int PENDING_INTENT_WIFI_OFF_INTERVAL_CODE = 2345;
    public static final int PENDING_INTENT_MOBILE_OFF_TIME_CODE = 3456;
    public static final int PENDING_INTENT_MOBILE_OFF_INTERVAL_CODE = 4567;
    public static final int PENDING_INTENT_WIFI_ON_TIME_CODE = 5678;
    public static final int PENDING_INTENT_WIFI_ON_INTERVAL_CODE = 6789;
    public static final int PENDING_INTENT_MOBILE_ON_TIME_CODE = 7890;
    public static final int PENDING_INTENT_MOBILE_ON_INTERVAL_CODE = 8901;

    public static final int NOTIFICATION_KEY_WIFI_ON = 8902;
    public static final int NOTIFICATION_KEY_WIFI_OFF = 8903;
    public static final int NOTIFICATION_KEY_MOBILE_ON = 8904;
    public static final int NOTIFICATION_KEY_MOBILE_OFF = 8905;

    public static final String NOTIFICATION_TIME_FORMAT = "d MMM yyy h:mm a";

}
