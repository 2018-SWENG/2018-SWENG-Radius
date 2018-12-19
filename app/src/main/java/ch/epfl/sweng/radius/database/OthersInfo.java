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
    public Timer timer = new Timer(true);
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

    /**
     * Static singleton method to return the instance of OthersInfo
     * @return othersInfo: the singleton instance of OthersInfo
     */
    public static OthersInfo getInstance() {
        if (othersInfo == null)
            othersInfo = new OthersInfo();
        return othersInfo;
    }

    /**
     * Private constructor for OthersInfo
     */
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
        }, 0, REFRESH_PERIOD*1000);
    }

    /**
     * Set the instance of OthersInfo to null
     */
    public void clearInstance(){
        othersInfo = null;
    }

    /**
     * Getter for usersPos
     * @return: users in radius
     */
    public HashMap<String, MLocation> getUsersInRadius(){
        return usersPos;
    }

    /**
     * Getter for allUserPos
     * @return: all user locations
     */
    public HashMap<String, MLocation> getAllUserLocations(){
        return allUserPos;
    }

    /**
     * Getter for groupsPos
     * @return: groups positions
     */
    public HashMap<String, MLocation> getGroupsPos(){
        return groupsPos;
    }

    /**
     * Getter for topicsPos
     * @return: topics positions
     */
    public HashMap<String, MLocation> getTopicsPos(){
        return topicsPos;
    }

    /**
     * Getter for newUsersPos
     * @return: new users positions
     */
    public HashMap<String, MLocation> getNewUsersPos() {
        return newUsersPos;
    }

    /**
     * Getter for users
     * @return: users
     */
    public HashMap<String, User> getUsers(){
        return users;
    }

    /**
     * Getter for convUsers
     * @return: convUsers
     */
    public HashMap<String, MLocation> getConvUsers() {
        return convUsers;
    }

    /**
     * Fetch the users according to the new location and radius value for the current user
     */
    public void fetchUsersInMyRadius(){
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

    /**
     * Fetch the users that the current user has a conversation with
     */
    private void fetchConvUsers(){
        List<String> ids = new ArrayList<>(UserInfo.getInstance().getCurrentUser().getChatList().keySet());
        database.readListObjOnce(ids, Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                for(MLocation loc : (ArrayList<MLocation>) value){
                    if(!usersPos.containsKey(loc.getID()));
                        convUsers.put(loc.getID(), loc);
                }
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    /**
     * Fetch all user objects from the USERS table of the database
     */
    public void fetchUserObjects(){
        database.readAllTableOnce(Database.Tables.USERS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                for (User user : (ArrayList<User>) value) {
                    users.put(user.getID(), user);
                }
                notifyLocationObservers(Database.Tables.LOCATIONS.toString());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("FetchUser", error.getMessage());
            }
        });
    }

    /**
     * Fetch the friends for the current user
     */
    public void fetchFriends(){
        List<String> ids = new ArrayList<>(UserInfo.getInstance().getCurrentUser().getFriends().values());
        database.readListObjOnce(ids, Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                editList(friendList, value);
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("FetchFriend", error.getMessage());
            }
        });
    }

    /**
     * Fetch the friends requests
     */
    public void fetchRequest(){
        List<String> ids = new ArrayList<>(UserInfo.getInstance().getCurrentUser().getFriendsInvitations().values());
        database.readListObjOnce(ids, Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                editList(requestList, value);
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("FetchRequests", error.getMessage());
            }
        });
    }

    /**
     * Change the locations list
     * @param list: list to change
     * @param value: the MLocation parameter
     */
    private void editList(HashMap<String, MLocation> list, Object value) {
        list.clear();
        for(MLocation loc : (ArrayList<MLocation>) value){
            list.put(loc.getID(), loc);
            notifyUserObservers("");
        }
    }

    /**
     * Save the given MLocation to a table according to type
     * @param loc: MLocation to put
     */
    public void putInTable(MLocation loc){
        switch (loc.getLocationType()){
            case 0:
                if(loc.getID().equals(UserInfo.getInstance().getCurrentPosition().getID()))
                    break;
                // For near friend notifications
                if(friendList.containsKey(loc.getID())) toggledVisibility(loc);
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

    /**
     * Put the new users position to the locations when visibility changes
     * @param loc: MLocation
     */
    private void toggledVisibility(MLocation loc) {
            if(!usersPos.containsKey(loc.getID())
                    || (!friendList.get(loc.getID()).getVisible()
                    && loc.getVisible()))
                newUsersPos.put(loc.getID(), loc);
    }

    /**
     * Remove MLocation from table according to type
     * @param loc: MLocation to put
     */
    public void removeFromTable(MLocation loc){
        if(loc == null) return;
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

    /**
     * Getter for values of friend list
     * @return: Values of friendList
     */
    public Collection<MLocation> getFriendList() {
        return friendList.values();
    }

    /**
     * Getter for values of requests list
     * @return: Values of requestList
     */
    public Collection<MLocation> getRequestList() {
        return requestList.values();
    }
}
