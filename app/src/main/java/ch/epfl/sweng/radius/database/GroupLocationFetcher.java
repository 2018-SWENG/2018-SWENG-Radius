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
        ArrayList<DatabaseObject> arrayList = (ArrayList<DatabaseObject>) value;
        for (DatabaseObject DBObj : arrayList) {
            MLocation location = (MLocation) DBObj;
            if (location.isGroupLocation() /* && contains condition using mapUtility */) {
                groupLocations.put(location.getID(), location);
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

}
