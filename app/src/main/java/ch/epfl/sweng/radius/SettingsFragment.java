package ch.epfl.sweng.radius;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener{

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.app_preferences);
        Preference logOutButton = findPreference("logOutButton");
        logOutButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                logOut();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen()
                .getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    // TODO: New File with settings actions and call also in mainActivity
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        System.out.println(key);
        Log.println(Log.INFO,"Settings","change");

        switch (key){
            case "incognitoSwitch": // TODO: set the incognito Mode
                Preference pref = findPreference(key);
                Log.println(Log.INFO,"Settings", String.valueOf((sharedPreferences.getBoolean(key, false))));
            case "notificationCheckbox": // TODO: set the notifications On/Off
                Log.println(Log.INFO,"Settings","notification");
                break;
            case "nightModeSwitch": // TODO: set the night Mode
                Log.println(Log.INFO,"Settings","night mode");
                break;
        }
    }

    private void logOut() {
        if (LoginActivity.googleSignInClient != null) {
            FirebaseAuth.getInstance().signOut();
            LoginActivity.googleSignInClient.signOut()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            revokeAccess();
                        }
                    });
        }
    }

    private void revokeAccess() {
        LoginActivity.googleSignInClient.revokeAccess()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                });
    }
}
