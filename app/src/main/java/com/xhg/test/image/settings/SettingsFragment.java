package com.xhg.test.image.settings;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xhg.test.image.R;

/**
 * @author xionghg
 * @created 17-7-18.
 */

public class SettingsFragment extends PreferenceFragment {
    private static final String TAG = "SettingsFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        initListeners();
    }

    private void initListeners() {
        final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager()
                .findPreference(getString(R.string.save_net_mode));

        checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = Boolean.valueOf(newValue.toString());
                Log.d(TAG, "Pref " + preference.getKey() + " changed to " + newValue.toString());
                return true;
            }
        });
    }
}
