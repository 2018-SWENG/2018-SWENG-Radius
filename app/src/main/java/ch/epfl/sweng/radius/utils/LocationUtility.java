package ch.epfl.sweng.radius.utils;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.DatabaseObject;
import ch.epfl.sweng.radius.database.Location;
import ch.epfl.sweng.radius.database.User;

public class LocationUtility {

    private Location myPos;
    // TODO Fix heritage
    private ArrayList<Location> otherPos;
    private ArrayList<String>   otherID;
    private final Database database = Database.getInstance();


    public LocationUtility(Location myPos){
        this.myPos = myPos;
        this.otherPos = new ArrayList<>();
    }

    public void fetchUsersInRadius(final int radius){

        if(!otherPos.isEmpty())
            otherPos.clear();

        database.readAllTableOnce(Database.Tables.USERS, new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        database.readListObjOnce(((User)value).getFriends(),
                                Database.Tables.USERS, new CallBackDatabase() {
                                    @Override
                                    public void onFinish(Object value) {
                                        for(User friend : (ArrayList<User>) value){
                                            if(isInRadius(friend.getLocation(), radius)) {
                                                otherPos.add(friend.getLocation());
                                                otherID.add(friend.getID());
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

        return computeDistance(loc) <= radius;
    }

    public void updatePos(Location newPos){

        this.myPos = newPos;
    }

    public ArrayList<Location> getOtherPos() {
        return otherPos;
    }

    public double computeDistance(Location loc){

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
