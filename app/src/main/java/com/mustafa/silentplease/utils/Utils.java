package com.mustafa.silentplease.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.mustafa.silentplease.MainActivity;
import com.mustafa.silentplease.R;
import com.mustafa.silentplease.receivers.BroadReceiver;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mustafa.Gamesterz on 24/05/16.
 *
 * put all helper methods here
 *
 */
public abstract class Utils {

    private static DateTime initialDateTime;

    public static String getPackageName(Context context){
        return context.getPackageName();
    }

    public static void cancelEvent(Context context, String key){
        if(context == null || key == null)
            return;

        Intent intent = new Intent(context, BroadReceiver.class);
        PendingIntent.getBroadcast(context,getKeyCode(key), intent, PendingIntent.FLAG_UPDATE_CURRENT).cancel();
    }

    public static void changeStatus(Context context){
        if(context==null) return;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        boolean isWifiAvailable = Utils.isWiFiEnabledStatus(context);
        if((isWifiAvailable && !preferences.getBoolean(Constants.PREF_KEY_WIFI_STATUS, false))
                || (!isWifiAvailable && preferences.getBoolean(Constants.PREF_KEY_WIFI_STATUS,false))){
            editor.putBoolean(Constants.PREF_KEY_WIFI_STATUS + Constants.PREF_KEY_SWITCH_SUFFIX, isWifiAvailable);
            editor.commit();
        }
    }

