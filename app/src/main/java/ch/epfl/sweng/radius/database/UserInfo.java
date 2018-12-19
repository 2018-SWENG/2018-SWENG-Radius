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

    private User current_user;
    private MLocation current_position;
    private boolean incognitoMode;

    /**
     * Static singleton method to return the instance of UserInfo
     * @return userInfo: the singleton instance of UserInfo
     */
    public static UserInfo getInstance(){
        if (userInfo == null) {
            userInfo = new UserInfo();
        }
        return userInfo;
    }

    //Private constructor since UserUtils is singleton
    private UserInfo(){
        current_user = new User(Database.getInstance().getCurrent_user_id());
        current_position = new MLocation(Database.getInstance().getCurrent_user_id());
        fetchDataFromDB();
    }

    /**
     * Setter for incognitoMode variable
     * @param incognitoMode: boolean value to set incognitoMode variable
     */
    public void setIncognitoMode(boolean incognitoMode) {
        this.incognitoMode = incognitoMode;
    }

    /**
     * Calls fetch methods for user and position
     */
    public void fetchDataFromDB(){
        fetchCurrentUser();
        fetchUserPosition();
    }

    /**
     * Getter for current user
     * @return current_user: The current user instance
     */
    public User getCurrentUser(){
        return current_user;
    }

    /**
     * Getter for current position
     * @return current_psoition: The current position instance
     */
    public MLocation getCurrentPosition(){
        return current_position;
    }

    /**
     * Sets the current_user variable as the current user instance from the database
     */
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

    /**
     * Resets all the data regarding the UserInfo instance
     */
    public void resetCurrentData(){
        this.removeAllObservers();
        userInfo = null;
    }

    /**
     * Sets the current_position variable as the current location instance from the database
     */
    private void fetchUserPosition(){
        if(!Database.getInstance().getCurrent_user_id().equals(current_user.getID())){
            current_position.setID(Database.getInstance().getCurrent_user_id());
            current_user.setID(Database.getInstance().getCurrent_user_id());
        }
        database.readObj(current_position, Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object loc) {
                current_position = (MLocation) loc;
                notifyLocationObservers(Database.Tables.LOCATIONS.toString());

                if (incognitoMode == current_position.getVisible())
                    updateLocationInDB();
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("FetchMLocFromFirebase", error.getMessage());
            }
        });
    }

    /**
     * Writes the UserInfo data to an external file
     */
    public void saveState(){
        ObjectOutput out;
        try {
            File outFile = new File(Environment.getExternalStorageDirectory(), SAVE_PATH);
            out = new ObjectOutputStream(new FileOutputStream(outFile));
            out.writeObject(this);
            out.close();
        } catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Fetches the saved UserInfo data from an external file
     * @return savedUserInfo: The fetched instance of the UserInfo
     */
    private static UserInfo loadState(){
        ObjectInput in;
        UserInfo savedUserInfo=null;
        try {
            File inFile = new File(Environment.getExternalStorageDirectory(), SAVE_PATH);
            in = new ObjectInputStream(new FileInputStream(inFile));
            savedUserInfo = (UserInfo) in.readObject();
            in.close();
        } catch (Exception e) {e.printStackTrace();}
        return savedUserInfo;
    }

    /**
     * Deletes the external file that UserInfo data is saved in
     */
    public static void deleteDataStorage(){
        try {
            File inFile = new File(Environment.getExternalStorageDirectory(), SAVE_PATH);
            inFile.delete();
        } catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Calls the method to write the current_user object to the USERS table of the database
     */
    public void updateUserInDB(){
        Database.getInstance().writeInstanceObj(current_user, Database.Tables.USERS);
    }

    /**
     * Calls the method to write the current_position object to the LOCATIONS table of the database
     */
    public void updateLocationInDB(){
        Database.getInstance().writeInstanceObj(current_position, Database.Tables.LOCATIONS);
    }
}
