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

import com.google.firebase.database.DatabaseError;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.FirebaseUtility;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.friends.FriendsFragment;
import ch.epfl.sweng.radius.home.HomeFragment;
import ch.epfl.sweng.radius.messages.MessagesFragment;
import ch.epfl.sweng.radius.profile.ProfileFragment;

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
        FirebaseUtility database = new FirebaseUtility("users");

        User user1 = new User("testUser1");
        User user2 = new User("testUser2");
        User user3 = new User("testUser3");
        User user4 = new User("testUser4");
        User current_user = new User(database.getCurrent_user_id());

        user1.addFriendRequest(user2);
        user1.addFriendRequest(user3);
        user2.addFriendRequest(user1);
        user2.addFriendRequest(user4);
        user4.addFriendRequest(user2);
        user4.addFriendRequest(current_user);
        user2.addFriendRequest(current_user);
        user3.addFriendRequest(current_user);

        current_user.addFriendRequest(user2);
        current_user.addFriendRequest(user3);

        database.writeInstanceObj(current_user);
        database.writeInstanceObj(user1);
        database.writeInstanceObj(user2);
        database.writeInstanceObj(user3);
        database.writeInstanceObj(user4);
        /*
        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUtility database = new FirebaseUtility("users");
        User currentUser = new User(userUID);
        database.readObjOnce(currentUser, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                User userStoredInTheDB = (User)value;
                System.out.println(userStoredInTheDB.getNickname());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase Error", error.getMessage());
            }
        });
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
                    case R.id.navigation_friends:
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
