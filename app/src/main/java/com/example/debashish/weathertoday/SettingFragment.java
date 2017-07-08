package com.example.debashish.weathertoday;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.example.debashish.weathertoday.data.WeatherContract;
import com.example.debashish.weathertoday.sync.SyncUtils;

public class SettingFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.setting_fragment);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen =  getPreferenceScreen();
        int countPreference = preferenceScreen.getPreferenceCount();

        for(int i = 0; i < countPreference ; i++)
        {
            Preference p = preferenceScreen.getPreference(i);
            setPreferenceSummry(p , sharedPreferences.getString(p.getKey() , ""));


        }

    }

    public void setPreferenceSummry(Preference preference , String value)
    {
       if (preference instanceof ListPreference)
       {
           ListPreference listPreference = (ListPreference) preference;
           int valueIndex = listPreference.findIndexOfValue(value);
           preference.setSummary(listPreference.getEntries()[valueIndex]);
       }else if (preference instanceof EditTextPreference)
       {
           preference.setSummary(value);
       }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference preference = findPreference(key);
        setPreferenceSummry(preference , sharedPreferences.getString(preference.getKey() , ""));
        Activity activity = getActivity();

        if (key.equals(getString(R.string.pref_location_key))) {
            SyncUtils.startImmediateSync(activity);
        }
        else if (key.equals(getString(R.string.pref_units_key))) {
            // units have changed. update lists of weather entries accordingly
            activity.getContentResolver().notifyChange(WeatherContract.WeatherEntry.CONTENT_URI, null);
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
