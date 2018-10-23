package ch.epfl.sweng.radius.database;

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
    private List<String> friendsRequests;
    private List<String> friendsInvitations;
    private List<String> friends;
    private List<String> blockedUsers;
    // Map is uID --> convID
    private Map<String, String> chatList;
    private String spokenLanguages;
    private LatLng location;

    public User(String userID) {
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
    }

    // Debugging purpose only
    public User() {
        this(Long.toString(idGenerator++));
    }

    // Getter
    public String getUserID() {
        return userID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUrlProfilePhoto() {
        return urlProfilePhoto;
    }

    public void setUrlProfilePhoto(String urlProfilePhoto) {
        this.urlProfilePhoto = urlProfilePhoto;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) throws IllegalArgumentException {
        if (status.length() > 50) // TODO : config file with all the constants
            throw new IllegalArgumentException("The status is limited to 50 characters");
        this.status = status;
    }

    public List<String> getFriendsRequests() {
        return friendsRequests;
    }

    public List<String> getFriendsInvitations() {
        return friendsInvitations;
    }

    public List<String> getFriends() {
        return friends;
    }
    // Setter

    public List<String> getBlockedUsers() {
        return blockedUsers;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public Map<String, String> getChatList() {
        return chatList;
    }

    public String getConvFromUser(String userID) {
        return chatList.get(userID);
    }

    public void addFriendRequest(String friendID) {
        if (friendsInvitations.contains(friendID)) {
            friendsInvitations.remove(friendID);
            friends.add(friendID);
        } else
            friendsRequests.add(friendID);
    }

    public String getSpokenLanguages() {
        return this.spokenLanguages;
    }

    public void setSpokenLanguages(String spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public void addChat(String uID, String chatID) {
        this.chatList.put(uID, chatID);
    }

    @Override
    public String getID() {
        return userID;
    }
}
