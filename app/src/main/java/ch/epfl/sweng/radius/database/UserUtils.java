package ch.epfl.sweng.radius.database;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UserUtils extends DBObservable{
    private final Timer timer = new Timer(true);

    private static UserUtils userUtils = null;
    private final int REFRESH_PERIOD = 10; // in seconds
    private static final Database database = Database.getInstance();

    private static final HashMap<String, MLocation> users = new HashMap<>();

    public static UserUtils getInstance(){
        if (userUtils == null)
            userUtils = new UserUtils();
        return userUtils;
    }

    private UserUtils(){ }

    public HashMap<String, MLocation> getSpecificsUsers(List<String> membersId){
        fetchSpeficitsOtherUsers(membersId);
        return users;
    }

    public HashMap<String, MLocation> getUsers(){
        return users;
    }


    public void fetchSpeficitsOtherUsers(List<String> membersId){
        database.readListObjOnce(membersId,Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                users.clear();
                String currentUserId = UserInfo.getInstance().getCurrentUser().getID();
                for (MLocation user : (ArrayList<MLocation>) value) {
                    if(!currentUserId.equals(user.getID())) {
                        users.put(user.getID(), user);
                    }
                }
                notifyLocationObservers(Database.Tables.LOCATIONS.toString());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("FetchUserRadius", error.getMessage());
            }
        });
    }



}
