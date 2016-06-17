package com.mustafa.silentplease;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mustafa.silentplease.fragments.PreferenceFragment;

public class MainActivity extends AppCompatActivity {

//    private enum TIME_OR_INTERVAL {
//        NORMAL, TIME, INTERVAL
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(android.R.id.content,new PreferenceFragment()).commit();
    }



}
