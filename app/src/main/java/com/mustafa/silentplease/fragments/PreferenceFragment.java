package com.mustafa.silentplease.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.SwitchPreference;

import com.mustafa.silentplease.R;
import com.mustafa.silentplease.utils.Constants;
import com.mustafa.silentplease.utils.Utils;

import org.joda.time.chrono.ISOChronology;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Mustafa.Gamesterz on 25/05/16.
 */
public class PreferenceFragment extends android.preference.PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {



    private enum TIME_OR_INTERVAL {
        NORMAL, TIME, INTERVAL
    }

    private HashMap<String, TIME_OR_INTERVAL> KEYS_ARRAY = new HashMap<String, TIME_OR_INTERVAL>();
    private HashMap<String, TIME_OR_INTERVAL> MOBILE_KEYS_ARRAY = new HashMap<String, TIME_OR_INTERVAL>();
    private Activity activity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_fragment);

        popultePrefArray();
        populateMobilePrefArray();
        ISOChronology.getInstance();
        Utils.initTime();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    private void popultePrefArray(){
        KEYS_ARRAY.put(Constants.PREF_KEY_WIFI_STATUS, TIME_OR_INTERVAL.NORMAL);
        KEYS_ARRAY.put(Constants.PREF_KEY_MOBILE_STATUS, TIME_OR_INTERVAL.NORMAL);
        KEYS_ARRAY.put(Constants.PREF_KEY_WIFI_OFF_TIME, TIME_OR_INTERVAL.TIME);
        KEYS_ARRAY.put(Constants.PREF_KEY_WIFI_OFF_INTERVAL, TIME_OR_INTERVAL.INTERVAL);
        KEYS_ARRAY.put(Constants.PREF_KEY_MOBILE_OFF_TIME, TIME_OR_INTERVAL.TIME);
        KEYS_ARRAY.put(Constants.PREF_KEY_MOBILE_OFF_INTERVAL, TIME_OR_INTERVAL.INTERVAL);
        KEYS_ARRAY.put(Constants.PREF_KEY_WIFI_ON_TIME, TIME_OR_INTERVAL.TIME);
        KEYS_ARRAY.put(Constants.PREF_KEY_WIFI_ON_INTERVAL, TIME_OR_INTERVAL.INTERVAL);
        KEYS_ARRAY.put(Constants.PREF_KEY_MOBILE_ON_TIME, TIME_OR_INTERVAL.TIME);
        KEYS_ARRAY.put(Constants.PREF_KEY_MOBILE_ON_INTERVAL, TIME_OR_INTERVAL.INTERVAL);
    }

    private void populateMobilePrefArray(){
        MOBILE_KEYS_ARRAY.put(Constants.PREF_KEY_MOBILE_STATUS,TIME_OR_INTERVAL.NORMAL);
        MOBILE_KEYS_ARRAY.put(Constants.PREF_KEY_MOBILE_OFF_TIME,TIME_OR_INTERVAL.TIME);
        MOBILE_KEYS_ARRAY.put(Constants.PREF_KEY_MOBILE_OFF_INTERVAL,TIME_OR_INTERVAL.INTERVAL);
        MOBILE_KEYS_ARRAY.put(Constants.PREF_KEY_MOBILE_ON_TIME,TIME_OR_INTERVAL.TIME);
        MOBILE_KEYS_ARRAY.put(Constants.PREF_KEY_MOBILE_OFF_INTERVAL,TIME_OR_INTERVAL.INTERVAL);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(activity != null){
            Utils.initTime();
            setSummary();
            setMobileDataDisabled();
        }
    }


    private void setSummary(){
        if(activity!=null){
            SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();

            if(preferences!=null){
                for(String key : KEYS_ARRAY.keySet()){
                    Preference p =findPreference(key);
                    if(p!=null){
                        if(KEYS_ARRAY.get(key).equals(TIME_OR_INTERVAL.NORMAL)){
                            if(key.equals(Constants.PREF_KEY_WIFI_STATUS)){
                                boolean isWIFION = Utils.isWiFiEnabledStatus(activity);
                                if((isWIFION && !preferences.getBoolean(key,false)) || (!isWIFION && preferences.getBoolean(key,false))){
                                    ((SwitchPreference) p).setChecked(isWIFION);
                                }

                            } else if(key.equals(Constants.PREF_KEY_MOBILE_STATUS)){
                                boolean isMobileOn = Utils.isMobileNetworkEnabledStatus(activity);
                                if((isMobileOn && !preferences.getBoolean(key,false)) || (!isMobileOn && preferences.getBoolean(key,false))){
                                    ((SwitchPreference) p).setChecked(isMobileOn);
                                }
                            }
                        } else if (KEYS_ARRAY.get(key).equals(TIME_OR_INTERVAL.TIME)){
                            if(!preferences.getBoolean(key + Constants.PREF_KEY_SWITCH_SUFFIX,false)) {
                                p.setSummary(Utils.getNowTime());
                            } else
                                p.setSummary(Utils.getFormattedTime(preferences.getString(key,Utils.getNowTime())));
                        } else if(KEYS_ARRAY.get(key).equals(TIME_OR_INTERVAL.INTERVAL)){
                            p.setSummary(activity.getString(R.string.interval_format,preferences.getInt(key, 0)));
                        }
                    }
                }
            }
        }
    }



    private void setMobileDataDisabled(){
        if (Utils.isNetWorkOperatorAvailable(activity)){
            setEnabledPrefrence(true);
        }else {
            setEnabledPrefrence(false);
        }
    }

    private void setEnabledPrefrence(boolean shouldBeEnable){
        Iterator iterator = MOBILE_KEYS_ARRAY.keySet().iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            Preference p = findPreference(key);
            if(shouldBeEnable && !p.isEnabled())
                p.setEnabled(true);
            else if (!shouldBeEnable && p.isEnabled())
                p.setEnabled(false);
        }
    }



    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (activity != null) {
            String originalKey = key.replace(Constants.PREF_KEY_SWITCH_SUFFIX, "");
            Preference p = findPreference(originalKey);

            if (key.contains(Constants.PREF_KEY_SWITCH_SUFFIX)) {
                p = findPreference(originalKey);

                if (KEYS_ARRAY.get(originalKey).equals(TIME_OR_INTERVAL.TIME)) {
                    ((TimeDIalogFragment) p).getSwitchLIStener().updatePreference();
                } else if (KEYS_ARRAY.get(originalKey).equals(TIME_OR_INTERVAL.INTERVAL)) {
                    ((TimeIntervalDialogFragment) p).getSwitchCheckedListener().updatePreference();
                } else if (KEYS_ARRAY.get(originalKey).equals(TIME_OR_INTERVAL.NORMAL)) {
                    boolean value = ((SwitchPreference) p).isChecked();

                    if (originalKey.equals(Constants.PREF_KEY_WIFI_STATUS)) {
                        ((SwitchPreference) p).setChecked(!value);
                    } else if (originalKey.equals(Constants.PREF_KEY_MOBILE_STATUS)) {
                        ((SwitchPreference) p).setChecked(!value);
                    }

                    return;
                }
            }

            if (KEYS_ARRAY.get(originalKey).equals(TIME_OR_INTERVAL.TIME)) {
                String time = sharedPreferences.getString(originalKey, Utils.getNowTime());
                final String timeForEvent = sharedPreferences.getString(originalKey, Utils.getNowTime()).substring(0, sharedPreferences.getString(originalKey, Utils.getNowTime()).indexOf(" "));
                final String oldHour = time.substring(0, time.indexOf(":"));
                int newHour = Utils.getFormattedHour(oldHour);
                time = time.replaceFirst(oldHour, String.valueOf(newHour));

                p.setSummary(time);

                if (((TimeDIalogFragment) p).getSwitchLIStener() != null) {
                    if (((TimeDIalogFragment) p).isSwitchON())
                        Utils.scheduleTimeEvent(activity, timeForEvent, originalKey);
                    else
                        Utils.cancelEvent(activity, originalKey);
                }
            } else if (KEYS_ARRAY.get(originalKey).equals(TIME_OR_INTERVAL.INTERVAL)) {
                p.setSummary(activity.getString(R.string.interval_format, sharedPreferences.getInt(originalKey, 0)));

                if (((TimeIntervalDialogFragment) p).getSwitchCheckedListener() != null) {
                    if (((TimeIntervalDialogFragment) p).isSwitchOn())
                        Utils.scheduleInternaEvent(activity, Integer.valueOf(p.getSummary().toString().replace(" min", "")), originalKey);
                    else
                        Utils.cancelEvent(activity, originalKey);
                }
            } else if (KEYS_ARRAY.get(originalKey).equals(TIME_OR_INTERVAL.NORMAL)) {
                boolean value = ((SwitchPreference) p).isChecked();

                if (originalKey.equals(Constants.PREF_KEY_WIFI_STATUS)) {
                    boolean isWifiOn = Utils.isWiFiEnabledStatus(activity);
                    if ((isWifiOn && !value) || (!isWifiOn && value)) {
                        SwitchConnectionTask task = new SwitchConnectionTask(p, value, true);
                        task.execute();
                    }
                } else if (originalKey.equals(Constants.PREF_KEY_MOBILE_STATUS)) {
                    boolean isMobileOn = Utils.isMobileNetworkEnabledStatus(activity);
                    if ((isMobileOn && !value) || (!isMobileOn && value)) {
                        SwitchConnectionTask task = new SwitchConnectionTask(p, value, false);
                        task.execute();
                    }
                }
            }
        }
    }

    private class SwitchConnectionTask extends AsyncTask<Void,Void,Boolean>{
        private Preference preference;
        private boolean shouldBeON;
        private boolean isWifi;

        public SwitchConnectionTask(Preference preference, boolean shouldBeON, boolean isWifi){
            this.preference = preference;
            this.shouldBeON = shouldBeON;
            this.isWifi = isWifi;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            preference.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if(isWifi){
                Utils.turnWiFiEnabled(activity,shouldBeON);
                while (!Utils.isWifiStatusEqual(activity,shouldBeON ? WifiManager.WIFI_STATE_ENABLED : WifiManager.WIFI_STATE_DISABLED)){

                }
                return true;
            } else {
                Utils.turnMobileNetworkEnabled(activity,shouldBeON);
                while (!Utils.isMobileStatusEqual(activity, shouldBeON)){

                }

                return true;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            int resID = shouldBeON ? R.string.notification_msg_turn_on_wifi_done : R.string.notification_msg_turn_off_wifi_done;
            if(!isWifi)
                resID = shouldBeON ? R.string.notification_msg_turn_on_network_done : R.string.notification_msg_turn_off_network_done;

            Utils.showShortToast(activity,resID);
            preference.setEnabled(true);
        }
    }
}
