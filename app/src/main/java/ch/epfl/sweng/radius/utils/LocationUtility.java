package ch.epfl.sweng.radius.utils;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.DatabaseObject;
import ch.epfl.sweng.radius.database.Location;
import ch.epfl.sweng.radius.database.User;

public class LocationUtility {

    private Location myPos;
    // TODO Replace by Map<uID, Location>
    private HashMap<String, Location> otherPos;
    private final Database database = Database.getInstance();

    private final double defaultLat = 46.5360698;
    private final double defaultLng = 6.5681216000000004;

    public LocationUtility(Location myPos){
        this.myPos = myPos;
        this.otherPos = new HashMap<>();

        final Database database = Database.getInstance();

        database.writeInstanceObj(new Location("testUser1", defaultLng, defaultLat),
                Database.Tables.LOCATIONS);
    }

    public void fetchUsersInRadius(final int radius){

        database.readAllTableOnce(Database.Tables.LOCATIONS, new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        database.readAllTableOnce(Database.Tables.LOCATIONS, new CallBackDatabase() {
                                    @Override
                                    public void onFinish(Object value) {
                                        for(Location loc : (ArrayList<Location>) value){
                                            if(isInRadius(loc, radius)) {
                                                if(!otherPos.containsKey(loc.getID())){
                                                    otherPos.put(loc.getID(), loc);
                                                }

                                            }
                                        }
                                    }
                                    @Override
                                    public void onError(DatabaseError error) {
                                        Log.e("Firebase", error.getMessage());
                                    }
                                });
                    }
                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase Error", error.getMessage());
                    }
                });
    }

    public boolean isInRadius(Location loc, int radius){
        return computeDistance(loc)/1000 <= radius;
    }

    public ArrayList<Location> getOtherLocations() {
        return new ArrayList<>(otherPos.values());
    }

    public void updatePos(Location newPos){

        this.myPos = newPos;
    }

    public void setMyPos(Location myPos) {
        this.myPos = myPos;
    }

    public HashMap<String, Location> getOtherPos() {
        return otherPos;
    }

    public double computeDistance(Location loc){

        if(loc == null)
            return Double.MAX_VALUE;

        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(loc.getLatitude() - myPos.getLatitude());
        double lngDiff = Math.toRadians(loc.getLongitude()-myPos.getLongitude());
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(myPos.getLatitude())) *
                        Math.cos(Math.toRadians(loc.getLatitude())) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }
}
