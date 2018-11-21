package ch.epfl.sweng.radius.utils;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.User;

public  class UserInfo {
    private static UserInfo userInfo = null;
    private static final Database database = Database.getInstance();


    private User current_user = new User(Database.getInstance().getCurrent_user_id(),
            "", "");
    private MLocation current_position = new MLocation(Database.getInstance().getCurrent_user_id());

    public static UserInfo getInstance(){
        if (userInfo == null)
            userInfo = new UserInfo();
        return userInfo;
    }

    private UserInfo(){
        fetchCurrentUser();
        fetchUserPosition();
    }

    public User getCurrentUser(){
        return current_user;
    }

    public MLocation getCurrentPosition(){
        return current_position;
    }

    private void fetchCurrentUser(){
        database.readObjOnce(current_user, Database.Tables.USERS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                current_user = (User) value;
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("FetchUserFromFirebase", error.getMessage());
            }
        });
    }

    private void fetchUserPosition(){
        database.readObjOnce(current_position, Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                current_position = (MLocation) value;
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("FetchMLocFromFirebase", error.getMessage());
            }
        });
    }

}
