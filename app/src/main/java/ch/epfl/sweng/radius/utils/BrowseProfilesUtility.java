package ch.epfl.sweng.radius.utils;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;

public class BrowseProfilesUtility {

    private String profileOwner;
    private User user;
    private final Database database = Database.getInstance();

    public BrowseProfilesUtility(String profileOwnerID) {
        this.profileOwner = profileOwnerID;
        user = new User(profileOwnerID);
    }

    public void reportUser(final String reportReason) {
        CallBackDatabase cb = new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                user = (User) value;
                user.addReport(UserInfo.getInstance().getCurrentUser().getID(), reportReason);
                database.writeInstanceObj(user, Database.Tables.USERS);
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase Error", error.getMessage());
            }
        };

        database.readObjOnce(user, Database.Tables.USERS, cb);
    }

    public void blockUser() {
        User currentUser = UserInfo.getInstance().getCurrentUser();
        ArrayList<String> blockedUsers = (ArrayList<String>) currentUser.getBlockedUsers(); //.add(profileOwner);

        if (!blockedUsers.contains(profileOwner)) {
            blockedUsers.add(profileOwner);
            UserInfo.getInstance().updateUserInDB();
        }
    }

    public void unblockUser() {
        User currentUser = UserInfo.getInstance().getCurrentUser();
        ArrayList<String> blockedUsers = (ArrayList<String>) currentUser.getBlockedUsers(); //.add(profileOwner);

        if (blockedUsers.contains(profileOwner)) {
            blockedUsers.remove(profileOwner);
            UserInfo.getInstance().updateUserInDB();
        }
    }

    public String getProfileOwner() {
        return profileOwner;
    }
}
