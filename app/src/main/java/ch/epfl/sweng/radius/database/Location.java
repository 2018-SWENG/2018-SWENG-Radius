package ch.epfl.sweng.radius.database;

import com.google.android.gms.maps.model.LatLng;

public class Location implements DatabaseObject {

    String userID;
    double longitude;
    double latitude;
    // Not actually stored in DB
    // We will use separate tables for each type of location
    // TODO Think about it for real

    public Location(String userID){
        this.userID = userID;
    }

    public Location(){
        this.userID = "Arth";
        this.latitude = 0;
        this.longitude = 0;
    }

    public Location(String userID, double longitude, double latitude){
        this.userID = userID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(String userID, LatLng pos){
        this.userID = userID;
        this.latitude = pos.latitude;
        this.longitude = pos.longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String getID() {
        return userID;
    }

}
