package ch.epfl.sweng.radius.browseProfiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.GroupLocationFetcher;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.BrowseProfilesUtility;


public class BrowseProfilesActivity extends AppCompatActivity {
    //Might want to create and get the id of users along with their names.
    private BrowseProfilesUtility profileActivityListener;
    private Toolbar toolbar;

    //THIS ACTIVITY HAS TO STORE THE ID OF THE USER WE ARE BROWSING THE PROFILE OF.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_profiles);
        Intent intent = getIntent();
        int clickedPic = intent.getIntExtra("Clicked Picture",2131230869); // The default value is the id
        // associated with the john doe picture - I set it to that instead of 1 because we actually don't have a resource
        // numbered 1. As a tests can't initialize the activity.
        String clickedName = intent.getStringExtra("Clicked Name");

        profileActivityListener = new BrowseProfilesUtility(clickedName); // WHEN THE CLASS STORES THE ID OF THE USER WE
        // CLICKED ON CHANGE CLICKED NAME WITH THE ID


        String userUID = intent.getStringExtra("UID");
        setUpAddFriendButton(userUID);

        ImageView imageView = findViewById(R.id.clickedPic);
        System.out.println("clickedPic " + clickedPic);
        imageView.setImageResource(clickedPic);
        TextView textViewName = findViewById(R.id.clickedName);
        textViewName.setText(clickedName);


        // ToolBar initialization
        toolbar = findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        GroupLocationFetcher glf = new GroupLocationFetcher();
        glf.setCurrentUserLoc();
        Database.getInstance().readAllTableOnce(Database.Tables.LOCATIONS, glf);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.block_user:
                Toast.makeText(this, "User:" + profileActivityListener.getProfileOwner() +
                        " is blocked.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.spam:
                Toast.makeText(this, "User:" + profileActivityListener.getProfileOwner() +
                        " is reported for spam.", Toast.LENGTH_SHORT).show();
                profileActivityListener.reportUser("spam");
                return true;
            case R.id.language:
                Toast.makeText(this, "User:" + profileActivityListener.getProfileOwner() +
                        " is reported for language.", Toast.LENGTH_SHORT).show();
                profileActivityListener.reportUser("language");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpAddFriendButton(final String userUID){
        final Database database = Database.getInstance();
        final Button addFriendButton = findViewById(R.id.add_user);

        // Disable the button if the current user is friend with this user
        database.readListObjOnce(Arrays.asList(database.getCurrent_user_id(), userUID),
                Database.Tables.USERS, new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        ArrayList<User> users = ((ArrayList<User>) value);
                        if (users.size() == 2) {
                            final User currentUser = users.get(0).getID()
                                    .equals(database.getCurrent_user_id()) ? users.get(0) : users.get(1);
                            final User profileUser = users.get(0).getID()
                                    .equals(database.getCurrent_user_id()) ? users.get(1) : users.get(0);

                            if (currentUser.getFriends().contains(profileUser.getID())) {
                                addFriendButton.setText("Already friends");
                                addFriendButton.setEnabled(false);
                            }
                            if (currentUser.getFriendsRequests().contains(profileUser.getID())) {
                                addFriendButton.setText("Request sent");
                                addFriendButton.setEnabled(false);
                            }

                            addFriendButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    currentUser.addFriendRequest(profileUser);
                                    database.writeInstanceObj(currentUser, Database.Tables.USERS);
                                    database.writeInstanceObj(profileUser, Database.Tables.USERS);
                                    addFriendButton.setText("Request sent");
                                    addFriendButton.setEnabled(false);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase", error.getMessage());
                    }
                });
    }
}
