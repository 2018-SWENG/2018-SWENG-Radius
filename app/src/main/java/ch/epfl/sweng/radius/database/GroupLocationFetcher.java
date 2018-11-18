package ch.epfl.sweng.radius.database;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;

import ch.epfl.sweng.radius.utils.MapUtility;

public class GroupLocationFetcher implements CallBackDatabase {

    private final Database database = Database.getInstance();
    private HashMap<String, MLocation> groupLocations;
    private MapUtility mapUtility;
    private MLocation currentUserLoc;

    public GroupLocationFetcher() {
        groupLocations = new HashMap<>();
        currentUserLoc = new MLocation(database.getCurrent_user_id());

        database.readObjOnce(currentUserLoc, Database.Tables.USERS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                currentUserLoc = (MLocation) value;
                Log.e("GroupLocationFetcher: ", "currentUser latitude" + currentUserLoc.getLatitude() +
                        "currentUser longtitude" + currentUserLoc.getLongitude());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase Error", error.getMessage());
            }
        });
    }

    @Override
    public void onFinish(Object value) {
        for(MLocation location : (ArrayList<MLocation>) value) {
            mapUtility = new MapUtility(location.getRadius());
            mapUtility.setCurrCoordinates(new LatLng(location.getLatitude(), location.getLongitude()));
            if(mapUtility.contains(currentUserLoc.getLatitude(), currentUserLoc.getLongitude())) { // compare it with the users location.
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
                Database.Tables.USERS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        if (((MLocation) value).getIsGroupLocation() == 1) {
                            groupLocations.put(((MLocation) value).getID(), (MLocation) value);
                            Log.e("value.getID()", ((MLocation) value).getID());
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase Error", error.getMessage());
                    }
                });
    }

}
