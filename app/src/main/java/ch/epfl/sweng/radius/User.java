package ch.epfl.sweng.radius;

import com.google.android.gms.maps.model.LatLng;

public class User {

    private static int totalNoOfUsers = 0;

    //properties
    private String userName;
    private String status;
    private LatLng location;

    //constructor
    public User(double latitude, double longtitude) {
        totalNoOfUsers++;
        userName = "user" + totalNoOfUsers;
        status = "Status of user " + totalNoOfUsers;
        location = new LatLng(latitude, longtitude);
    }

    //methods
    public String getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
