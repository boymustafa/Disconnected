package com.mustafa.silentplease.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mustafa.silentplease.utils.Constants;
import com.mustafa.silentplease.utils.Utils;

/**
 * Created by Mustafa.Gamesterz on 24/05/16.
 */
public class BroadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.showNotifAndEnabledConnection(context, intent.getStringExtra(Constants.EXTRA_KEY), intent.getStringExtra(Constants.EXTRA_TIME));
        Utils.disableSwitchAfterAct(context, intent.getStringExtra(Constants.EXTRA_KEY));
    }
}
