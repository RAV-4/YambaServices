package com.example.rafael.yamba;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.marakana.android.yamba.clientlib.YambaClient;

public class SettingsActivity extends Activity{

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    // Check whether this activity was created before
        if (savedInstanceState == null) {

        // Create a fragment
            SettingsFragment fragment = new SettingsFragment(); //
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment,
                            fragment.getClass().getSimpleName())
                    .commit(); //
        }
    };


}