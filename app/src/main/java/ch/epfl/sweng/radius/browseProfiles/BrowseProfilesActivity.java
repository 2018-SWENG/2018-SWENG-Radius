package ch.epfl.sweng.radius.browseProfiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.DatabaseObject;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.CustomListItem;

public class BrowseProfilesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_profiles);
        Intent intent = getIntent();
        int clickedPic = intent.getIntExtra("Clicked Picture",1);
        String clickedName = intent.getStringExtra("Clicked Name");
        String userUID = intent.getStringExtra("UID");
        setUpAddFriendButton(userUID);
        ImageView imageView = findViewById(R.id.clickedPic);
        imageView.setImageResource(clickedPic);
        TextView textViewName = findViewById(R.id.clickedName);
        textViewName.setText(clickedName);
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
                        final User currentUser = users.get(0).getID()
                               .equals(database.getCurrent_user_id()) ? users.get(0) : users.get(1);
                        final User profileUser = users.get(0).getID()
                               .equals(database.getCurrent_user_id()) ? users.get(1) : users.get(0);

                        if(currentUser.getFriends().contains(profileUser.getID())){
                            addFriendButton.setText("Already friends");
                            addFriendButton.setEnabled(false);
                        }
                        if(currentUser.getFriendsRequests().contains(profileUser.getID())){
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
