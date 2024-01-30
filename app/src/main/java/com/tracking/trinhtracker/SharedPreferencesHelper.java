package com.tracking.trinhtracker;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public class SharedPreferencesHelper {

    private static final String SMS_ALERT_KEY = "SMS_alert";

    //get share preferences
    public static boolean getSmsAlertPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(SMS_ALERT_KEY, false); // Default value is false
    }
}
