package ch.epfl.sweng.radius.database;

import android.provider.ContactsContract;
import android.util.ArrayMap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is design to store all the element we need about a user in the app
 * We can then store/access the states of each user in the database
 */
public class User implements DatabaseObject {
    private static long idGenerator = 0;// Debugging purpose only

    private final String userID;
    private String nickname;
    private String urlProfilePhoto;
    private int radius; // meters
    private String status;
    private List<Integer> friendsRequests;
    private List<Integer> friendsInvitations;
    private List<Integer> friends;
    private List<Integer> blockedUsers;
    // Map is uID --> convID
    private Map<String, String> chatList;
    private List<String> convList;
    private String spokenLanguages;
    private LatLng location;

    public User(String userID){
        this.userID = userID;
        this.nickname = "New User " + userID;
        this.urlProfilePhoto = "";
        this.radius = 500;
        this.status = "Hi, I'm new to radius !";
        this.friendsRequests = new ArrayList<>();
        this.friendsInvitations = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
        this.spokenLanguages = "";
        this.chatList = new HashMap<>();
        this.convList = new ArrayList<>();
    }

    // Debugging purpose only
    public User(){
        this.userID = Long.toString(idGenerator++);
        this.nickname = "New User " + this.userID;
        this.urlProfilePhoto = "";
        this.radius = 500;
        this.status = "Hi, I'm new to radius !";
        this.friendsRequests = new ArrayList<>();
        this.friendsInvitations = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
        this.chatList = new HashMap<>();
        this.convList = new ArrayList<>();
    }

    // Getter
    public String getUserID() {
        return userID;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUrlProfilePhoto() {
        return urlProfilePhoto;
    }

    public int getRadius() {
        return radius;
    }

    public String getStatus() {
        return status;
    }

    public List<Integer> getFriendsRequests() {
        return friendsRequests;
    }

    public List<Integer> getFriendsInvitations() {
        return friendsInvitations;
    }

    public List<Integer> getFriends() {
        return friends;
    }

    public List<Integer> getBlockedUsers() {
        return blockedUsers;
    }

    public LatLng getLocation() {
        return location;
    }

    public Map<String, String> getChatList(){return chatList; }

    public List<String> getConvList(){ return convList;}

    public String getConvFromUser(String userID){ return chatList.get(userID);}
    // Setter

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUrlProfilePhoto(String urlProfilePhoto) {
        this.urlProfilePhoto = urlProfilePhoto;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setStatus(String status) throws IllegalArgumentException{
        if (status.length() > 50) // TODO : config file with all the constants
            throw new IllegalArgumentException("The status is limited to 50 characters");
        this.status = status;
    }


    public void addFriendRequest(Integer friendID){
        if (friendsInvitations.contains(friendID)){
            friendsInvitations.remove(friendID);
            friends.add(friendID);
        }
        else
            friendsRequests.add(friendID);
    }

    /*public void addFriendInvitation(Integer friendID){
        if (friendsRequests.contains(friendID)) {
            friendsRequests.remove(friendID);
            friends.add(friendID);
        }
        else
            friendsInvitations.add(friendID);
    }*/

    /*public void addBlockedUser (Integer userID){
        blockedUsers.add(userID);
    }*/

    //public void removeBlockedUser(Integer userID){
    //    blockedUsers.remove(userID);
    //}

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setSpokenLanguages(String spokenLanguages) { this.spokenLanguages = spokenLanguages; }

    public String getSpokenLanguages() { return this.spokenLanguages; }

    public void setChatList(Map<String, String> newChatList){ this.chatList = newChatList;}

    public void setConvList(List<String> newConv){ this.convList = newConv;}

    public void addChat(String uID, String chatID){ this.chatList.put(uID, chatID);    }

    public void addConv(String convID){ this.convList.add(convID);}

    @Override
    public String getID() {
        return userID;
    }
}
