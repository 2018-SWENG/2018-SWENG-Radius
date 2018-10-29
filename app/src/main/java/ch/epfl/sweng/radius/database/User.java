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
    public String urlProfilePhoto;
    private int radius; // meters
    private String status;
    private FriendsHandler friendsHandler;
    private Map<String, String> chatList; // Map is uID --> convID
    private String spokenLanguages;
    private LatLng location;
    private boolean isHidden;

    public User(String userID) {
        this.userID = userID;
        this.nickname = "New User " + userID;
        this.urlProfilePhoto = "";
        this.radius = 500;
        this.status = "Hi, I'm new to radius !";
        this.spokenLanguages = "";
        this.chatList = new HashMap<>();
        this.isHidden = false;
    }

    // Debugging purpose only
    public User(){
        this.userID = Long.toString(idGenerator++);
        this.nickname = "New User " + this.userID;
        this.urlProfilePhoto = "";
        this.radius = 500;
        this.status = "Hi, I'm new to radius !";
        this.spokenLanguages = "";
        this.isHidden = false;
    }

    // Getter
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

    public FriendsHandler getFriendsHandler() {
        return friendsHandler;
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

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void toggleHidden(){ this.isHidden = !isHidden;  }
    
    public Map<String, String> getChatList() {
        return chatList;
    }

    public String getConvFromUser(String userID) {
        return chatList.get(userID);
    }

    public String getSpokenLanguages() {
        return this.spokenLanguages;
    }

    public void setSpokenLanguages(String spokenLanguages) { if (spokenLanguages != null) this.spokenLanguages = spokenLanguages; }

    public void addChat(String uID, String chatID) {
        this.chatList.put(uID, chatID);
    }

    @Override
    public String getID() {
        return userID;
    }

    public boolean getisHidden() {
        return isHidden;
    }

}
