package ch.epfl.sweng.radius.database;

import com.google.android.gms.maps.model.LatLng;

public class Location implements DatabaseObject {

    String userID;
    double longitude;
    double latitude;
    // Not actually stored in DB
    // We will use separate tables for each type of location
    // TODO Think about it for real
    int    type; // e.g. {myLocation, friendLocation, Marker, Event, ...}

    public Location(String userID){
        this.userID = userID;
        this.type = 0;
    }

    public Location(String userID, double longitude, double latitude){
        this.userID = userID;
        this.type = 0;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public double computeDistance(double lat, double lng){
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat - latitude);
        double lngDiff = Math.toRadians(lng-longitude);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(lat)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }
}
