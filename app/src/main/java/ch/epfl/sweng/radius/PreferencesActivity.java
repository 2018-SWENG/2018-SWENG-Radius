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

import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import com.google.firebase.database.DatabaseError;

public class PreferencesActivity extends PreferenceActivity {

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

        // TODO: New File with settings actions and call also in loginActivity
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            //System.out.println(key);
            //Log.println(Log.INFO,"Settings","change");

            switch (key){
                case "incognitoSwitch": // TODO: set the incognito Mode

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

        /**
        private void changeInvisibility() {
            final Database database = Database.getInstance();
            database.readObjOnce(new User(database.getCurrent_user_id()),
                    Database.Tables.USERS, new CallBackDatabase() {
                        @Override
                        public void onFinish(Object value) {
                            User currentUser = (User) value;
                            boolean isİnvisible = currentUser.isVisible();
                            database.
                        }

                        @Override
                        public void onError(DatabaseError error) {
                            Log.e("Firebase Error", error.getMessage());
                        }
                    });
        }
         */

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

}
