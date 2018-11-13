package ch.epfl.sweng.radius.utils;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;

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
                user.addReport(database.getCurrent_user_id(), reportReason);
                database.writeInstanceObj(user, Database.Tables.USERS);
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase Error", error.getMessage());
            }
        };

        database.readObjOnce(user, Database.Tables.USERS, cb);
    }

    public String getProfileOwner() {
        return profileOwner;
    }
}
