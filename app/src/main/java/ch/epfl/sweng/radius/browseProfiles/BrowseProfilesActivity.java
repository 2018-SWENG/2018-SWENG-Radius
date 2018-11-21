package ch.epfl.sweng.radius.browseProfiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

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
import ch.epfl.sweng.radius.utils.UserInfo;


public class BrowseProfilesActivity extends AppCompatActivity {
    //Might want to create and get the id of users along with their names.
    private BrowseProfilesUtility profileActivityListener;
    private Toolbar toolbar;
    private final Database database = Database.getInstance();
    private String userUID;

    // UI elements
    private ImageView userPhoto;
    private TextView textViewName;
    private TextView textViewStatus;
    private TextView textViewInterests;
    private TextView textViewLanguages;





    //THIS ACTIVITY HAS TO STORE THE ID OF THE USER WE ARE BROWSING THE PROFILE OF.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_profiles);
        Intent intent = getIntent();

        profileActivityListener = new BrowseProfilesUtility(intent.getStringExtra("Clicked Name")); // WHEN THE CLASS STORES THE ID OF THE USER WE
        // CLICKED ON CHANGE CLICKED NAME WITH THE ID

        // Initialize UI Components
        userUID = intent.getStringExtra("UID");
        userPhoto = findViewById(R.id.userPhoto);
        textViewName = findViewById(R.id.userNickname);
        textViewStatus = findViewById(R.id.userStatus);
        textViewInterests = findViewById(R.id.userInterests);
        textViewLanguages = findViewById(R.id.userLanguages);


        // Get the current user profile from the DB
        setUpAddFriendButton(UserInfo.current_user);
        setUpUIComponents(current_user);

        // ToolBar initialization
        toolbar = findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        //REMOVE THIS PART FOR DEMO----------------------------------------
        GroupLocationFetcher glf = new GroupLocationFetcher();
        Database.getInstance().readAllTableOnce(Database.Tables.LOCATIONS, glf);
    }

    public void setUpUIComponents(User current_user){
        /*
        byte[] decodedString = Base64.decode(current_user.getUrlProfilePhoto(), Base64.DEFAULT);
        Bitmap profilePictureUri = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        userPhoto.setImageBitmap(profilePictureUri);
        */
        userPhoto.setImageResource(R.drawable.ic_man);
        textViewName.setText(current_user.getNickname());
        textViewStatus.setText(current_user.getStatus());
        textViewInterests.setText(current_user.getInterests());
        textViewLanguages.setText(current_user.getSpokenLanguages());
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

    private void setUpAddFriendButton(final User profileUser){
        final Button addFriendButton = findViewById(R.id.add_user);

        // Disable the button if the current user is friend with this user
        database.readObjOnce(new User(database.getCurrent_user_id()),
                Database.Tables.USERS, new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        final User currentUser = (User)value;

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


                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase", error.getMessage());
                    }
                });
    }
}
