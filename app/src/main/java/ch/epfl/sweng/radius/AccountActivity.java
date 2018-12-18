package ch.epfl.sweng.radius;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;

import ch.epfl.sweng.radius.database.ChatlogsUtil;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.friends.FriendsFragment;
import ch.epfl.sweng.radius.home.HomeFragment;
import ch.epfl.sweng.radius.messages.MessagesFragment;
import ch.epfl.sweng.radius.profile.ProfileFragment;
import ch.epfl.sweng.radius.utils.NotificationUtility;

public class AccountActivity extends AppCompatActivity {
    private Fragment homeFragment;
    private Fragment messageFragment;
    private Fragment friendsFragment;
    private Fragment profileFragment;
    private Timer timer;
    public static class myTimer extends  TimerTask {

        public myTimer(){
            isSet = false;
        }

        public myTimer getInstance(){
            return new myTimer();
        }

        boolean isSet;

        public void setSet(boolean set) {
            isSet = set;
        }

        public boolean isSet(){
            return isSet;
        }

        @Override
        public void run() {
            leaveApp();

        }
    }
    private myTimer timerTask;

    public void initChannel(String channel_name, String channel_description) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(channel_name, channel_name, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription(channel_description); mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            mNotificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder msgNotif = new NotificationCompat.Builder(this, "radiusNotif");
        NotificationCompat.Builder reqNotif = new NotificationCompat.Builder(this, "radiusNotif");
        NotificationCompat.Builder nearFriendNotif = new NotificationCompat.Builder(this, "radiusNotif");

        NotificationUtility.getInstance(mNotificationManager, msgNotif, reqNotif, nearFriendNotif);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.setDefaultValues(this, R.xml.app_preferences, false);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean nightMode = settings.getBoolean("nightModeSwitch", false);
        if (nightMode)
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.LightTheme);
        super.onCreate(savedInstanceState);

        timer = new Timer();
        // To load the current user infos
        UserInfo.getInstance();//.fetchDataFromDB();
        OthersInfo.getInstance().fetchUsersInMyRadius();
        ChatlogsUtil.getInstance(getApplicationContext());
        // Set the layout
        setContentView(R.layout.activity_account);

        homeFragment = new HomeFragment();
        messageFragment = new MessagesFragment();
        friendsFragment = new FriendsFragment();
        profileFragment = new ProfileFragment();

        initChannel("radiusNotif", "radius notifications");
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
        enterApp();


    }

    @Override
    public void onStart(){
        super.onStart();
        if(timerTask == null)
            timerTask = new myTimer();
        else if(timerTask.isSet){
            timer.cancel();
            timer = new Timer();
            timerTask = new myTimer();
        }
        UserInfo.getInstance().updateLocationInDB();
    }

    @Override
    public void onStop() {
        super.onStop();

        timerTask.setSet(true);
        timer.schedule(timerTask, 20*60*1000);
    }

    private static void leaveApp(){
        UserInfo.getInstance().saveState();

        UserInfo.getInstance().getCurrentPosition().setVisible(false);
        UserInfo.getInstance().updateLocationInDB();
    }

    private void enterApp(){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean incognito = settings.getBoolean("incognitoSwitch", false);
        UserInfo.getInstance().getCurrentPosition().setVisible(!incognito);
        UserInfo.getInstance().setIncognitoMode(incognito);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        leaveApp();
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

        finish();

        return super.onOptionsItemSelected(item);
    }

}
