package ch.epfl.sweng.radius.utils;
import android.util.Log;
import com.google.firebase.database.DatabaseError;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;

public  class UserInfo {
    private static UserInfo userInfo = null;
    private static final Database database = Database.getInstance();
    private User current_user = new User(Database.getInstance().getCurrent_user_id(),
            "", "");

    public static UserInfo getInstance(){
        if (userInfo == null)
            userInfo = new UserInfo();
        return userInfo;
    }

    private UserInfo(){
        fetchCurrentUser();
    }

    public User getCurrentUser(){
        return current_user;
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

}
