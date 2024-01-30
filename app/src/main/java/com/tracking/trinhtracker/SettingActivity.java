package com.tracking.trinhtracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import androidx.preference.PreferenceManager;

public class SettingActivity extends AppCompatActivity {

    private static final String SMS_ALERT_KEY = "SMS_alert";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat
            implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            SwitchPreferenceCompat smsAlertPreference = findPreference(SMS_ALERT_KEY);
            if (smsAlertPreference != null) {
                smsAlertPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    boolean isSmsAlertEnabled = (boolean) newValue;

                    // Save the state to SharedPreferences
                    saveSmsAlertState(isSmsAlertEnabled);

                    // Show a Toast
                    showToast(isSmsAlertEnabled);

                    return true;
                });
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            // Handle other preference changes if needed
        }

        private void saveSmsAlertState(boolean isSmsAlertEnabled) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(SMS_ALERT_KEY, isSmsAlertEnabled);
            editor.apply();
        }

        private void showToast(boolean isSmsAlertEnabled) {
            String message = isSmsAlertEnabled ? "SMS is enabled" : "SMS is disabled";
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}

