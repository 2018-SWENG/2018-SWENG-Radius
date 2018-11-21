package ch.epfl.sweng.radius.database;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.utils.MapUtility;

public class GroupLocationFetcher implements CallBackDatabase {

    private final Database database = Database.getInstance();
    private List<String> groupLocationsIds;
    private MLocation currentUserLoc;

    public GroupLocationFetcher() {
        groupLocationsIds = new ArrayList<>();
        currentUserLoc = new MLocation(database.getCurrent_user_id());



        database.readObjOnce(currentUserLoc, Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                currentUserLoc = (MLocation) value;
                Log.e("GroupLocationFetcher: ", "currentUser latitude" + currentUserLoc.getLatitude() +
                        "currentUser longitude" + currentUserLoc.getLongitude());
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
            MapUtility mapUtility = new MapUtility(location.getRadius());
            mapUtility.setMyPos(location);
            if(mapUtility.contains(currentUserLoc.getLatitude(), currentUserLoc.getLongitude()) && location.getIsGroupLocation() == 1) {
                groupLocationsIds.add(location.getID());
                Log.e("value.getID()", location.getID());
                //recordLocationIfGroup(location);
            }
        }
    }

    @Override
    public void onError(DatabaseError error) {
        Log.e("Firebase", error.getMessage());
    }

    public List<String> getGroupLocationsIds() {
        return groupLocationsIds;
    }
}
