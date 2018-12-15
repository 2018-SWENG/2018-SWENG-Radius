package ch.epfl.sweng.radius;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.UserInfo;

public class PreferencesActivity extends PreferenceActivity {

    private static final String INCOGNITO = "incognitoSwitch";
    private static final String INVISIBLE = "You are currently invisible, nobody can see you in the map.";
    private static final String VISIBLE = "You are visible, people can see your location in the map.";
    private static final String DELETINGACCOUNTMESSAGE = "All your conversations and friends will be deleted.";
    private static boolean isVisible = true;

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

            //Initialize the preferences activity
            initializeIncognitoPreference();
            setupLogoutButton();
            setupDeleteAccountButton();
        }

        public void setupLogoutButton() {
            Preference logOutButton = findPreference("logOutButton");

            logOutButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    logOut();
                    return true;
                }
            });
        }

        private void setupDeleteAccountButton() {
            Preference deleteAccountButton = findPreference("deleteAccount");
            deleteAccountButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //Prepare alert dialog
                    AlertDialog.Builder dialog = setupAlertDialogBuilder();
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();

                    return true;
                }
            });
        }

        /*
        * Creates a pop up to ask the user if they want
        * to delete their account one more time and warn them.
        * */
        private AlertDialog.Builder setupAlertDialogBuilder() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("Are you sure?");
            dialog.setMessage(DELETINGACCOUNTMESSAGE);

            setupPositiveButton(dialog);
            setupNegativeButton(dialog);

            return dialog;
        }

        /*
        * Deletes account and takes the user to the sign in page
        * */
        private void setupPositiveButton(AlertDialog.Builder dialog) {
            dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Account Deleted", Toast.LENGTH_SHORT).show();
                                MLocation currentLocation = UserInfo.getInstance().getCurrentPosition();
                                currentLocation.setDeleted(true);
                                currentLocation.setTitle("Deleted User - " + currentLocation.getTitle());
                                UserInfo.getInstance().updateLocationInDB();
                                logOut(); // might just want to change this line with startActivity
                            } else {
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }

        /*
        * Cancels the operation to delete the account.
        * */
        private void setupNegativeButton(AlertDialog.Builder dialog) {
            dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        private void initializeIncognitoPreference() {
            isVisible = UserInfo.getInstance().getCurrentPosition().getVisible();
            SwitchPreference incognitoPref = (android.preference.SwitchPreference) findPreference(INCOGNITO);
            if (isVisible) {
                findPreference(INCOGNITO).setDefaultValue("false");
                incognitoPref.setSummaryOff(VISIBLE);
            } else {
                findPreference(INCOGNITO).setDefaultValue("true");
                incognitoPref.setSummaryOn(INVISIBLE);
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
            isVisible = !isVisible;
            UserInfo.getInstance().getCurrentPosition().setVisible(isVisible);
            UserInfo.getInstance().updateLocationInDB();
            SwitchPreference incognitoPref = (android.preference.SwitchPreference) findPreference(INCOGNITO);
            if (isVisible) {
                incognitoPref.setSummaryOff(VISIBLE);
            } else {
                incognitoPref.setSummaryOn(INVISIBLE);
            }
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
