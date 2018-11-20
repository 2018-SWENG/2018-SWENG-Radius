package ch.epfl.sweng.radius.database;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import ch.epfl.sweng.radius.utils.MapUtility;

public class GroupLocationFetcher implements CallBackDatabase {

    private final Database database = Database.getInstance();
    private ArrayList<MLocation> groupLocations;
    private MLocation currentUserLoc;

    public GroupLocationFetcher() {
        groupLocations = new ArrayList<>();
        currentUserLoc = new MLocation(database.getCurrent_user_id());

    }

    public GroupLocationFetcher(double radius) {
        groupLocations = new ArrayList<>();
        currentUserLoc = new MLocation(database.getCurrent_user_id());

    }

    public void setCurrentUserLoc() {

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
   //     System.out.println(currentUserLoc.getLatitude() + " " + currentUserLoc.getLongitude());
        for(MLocation location : (ArrayList<MLocation>) value) {
            System.out.println("location.getID()" + location.getID());
            MapUtility mapUtility = new MapUtility(location.getRadius());
            mapUtility.setMyPos(location);
            if(mapUtility.contains(currentUserLoc.getLatitude(), currentUserLoc.getLongitude())) {
                recordLocationIfGroup(location);
            }
        }
    }

    @Override
    public void onError(DatabaseError error) {
        Log.e("Firebase", error.getMessage());
    }

    public ArrayList<MLocation> getGroupLocations() {
        return groupLocations;
    }

    private void recordLocationIfGroup(final MLocation location) {
        final Database database = Database.getInstance();
        database.readObjOnce(new MLocation(location.getID()),
                Database.Tables.LOCATIONS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        if (((MLocation) value).isGroupLocation()) {
                            groupLocations.add((MLocation) value);
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
