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
    private ProfileInfo profileInfo;
    public String urlProfilePhoto;
    private int radius; // meters

    private FriendsHandler friendsHandler;
    private Map<String, String> chatList; // Map is uID --> convID
    private Map<String, String> reportList;

    private LatLng location;
    private boolean isHidden;

    public User(String userID) {
        this.userID = userID;
        this.profileInfo = new ProfileInfo(userID);
        this.urlProfilePhoto = "";
        this.radius = 50000;
        this.chatList = new HashMap<>();
        this.reportList = new HashMap<>();
        this.isHidden = false;

    }

    // Debugging purpose only
    public User(){
        this.userID = Long.toString(idGenerator++);
        this.profileInfo = new ProfileInfo(userID);
        this.urlProfilePhoto = "";
        this.radius = 50000;
        this.isHidden = false;
    }

    // Getter
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

    public ProfileInfo getProfileInfo() {
        return profileInfo;
    }

    public String getConvFromUser(String userID) {
        return chatList.get(userID);
    }

    public LatLng getLocation() {
        return location;
    }

    public String getID() {
        return userID;
    }

    public boolean getisHidden() {
        return isHidden;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void toggleHidden(){ this.isHidden = !isHidden;  }

    public Map<String, String> getChatList() {
        return chatList;
    }

    public Map<String, String> getReportList() {
        return reportList;
    }

    public void addReport(String reportingUserID, String reportingReason) {
        reportList.put(reportingUserID, reportingReason);
    }

    public String getReportFromUser(String userID) {
        return reportList.get(userID);
    }

    public void addChat(String uID, String chatID) {
        this.chatList.put(uID, chatID);
    }

}
