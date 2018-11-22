package ch.epfl.sweng.radius.database;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;

import ch.epfl.sweng.radius.utils.MapUtility;

public class UserFetchCallback implements CallBackDatabase {

    private MapUtility mapUtility;
    private int radius;

    public UserFetchCallback(double radius) {
        mapUtility = MapUtility.getMapInstance();
        this.radius = (int) radius;

    }

    @Override
    public void onFinish(Object value) {
        final HashMap<String, MLocation> otherPos = mapUtility.getOtherPos();
        for (MLocation loc : (ArrayList<MLocation>) value) {
            Database.getInstance().readObj(loc, Database.Tables.LOCATIONS, new CallBackDatabase() {
                @Override
                public void onFinish(Object value) {
                    MLocation loc = (MLocation) value;
                    if(otherPos.containsKey(loc.getID()))
                        otherPos.remove(loc.getID());

                    if(mapUtility.contains(loc.getLatitude(), loc.getLongitude())) {
                        Log.e("MapUtility", "Adder user " + loc.getID());
                        otherPos.put(loc.getID(), loc);
                        // Code update markers

                    }

                }

                @Override
                public void onError(DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onError(DatabaseError error) {
        Log.e("Firebase", error.getMessage());
    }



    /*
    private void recordLocationIfVisible(final MLocation location) {
        final Database database = Database.getInstance();
        database.readObjOnce(new User(location.getID()),
                Database.Tables.USERS, new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        if (((User) value).isVisible()) {
                            mapUtility.getOtherPos().put(location.getID(), location);
                        }
                    }
                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase Error", error.getMessage());
                    }
                });
    }
    */

}
