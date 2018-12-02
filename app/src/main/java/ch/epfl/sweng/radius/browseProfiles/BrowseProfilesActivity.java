package ch.epfl.sweng.radius.browseProfiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.GroupLocationFetcher;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.BrowseProfilesUtility;


public class BrowseProfilesActivity extends AppCompatActivity{
    //Might want to create and get the id of users along with their names.
    private BrowseProfilesUtility profileActivityListener;
    private Toolbar toolbar;
    private final Database database = Database.getInstance();
    private String userUID;
    private String userNickname;

    // UI elements
    private ImageView userPhoto;
    private TextView textViewName;
    private TextView textViewStatus;
    private TextView textViewInterests;
    private TextView textViewLanguages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_profiles);
        Intent intent = getIntent();

        //profileActivityListener = new BrowseProfilesUtility(intent.getStringExtra("Clicked Name")); // WHEN THE CLASS STORES THE ID OF THE USER WE
        // CLICKED ON CHANGE CLICKED NAME WITH THE ID

        // Initialize UI Components
        userNickname = intent.getStringExtra("Clicked Name");
        userUID = intent.getStringExtra("UID");
        profileActivityListener = new BrowseProfilesUtility(userUID);
        userPhoto = findViewById(R.id.userPhoto);
        textViewName = findViewById(R.id.userNickname);
        textViewStatus = findViewById(R.id.userStatus);
        textViewInterests = findViewById(R.id.userInterests);
        textViewLanguages = findViewById(R.id.userLanguages);
        toolbar = findViewById(R.id.toolbar);

        fetchUserInfo(userUID);

        // ToolBar initialization
        setSupportActionBar(toolbar);
    }

    void fetchUserInfo(String userUID){
        database.readObjOnce(new User(userUID), Database.Tables.USERS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                // Get the current user profile from the DB
                User current_profile = (User) value;
                setUpAddFriendButton(current_profile);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
        MLocation targetUser = OthersInfo.getInstance().getUsersInRadius().get(userUID);

        if(targetUser == null)
            targetUser = OthersInfo.getInstance().getConvUsers().get(userUID);

        setUpUIComponents(targetUser);
    }

    public void setUpUIComponents(MLocation current_user){
        if (current_user.getUrlProfilePhoto() != null && !current_user.getUrlProfilePhoto().equals("")) {
            Picasso.get().load(current_user.getUrlProfilePhoto()).into(userPhoto);
        } else {
            userPhoto.setImageResource(R.drawable.user_photo_default);
        }
        textViewName.setText(current_user.getTitle());
        textViewStatus.setText(current_user.getMessage());
        textViewInterests.setText(current_user.getInterests());
        textViewLanguages.setText(current_user.getSpokenLanguages());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        if (UserInfo.getInstance().getCurrentUser().getBlockedUsers().contains(userUID)) {
            menu.getItem(0).getSubMenu().getItem(0).setTitle("Unblock User");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.block_user:
                if (item.getTitle().toString().trim().equals("Block User")) {
                    profileActivityListener.blockUser();
                    item.setTitle("Unblock user");
                } else if (item.getTitle().toString().trim().equals("Unblock User")) {
                    profileActivityListener.unblockUser();
                    item.setTitle("Block User");
                }
                return true;
            case R.id.spam:
                profileActivityListener.reportUser("spam");
                return true;
            case R.id.language:
                profileActivityListener.reportUser("language");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpAddFriendButton(final User profileUser){
        final Button addFriendButton = findViewById(R.id.add_user);
        final User currentUser = UserInfo.getInstance().getCurrentUser();

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
                UserInfo.getInstance().updateUserInDB();
                database.writeInstanceObj(profileUser, Database.Tables.USERS);
                addFriendButton.setText("Request sent");
                addFriendButton.setEnabled(false);
            }
        });
    }
}
