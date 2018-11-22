package ch.epfl.sweng.radius.database;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

public  class UserInfo extends DBObservable{
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
        database.readObj(current_user, Database.Tables.USERS, new CallBackDatabase() {
            @Override
            public void onFinish(Object user) {
                current_user = (User) user;
                notifyUserObservers(Database.Tables.USERS.toString());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("FetchUserFromFirebase", error.getMessage());
            }
        });
    }

    private void fetchUserPosition(){
        database.readObj(current_position, Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object loc) {
                current_position = (MLocation) loc;
                notifyLocactionObservers(Database.Tables.LOCATIONS.toString());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("FetchMLocFromFirebase", error.getMessage());
            }
        });
    }

}