     /*
        turn off switch after action has been performed, to avoid repeated action
      */
    public static void changeStatusSwitch(Context context, String key){
        if(context==null || key == null || key.isEmpty()){
            return;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        if(key.contains("wifi")){
            editor.putBoolean(Constants.PREF_KEY_WIFI_STATUS + Constants.PREF_KEY_SWITCH_SUFFIX,!preferences.getBoolean(Constants.PREF_KEY_WIFI_STATUS + Constants.PREF_KEY_SWITCH_SUFFIX,false));
        } else {
            editor.putBoolean(Constants.PREF_KEY_MOBILE_STATUS + Constants.PREF_KEY_SWITCH_SUFFIX,!preferences.getBoolean(Constants.PREF_KEY_MOBILE_STATUS + Constants.PREF_KEY_SWITCH_SUFFIX,false));

        }

        editor.commit();

    }


    public static void disableSwitchAfterAct(Context context, String key){
        if(context == null || key == null || key.isEmpty()) return;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(key+Constants.PREF_KEY_SWITCH_SUFFIX,false);
        editor.commit();
    }

    public static int getKeyCode(String key){
        if(key == null || key.isEmpty()) return -1;

        if(key.equals(Constants.PREF_KEY_WIFI_OFF_TIME)) return Constants.PENDING_INTENT_WIFI_OFF_TIME_CODE;

        if(key.equals(Constants.PREF_KEY_WIFI_OFF_INTERVAL)) return Constants.PENDING_INTENT_WIFI_OFF_INTERVAL_CODE;

        if(key.equals(Constants.PREF_KEY_MOBILE_OFF_TIME)) return Constants.PENDING_INTENT_MOBILE_OFF_TIME_CODE;

        if(key.equals(Constants.PREF_KEY_MOBILE_OFF_INTERVAL)) return Constants.PENDING_INTENT_MOBILE_OFF_INTERVAL_CODE;

        if(key.equals(Constants.PREF_KEY_WIFI_ON_TIME)) return Constants.PENDING_INTENT_WIFI_ON_TIME_CODE;

        if(key.equals(Constants.PREF_KEY_WIFI_ON_INTERVAL)) return Constants.PENDING_INTENT_WIFI_ON_INTERVAL_CODE;

        if(key.equals(Constants.PREF_KEY_MOBILE_ON_TIME)) return Constants.PENDING_INTENT_MOBILE_ON_TIME_CODE;

        if(key.equals(Constants.PREF_KEY_MOBILE_ON_INTERVAL)) return Constants.PENDING_INTENT_MOBILE_ON_INTERVAL_CODE;

        return -1;
    }

    public static int getFormattedHour(String time){
        if(time == null) return 0;

        String[] pieces = time.split(":");
        int hour = Integer.parseInt(pieces[0]);

        if(hour == 0)
            hour = 12;
        else if(hour > 12)
            hour -= 12;

        return hour;
    }

    public static String getFormattedTime(String time){
        if(time==null) return "";

        return getFormattedHour(time)+time.substring(time.indexOf(":"));
    }

    public static int getHour(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("h");

        return Integer.valueOf(dateTimeFormatter.print(initialDateTime));
    }

    public static int getMinute(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("mm");

        return Integer.valueOf(dateTimeFormatter.print(initialDateTime));
    }

    public static int getNotificationIdFromKey(String key){
        if(key == null || key.isEmpty()) return -1;

        if(key.equals(Constants.PREF_KEY_WIFI_OFF_TIME) || key.equals(Constants.PREF_KEY_WIFI_OFF_INTERVAL) )
            return Constants.NOTIFICATION_KEY_WIFI_OFF;

        if(key.equals(Constants.PREF_KEY_WIFI_ON_TIME) || key.equals(Constants.PREF_KEY_WIFI_ON_INTERVAL))
            return Constants.NOTIFICATION_KEY_WIFI_ON;

        if(key.equals(Constants.PREF_KEY_MOBILE_OFF_TIME) || key.equals(Constants.PREF_KEY_MOBILE_OFF_INTERVAL))
            return Constants.NOTIFICATION_KEY_MOBILE_OFF;

        if(key.equals(Constants.PREF_KEY_MOBILE_ON_TIME) || key.equals(Constants.PREF_KEY_MOBILE_ON_INTERVAL))
            return Constants.NOTIFICATION_KEY_MOBILE_ON;

        return -1;

    }

    public static String getNotifDoneFromKey(Context context, String key){
        if(context == null || key == null || key.isEmpty()) return "";


        if(key.equals(Constants.PREF_KEY_WIFI_OFF_TIME) || key.equals(Constants.PREF_KEY_WIFI_OFF_INTERVAL))
            return context.getString(R.string.notification_msg_turn_off_wifi_done);

        if(key.equals(Constants.PREF_KEY_WIFI_ON_TIME) || key.equals(Constants.PREF_KEY_WIFI_ON_INTERVAL))
            return context.getString(R.string.notification_msg_turn_on_wifi_done);

        if(key.equals(Constants.PREF_KEY_MOBILE_OFF_TIME) || key.equals(Constants.PREF_KEY_MOBILE_OFF_INTERVAL))
            return context.getString(R.string.notification_msg_turn_off_network_done);

        if(key.equals(Constants.PREF_KEY_MOBILE_ON_TIME) || key.equals(Constants.PREF_KEY_MOBILE_ON_INTERVAL))
            return context.getString(R.string.notification_msg_turn_on_network_done);

        return "";
    }

    public static String getNotifTitleInProgressFromKey(Context context, String key){
        if(context == null || key == null || key.isEmpty()) return "";

        if(key.equals(Constants.PREF_KEY_WIFI_OFF_TIME) || key.equals(Constants.PREF_KEY_WIFI_OFF_INTERVAL))
            return context.getString(R.string.notification_msg_turn_off_wifi);

        if(key.equals(Constants.PREF_KEY_WIFI_ON_TIME) || key.equals(Constants.PREF_KEY_WIFI_ON_INTERVAL))
            return context.getString(R.string.notification_msg_turn_on_wifi);

        if(key.equals(Constants.PREF_KEY_MOBILE_OFF_TIME) || key.equals(Constants.PREF_KEY_MOBILE_OFF_INTERVAL))
            return context.getString(R.string.notification_msg_turn_off_network);

        if(key.equals(Constants.PREF_KEY_MOBILE_ON_TIME) || key.equals(Constants.PREF_KEY_MOBILE_ON_INTERVAL))
            return context.getString(R.string.notification_msg_turn_on_network);

        return "";
    }

    public static String getNowTime(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("h:mm a");

        return dateTimeFormatter.print(initialDateTime);
    }

    public static void initTime(){
        initialDateTime = new DateTime();
        initialDateTime.plusMinutes(1);
    }

    public static boolean isAM(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("mm");
        return dateTimeFormatter.print(initialDateTime).equals("AM");
    }

    public static boolean isMobileNetworkEnable(Context context, String key){
        if(context == null || key == null || key.isEmpty()) return false;

        if(key.equals(Constants.PREF_KEY_MOBILE_OFF_TIME) || key.equals(Constants.PREF_KEY_MOBILE_OFF_INTERVAL))
            return false;

        if(key.equals(Constants.PREF_KEY_MOBILE_ON_TIME) || key.equals(Constants.PREF_KEY_MOBILE_ON_INTERVAL))
            return true;

        return false;

    }

    public static boolean isMobileNetworkEnabledStatus(Context context){
        if(context == null) return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class conmanClass;
        try {
            conmanClass = Class.forName(connectivityManager.getClass().getName());
            Field field = conmanClass.getDeclaredField("mService");
            field.setAccessible(true);
            Object object = field.get(connectivityManager);
            Class mClass = Class.forName(object.getClass().getName());
            Method setDataEnabledMethod = mClass.getDeclaredMethod("setMobileDataEnabled",Boolean.TYPE);
            setDataEnabledMethod.setAccessible(true);

            Method getDataEnabledMethod = mClass.getDeclaredMethod("getMobileDataEnabled");
            getDataEnabledMethod.setAccessible(true);
            boolean mobileDataEnabled = (Boolean) getDataEnabledMethod.invoke(object);

            return mobileDataEnabled;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean isMobileStatusEqual(Context context, boolean mobileStatusEnable){
        if(context == null) return false;

        return isMobileNetworkEnabledStatus(context) == mobileStatusEnable;
    }

    public static boolean isNetWorkOperatorAvailable(Context context){
        if(context == null) return false;

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        return telephonyManager.getNetworkOperator() != null && !telephonyManager.getNetworkOperator().isEmpty() && telephonyManager.getNetworkOperatorName() != null && !telephonyManager.getNetworkOperatorName().isEmpty();
    }

    public static boolean isWifiEnable(Context context, String key){
        if(context == null || key == null || key.isEmpty()) return false;

        if(key.equals(Constants.PREF_KEY_WIFI_OFF_TIME) || key.equals(Constants.PREF_KEY_WIFI_OFF_INTERVAL))
            return false;

        if(key.equals(Constants.PREF_KEY_WIFI_ON_TIME) || key.equals(Constants.PREF_KEY_WIFI_ON_INTERVAL))
            return true;

        return false;
    }

    public static boolean isWiFiEnabledStatus(Context context){
        if(context == null) return false;

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
    }

    public static boolean isWifiStatusEqual(Context context, int wifiStatusEnable){
        if(context == null) return false;

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getWifiState() == wifiStatusEnable;
    }

    public static void scheduleInternaEvent(Context context, int interval, String key){
        if(context == null || interval <= 0 || key == null) return;

        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusMinutes(interval);

        Intent intent = new Intent(context,BroadReceiver.class);
        intent.putExtra(Constants.EXTRA_KEY,key);
        intent.putExtra(Constants.EXTRA_TIME, dateTime.toString(Constants.NOTIFICATION_TIME_FORMAT));
        intent.putExtra(Constants.EXTRA_IS_TIME,false);

        PendingIntent sender = PendingIntent.getBroadcast(context,getKeyCode(key),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC,dateTime.getMillis(),sender);
    }

    public static void scheduleTimeEvent(Context context, String time, String key){
        if(context == null || time == null || key == null) return;

        String[] pieces = time.split(":");
        String hourPiece = pieces[0];
        String minPiece = pieces[1];

        DateTime dateTime = new DateTime();
        DateTime returnDateTime;

        if(Integer.valueOf(hourPiece) < dateTime.getHourOfDay() ||
                (Integer.valueOf(hourPiece) == dateTime.getHourOfDay() && Integer.valueOf(minPiece) < dateTime.getMinuteOfHour())
                ){
            dateTime = dateTime.plusDays(1);
        }

        returnDateTime = new DateTime(dateTime.getYear(),dateTime.getMonthOfYear(),dateTime.getDayOfMonth(),Integer.valueOf(hourPiece),Integer.valueOf(minPiece));

        Intent intent = new Intent(context, BroadReceiver.class);
        intent.putExtra(Constants.EXTRA_KEY,key);
        intent.putExtra(Constants.EXTRA_TIME,returnDateTime.toString(Constants.NOTIFICATION_TIME_FORMAT));
        intent.putExtra(Constants.EXTRA_IS_TIME,true);

        PendingIntent sender = PendingIntent.getBroadcast(context,getKeyCode(key), intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC,returnDateTime.getMillis(),sender);

    }


    public static void showNotifAndEnabledConnection(Context context, String key, String time){
        if(context == null || key == null || key.isEmpty() || time == null || time.isEmpty()) return;

        final long when = System.currentTimeMillis();
        final String title = getNotifTitleInProgressFromKey(context,key);
        final String titleWhenDone = getNotifDoneFromKey(context,key);
        final int notifKey = getNotificationIdFromKey(key);
        boolean isKeyWifi = true;
        if(!(notifKey == Constants.NOTIFICATION_KEY_WIFI_OFF || notifKey == Constants.NOTIFICATION_KEY_WIFI_ON))
            isKeyWifi = false;

        if(isKeyWifi){
            if(isWifiEnable(context,key) && isWiFiEnabledStatus(context)){
                return;
            }

            if(!isWifiEnable(context,key) && !isWiFiEnabledStatus(context)){
                return;
            }
        } else {
            if(isMobileNetworkEnable(context,key) && isMobileNetworkEnabledStatus(context)){
                return;
            }

            if(!isMobileNetworkEnable(context,key) && !isMobileNetworkEnabledStatus(context)){
                return;
            }
        }


        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(context);
        notifBuilder.setSmallIcon(R.drawable.notification_icon);
        notifBuilder.setTicker(title);
        notifBuilder.setContentTitle(title);
        notifBuilder.setWhen(when);
        notifBuilder.setOnlyAlertOnce(true);
        notifBuilder.setAutoCancel(true);
        notifBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifKey,notifBuilder.build());

        if(isKeyWifi)
            turnWiFiEnabled(context, isWifiEnable(context,key));
        else
            turnMobileNetworkEnabled(context, isMobileNetworkEnable(context, key));

        try{
            TimeUnit.MILLISECONDS.sleep(2500);
        }catch (InterruptedException e){
            Log.e("","Sleep has been intertupted");
        }

        notifBuilder.setTicker(titleWhenDone);
        notifBuilder.setContentTitle(titleWhenDone);
        notifBuilder.setContentText(time);

        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        notifBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(notifKey,notifBuilder.build());

        changeStatusSwitch(context, key);
    }

    public static void showShortToast(Context context, int textResID){
        if(context==null)return;

        Toast.makeText(context, context.getString(textResID), Toast.LENGTH_SHORT).show();
    }

    public static void turnMobileNetworkEnabled(Context context, boolean enabled){
        if(context == null) return;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class conmanClass;
        try {
            conmanClass = Class.forName(connectivityManager.getClass().getName());
            Field field = conmanClass.getDeclaredField("mService");
            field.setAccessible(true);
            Object object = field.get(connectivityManager);
            Class mClass = Class.forName(object.getClass().getName());
            Method setDataEnabledMethod = mClass.getDeclaredMethod("setMobileDataEnabled",Boolean.TYPE);
            setDataEnabledMethod.setAccessible(true);

            Method getDataEnabledMethod = mClass.getDeclaredMethod("getMobileDataEnabled");
            getDataEnabledMethod.setAccessible(true);
            boolean mobileDataEnabled = (Boolean) getDataEnabledMethod.invoke(object);

            if(enabled){
                if(!mobileDataEnabled)
                    setDataEnabledMethod.invoke(object,true);
            } else {
                if(mobileDataEnabled)
                    setDataEnabledMethod.invoke(object,false);
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }



    public static void turnWiFiEnabled(Context context, boolean enabled){
        if(context == null) return;

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if(enabled){
            if(!(wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED))
                wifiManager.setWifiEnabled(true);
        } else {
            if(!(wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED))
                wifiManager.setWifiEnabled(false);
        }
    }


}
