package ch.epfl.sweng.radius.database;

import android.os.Environment;
import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public  class UserInfo extends DBObservable implements Serializable{
    private static final String SAVE_PATH = "current_user_info.data";
    private static UserInfo userInfo = loadState();
    private static final Database database = Database.getInstance();

    private static User current_user = new User(Database.getInstance().getCurrent_user_id(),
            "", "");
    private static MLocation current_position = new MLocation(Database.getInstance().getCurrent_user_id());

    public static UserInfo getInstance(){
        if (userInfo == null)
            userInfo = new UserInfo();
        return userInfo;
    }

    private UserInfo(){
    }

    public void fetchDataFromDB(){
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


    public void saveState(){
        ObjectOutput out;
        try {
            File outFile = new File(Environment.getExternalStorageDirectory(), SAVE_PATH);
            out = new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(this);
            out.close();
        } catch (Exception e) {e.printStackTrace();}
    }

    private static UserInfo loadState(){
        ObjectInput in;
        UserInfo savedUserInfo=null;
        try {
            File inFile = new File(Environment.getExternalStorageDirectory(), SAVE_PATH);
            in = new ObjectInputStream(new FileInputStream(inFile));
            savedUserInfo = (UserInfo) in.readObject();
            in.close();
        } catch (Exception e) {e.printStackTrace();}
        Log.e("SAVE STATE", "Loading the state");
        return savedUserInfo;
    }

    public void updateUserInDB(){
        Database.getInstance().writeInstanceObj(current_user, Database.Tables.USERS);
    }

    public void updateLocationInDB(){
        Database.getInstance().writeInstanceObj(current_position, Database.Tables.LOCATIONS);
    }



}
