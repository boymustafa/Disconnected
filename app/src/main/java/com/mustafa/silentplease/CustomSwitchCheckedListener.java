package com.mustafa.silentplease;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by Mustafa.Gamesterz on 25/05/16.
 */
public class CustomSwitchCheckedListener implements CompoundButton.OnCheckedChangeListener {

    protected final Context context;
    private Switch aSwitch;
    private String key;

    public CustomSwitchCheckedListener(Context context, Switch aSwitch, String key){
        this.context = context;
        this.key = key;

        setSwitch(aSwitch);
        resume();
    }

    public void setSwitch(Switch aSwitch){
        if(aSwitch==null) return;

        if(this.aSwitch == null)
            this.aSwitch = aSwitch;
    }

    public void resume(){
        this.aSwitch.setOnCheckedChangeListener(this);
        this.aSwitch.setChecked(isSwitchON());
    }

    public boolean isSwitchON(){
        SharedPreferences preferences;
        preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        return preferences.getBoolean(key,false);
    }

    public void pause(){
        this.aSwitch.setOnCheckedChangeListener(null);
    }

    public void updatePreference(){
        boolean isON = isSwitchON();
        if(this.aSwitch.isChecked() != isON)
            this.aSwitch.setChecked(isSwitchON());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences preferences;
        SharedPreferences.Editor editor;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();

        editor.putBoolean(key,isChecked);
        editor.commit();
    }
}
