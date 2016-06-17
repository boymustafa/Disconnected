package com.mustafa.silentplease.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.mustafa.silentplease.utils.Utils;

/**
 * Created by Mustafa.Gamesterz on 25/05/16.
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!=null){
            Utils.changeStatus(context);
        }

    }
}
