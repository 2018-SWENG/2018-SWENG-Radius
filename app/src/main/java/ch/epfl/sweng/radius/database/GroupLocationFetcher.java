package ch.epfl.sweng.radius.database;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;

import ch.epfl.sweng.radius.utils.MapUtility;

public class GroupLocationFetcher implements CallBackDatabase {

    private HashMap<String, MLocation> groupLocations;
    private MapUtility mapUtility;

    public GroupLocationFetcher(double radius) {
        groupLocations = new HashMap<>();
        mapUtility = new MapUtility(radius);

    }
    @Override
    public void onFinish(Object value) {
        for(MLocation location : (ArrayList<MLocation>) value) {
            if(mapUtility.contains(location.getLatitude(), location.getLongitude())) {
                recordLocationIfGroup(location);
            }
        }
    }

    @Override
    public void onError(DatabaseError error) {
        Log.e("Firebase", error.getMessage());
    }

    public HashMap<String, MLocation> getGroupLocations() {
        return groupLocations;
    }

    private void recordLocationIfGroup(final MLocation location) {
        final Database database = Database.getInstance();
        database.readObjOnce(new MLocation(location.getID()),
                Database.Tables.LOCATIONS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        if (((MLocation) value).getIsGroupLocation() == 1) {
                            groupLocations.put(((MLocation) value).getID(), (MLocation) value);
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase Error", error.getMessage());
                    }
                });
    }

}
