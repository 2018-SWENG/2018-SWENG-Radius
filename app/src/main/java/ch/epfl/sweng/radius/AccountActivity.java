package ch.epfl.sweng.radius;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.FirebaseUtility;

public class AccountActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private Fragment homeFragment;
    private Fragment messageFragment;
    private Fragment friendsFragment;
    private Fragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.app_preferences, false);

        // Read the current User from the database

/*
        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println(userUID);
        User current_user = new User("userTest00");
        FirebaseUtility fbUserUtility = new FirebaseUtility(current_user, "users");

        if (fbUserUtility.isNew()){
            System.out.println("isNew");
        } else
            System.out.println("isNotNew");



        System.out.println("fbUtilityCreated");
        if (fbUserUtility.isNew()){
            System.out.println("isNew");
            fbUserUtility.writeInstanceObj();
        }
        else {
            System.out.println("isNotNew");
            try {
                fbUserUtility.readObj();
                System.out.println(((User)fbUserUtility.getInstance()).getStatus());
            } catch (Exception e){
                System.err.println("Error during database access");
                e.printStackTrace();
            }
        }
        */




        // Set the layout
        setContentView(R.layout.activity_account);

        if (savedInstanceState != null) {
            homeFragment = getSupportFragmentManager().getFragment(savedInstanceState, "homeFragment");
            friendsFragment = getSupportFragmentManager().getFragment(savedInstanceState, "friendsFragment");
            messageFragment = getSupportFragmentManager().getFragment(savedInstanceState, "messageFragment");
            profileFragment = getSupportFragmentManager().getFragment(savedInstanceState, "profileFragment");
        }
        else {
            homeFragment = new HomeFragment();
            messageFragment = new MessagesFragment();
            friendsFragment = new FriendsFragment();
            profileFragment = new ProfileFragment();
        }

        loadFragment(homeFragment);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView
                                                            .OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        //fragment = new HomeFragment();
                        loadFragment(homeFragment);
                        break;
                    case R.id.navigation_messages:
                        //fragment = new MessagesFragment();
                        loadFragment(messageFragment);
                        break;
                    case R.id.navigation_settings:
                        //fragment = new SettingsFragment();
                        loadFragment(friendsFragment);
                        break;
                    case R.id.navigation_profile:
                        //fragment = new ProfileFragment();
                        loadFragment(profileFragment);
                        break;
                    default:
                        System.out.println("Unknown item id selected: " + item.getItemId());
                }
                return true;
            }
        });

        // ToolBar initialization
        toolbar = findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        if (homeFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, "homeFragment", homeFragment);//(outState, "myFragmentName", mContent);
        if (messageFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, "messageFragment", messageFragment);
        if (friendsFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, "settingsFragment", friendsFragment);
        if (profileFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, "profileFragment", profileFragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fcontainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, PreferencesActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


}
