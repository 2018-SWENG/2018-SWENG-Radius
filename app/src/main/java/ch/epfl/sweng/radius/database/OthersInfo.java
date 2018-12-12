package ch.epfl.sweng.radius.database;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ch.epfl.sweng.radius.utils.MapUtility;

public class OthersInfo extends DBObservable{
    private final Timer timer = new Timer(true);
    private final int REFRESH_PERIOD = 5; // in seconds

    private static OthersInfo othersInfo = null;
    private static final Database database = Database.getInstance();
    private static final MapUtility mapUtility = MapUtility.getMapInstance();

    private static final HashMap<String, MLocation> usersPos = new HashMap<>();
    private static final HashMap<String, MLocation> newUsersPos = new HashMap<>();
    private static final HashMap<String, MLocation> allUserPos = new HashMap<>();
    private static final HashMap<String, MLocation> convUsers = new HashMap<>();
    private static final HashMap<String, MLocation> groupsPos = new HashMap<>();
    private static final HashMap<String, MLocation> topicsPos = new HashMap<>();
    private static final HashMap<String, User> users = new HashMap<>();
    private static final HashMap<String, MLocation> friendList = new HashMap<>();
    private static final HashMap<String, MLocation> requestList = new HashMap<>();


    public static OthersInfo getInstance(){
        if (othersInfo == null)
            othersInfo = new OthersInfo();
        return othersInfo;
    }

    private OthersInfo(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchUsersInMyRadius();
                fetchUserObjects();
                fetchConvUsers();
                fetchFriends();
                fetchRequest();
            }
        }, 0, REFRESH_PERIOD*1000);    }

    public HashMap<String, MLocation> getUsersInRadius(){
        return usersPos;
    }

    public HashMap<String, MLocation> getAllUserLocations(){
        return allUserPos;
    }

    public HashMap<String, MLocation> getGroupsPos(){
        return groupsPos;
    }

    public HashMap<String, MLocation> getTopicsPos(){
        return topicsPos;
    }

    public HashMap<String, MLocation> getNewUsersPos() { return newUsersPos; }

    public HashMap<String, User> getUsers(){
        return users;
    }

    public HashMap<String, MLocation> getConvUsers() {
        return convUsers;
    }

    public void fetchUsersInMyRadius(){ // Might want to change the name of this method later on.
        database.readAllTableOnce(Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                newUsersPos.clear();
                for (MLocation loc : (ArrayList<MLocation>) value) {
                    if(mapUtility.contains(loc.getLatitude(), loc.getLongitude())) {

                        putInTable(loc);
                    }
                    else
                        removeFromTable(loc);
                    if (loc.getLocationType() == 0) {
                        allUserPos.put(loc.getID(), loc);
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

    private void fetchConvUsers(){
        List<String> ids = new ArrayList<>(UserInfo.getInstance().getCurrentUser().getChatList().keySet());
        Log.e("Refactor OthersInfo", "Size of ids is" + ids.size());
        database.readListObjOnce(ids, Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                for(MLocation loc : (ArrayList<MLocation>) value){
                    Log.e("Refactor OthersInfo", "Current userID is" + loc.getID());
                    if(!usersPos.containsKey(loc.getID()));
                        convUsers.put(loc.getID(), loc);
                }
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }
    public void fetchUserObjects(){
        database.readAllTableOnce(Database.Tables.USERS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                for (User user : (ArrayList<User>) value) {
                    users.put(user.getID(), user);
                }
                Log.e("DEBUGG0", "Fetching the users " + users.size());

                notifyLocationObservers(Database.Tables.LOCATIONS.toString());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("FetchUser", error.getMessage());
            }
        });
    }

    public void fetchFriends(){
        List<String> ids = new ArrayList<>(UserInfo.getInstance().getCurrentUser().getFriends().values());
        database.readListObjOnce(ids, Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                friendList.clear();
                for(MLocation loc : (ArrayList<MLocation>) value){
                    Log.e("Refactor OthersInfo", "Current userID is" + loc.getID());
                    if(!friendList.containsKey(loc.getID()));
                    friendList.put(loc.getID(), loc);
                    notifyUserObservers("");
                }
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("FetchFriend", error.getMessage());
            }
        });
    }


    public void fetchRequest(){
        List<String> ids = new ArrayList<>(UserInfo.getInstance().getCurrentUser().getFriendsInvitations().values());
        database.readListObjOnce(ids, Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                requestList.clear();
                for(MLocation loc : (ArrayList<MLocation>) value){
                    Log.e("Refactor OthersInfo", "Current userID is" + loc.getID());
                    if(!requestList.containsKey(loc.getID()));
                    requestList.put(loc.getID(), loc);
                    notifyUserObservers("");
                }
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("FetchRequests", error.getMessage());
            }
        });
    }

    public void putInTable(MLocation loc){
        switch (loc.getLocationType()){
            case 0:
                if(loc.getID().equals(UserInfo.getInstance().getCurrentPosition().getID()))
                    break;
                // For near friend notifications
                if(!usersPos.containsKey(loc.getID())) newUsersPos.put(loc.getID(), loc);
                usersPos.put(loc.getID(), loc);
                break;
            case 1:
                groupsPos.put(loc.getID(), loc);
                break;
            case 2:
                topicsPos.put(loc.getID(), loc);
                break;
        }
    }

    public void removeFromTable(MLocation loc){
        switch (loc.getLocationType()){
            case 0:
                if(loc.getID().equals(UserInfo.getInstance().getCurrentPosition().getID()))
                    break;
                // For near friend notifications
                usersPos.remove(loc.getID());
                break;
            case 1:
                groupsPos.remove(loc.getID());
                break;
            case 2:
                topicsPos.remove(loc.getID());
                break;
        }
    }

    public Collection<MLocation> getFriendList() {
        return friendList.values();
    }

    public Collection<MLocation> getRequestList() {
        return requestList.values();
    }
}
