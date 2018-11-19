package ch.epfl.sweng.radius.database;

import com.google.android.gms.maps.model.LatLng;

public class MLocation implements DatabaseObject {

    private static int locIDCounter = 0;

    private String userID;
    private String title;
    private String message;
    private double longitude;
    private double latitude;

    private int isGroupLocation; // if 1, it means that the location belongs to a group not a user

    public MLocation(String userID){
        this.userID = userID;
    }

    public MLocation(){
        this.userID = "NewLoc" + Integer.toString(locIDCounter++);
        this.latitude = 46.5360698;
        this.longitude = 6.5681216000000004;
        this.title = "New MLocation";
        this.message = "Here I am";
        this.isGroupLocation = 0;
    }

    public MLocation(String userID, double longitude, double latitude){
        this.userID = userID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = "New MLocation";
        this.message = "Here I am";
        this.isGroupLocation = 0;
    }

    public MLocation(String userID, LatLng pos){
        this.userID = userID;
        this.latitude = pos.latitude;
        this.longitude = pos.longitude;
        this.title = "New MLocation";
        this.message = "Here I am";
        this.isGroupLocation = 0;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getID() {
        return userID;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setID(String userID) {
        this.userID = userID;
    }

    public int getIsGroupLocation() {
        return isGroupLocation;
    }

    public void setIsGroupLocation(int isGroupLocation) {
        this.isGroupLocation = isGroupLocation;
    }

}
