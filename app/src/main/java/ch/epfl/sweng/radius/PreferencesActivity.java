package ch.epfl.sweng.radius;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.utils.UserInfos;

public class PreferencesActivity extends PreferenceActivity {

    private static final String INCOGNITO = "incognitoSwitch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment(), "preferencesFragment").commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
    {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            // Load the Preferences from the XML file
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.app_preferences);
            Preference logOutButton = findPreference("logOutButton");
            initializeIncognitoPreference();
            logOutButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    logOut();
                    return true;
                }
            });
        }

        private void initializeIncognitoPreference() {
            boolean isInvisible = UserInfos.getCurrentUser().isVisible();
            if (isInvisible) {
                findPreference(INCOGNITO).setDefaultValue("false");
            } else {
                findPreference(INCOGNITO).setDefaultValue("true");
            }
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
            //System.out.println(key);
            //Log.println(Log.INFO,"Settings","change");

            switch (key){
                case "incognitoSwitch": // TODO: set the incognito Mode
                    changeInvisibility();
                    //Preference pref = findPreference(key);
                    //Log.println(Log.INFO,"Settings", String.valueOf((sharedPreferences.getBoolean(key, false))));
                    break;
                case "notificationCheckbox": // TODO: set the notifications On/Off
                    //Log.println(Log.INFO,"Settings","notification");
                    break;
                case "nightModeSwitch": // TODO: set the night Mode
                    //Log.println(Log.INFO,"Settings","night mode");
                    break;
            }
        }

        private void changeInvisibility() {
            boolean isInvisible = UserInfos.getCurrentUser().isVisible();
            UserInfos.getCurrentUser().setVisibility(!isInvisible);
            Database.getInstance().writeInstanceObj(UserInfos.getCurrentUser(), Database.Tables.USERS);
        }

        private void logOut() {
            if (MainActivity.googleSignInClient != null) {
                FirebaseAuth.getInstance().signOut();
                MainActivity.googleSignInClient.signOut()
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                revokeAccess();
                            }
                        });
            }
        }

        private void revokeAccess() {
            MainActivity.googleSignInClient.revokeAccess()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        }
                    });
        }
    }

}
