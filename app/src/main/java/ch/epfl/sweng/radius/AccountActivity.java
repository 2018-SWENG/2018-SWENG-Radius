package ch.epfl.sweng.radius;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class AccountActivity extends AppCompatActivity {

    private Fragment homeFragment;
    private Fragment messageFragment;
    private Fragment settingsFragment;
    private Fragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        if (savedInstanceState != null) {
            homeFragment = getSupportFragmentManager().getFragment(savedInstanceState, "homeFragment");
            settingsFragment = getSupportFragmentManager().getFragment(savedInstanceState, "settingsFragment");
            messageFragment = getSupportFragmentManager().getFragment(savedInstanceState, "messageFragment");
            profileFragment = getSupportFragmentManager().getFragment(savedInstanceState, "profileFragment");
        }
        else {
            homeFragment = new HomeFragment();
            messageFragment = new MessagesFragment();
            settingsFragment = new SettingsFragment();
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
                        loadFragment(settingsFragment);
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "homeFragment", homeFragment);//(outState, "myFragmentName", mContent);
        getSupportFragmentManager().putFragment(outState, "messageFragment", messageFragment);
        getSupportFragmentManager().putFragment(outState, "settingsFragment", settingsFragment);
        getSupportFragmentManager().putFragment(outState, "profileFragment", profileFragment);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fcontainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
