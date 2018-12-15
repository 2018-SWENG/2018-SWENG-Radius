package ch.epfl.sweng.radius.browseProfiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.BrowseProfilesUtility;


public class BrowseProfilesUnblockedActivity extends BrowseProfilesActivity{

    private final Database database = Database.getInstance();
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
    }


    void fetchUserInfo(String userUID){

        final MLocation targetUser = OthersInfo.getInstance().getUsersInRadius().get(userUID) != null ?
                OthersInfo.getInstance().getUsersInRadius().get(userUID):
                OthersInfo.getInstance().getConvUsers().get(userUID);

        database.readObjOnce(new User(userUID), Database.Tables.USERS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                // Get the current user profile from the DB
                User current_profile = (User) value;
                setUpAddFriendButton(current_profile);
                setUpUIComponents(targetUser);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
        }

    public void setUpUIComponents(MLocation current_user){
        if (current_user != null && current_user.getUrlProfilePhoto() != null && !current_user.getUrlProfilePhoto().equals("")) {
            Picasso.get().load(current_user.getUrlProfilePhoto()).into(userPhoto);
            textViewName.setText(current_user.getTitle());
            textViewStatus.setText(current_user.getMessage());
            textViewInterests.setText(current_user.getInterests());
            textViewLanguages.setText(current_user.getSpokenLanguages());
        } else {
            userPhoto.setImageResource(R.drawable.user_photo_default);
        }
    }

    private void setUpAddFriendButton(final User profileUser){
        final Button addFriendButton = findViewById(R.id.add_user);
        final User currentUser = UserInfo.getInstance().getCurrentUser();
        if (OthersInfo.getInstance().getAllUserLocations().containsKey(profileUser.getID())
                && OthersInfo.getInstance().getAllUserLocations().get(profileUser.getID()).getDeleted()) {
            addFriendButton.setText("This User Is Deleted");
            addFriendButton.setEnabled(false);
            return;
        }
        if (currentUser.getFriends().containsKey(profileUser.getID())) {
            addFriendButton.setText("Remove friend"); addFriendButton.setEnabled(true);
        }
        else if (currentUser.getFriendsRequests().containsKey(profileUser.getID())) {
            addFriendButton.setText("Request sent"); addFriendButton.setEnabled(false);
        }
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser.getFriends().containsKey(profileUser.getID())) {
                    currentUser.removeFriend(profileUser);
                    addFriendButton.setText("Removed !");
                    addFriendButton.setEnabled(false);
                } else {
                    currentUser.addFriendRequest(profileUser);
                    UserInfo.getInstance().updateUserInDB();
                    database.writeInstanceObj(profileUser, Database.Tables.USERS);
                    addFriendButton.setText("Request sent"); addFriendButton.setEnabled(false);
                }
            }
        });
    }
}
