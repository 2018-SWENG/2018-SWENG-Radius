package ch.epfl.sweng.radius.home;

import android.location.Location;
import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.GroupLocationFetcher;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.customLists.customGroups.CustomGroupTab;

public class GroupTab extends CustomGroupTab {

    private MLocation myLocation;
    private double myRadius = -1;
    private String radiusListener;
    List<String> userIDs = new ArrayList<>();

    private final CallBackDatabase radiusCallback = new CallBackDatabase() {
        @Override
        public void onFinish(Object value) {
            myRadius = ((User) value).getRadius();
        }

        @Override
        public void onError(DatabaseError error) {

        }
    };

    private final CallBackDatabase locationCallback = new CallBackDatabase() {
        @Override
        public void onFinish(Object value) {
            myLocation = (MLocation) value;

        }

        @Override
        public void onError(DatabaseError error) {
            Log.e("GroupTab", "Database read error on my Location");
        }
    };

    private CallBackDatabase locationsCallback = new CallBackDatabase() {
        @Override
        public void onFinish(Object value) {
            ArrayList<MLocation> locations = (ArrayList<MLocation>) value;
            for(MLocation loc : locations){
                // TODO Fix for non-user locations by checking TBD location type
                if(isInRadius(loc)){
                    userIDs.add(loc.getID());
                }

            }
        }

        @Override
        public void onError(DatabaseError error) {
            Log.e("GroupTab", "Database read error all locations");
        }
    };


    private boolean isInRadius(MLocation loc) {

        return findDistance(loc.getLatitude(), loc.getLongitude()) < myRadius*1000;
    }

    public double findDistance(double p2latitude, double p2longtitude) {
        float[] distance = new float[3];
        Location.distanceBetween( myLocation.getLatitude(), myLocation.getLongitude(),
                p2latitude, p2longtitude, distance);
       /*
        Log.e("Map","Distance is :" + Double.toString(distance[0])
                + "currCoordinates.latitude" + myLocation.getLatitude()
                + "currCoordinates.longitude" + myLocation.getLongitude());
                */
        return distance[0];
    }

    public GroupTab(){}

    private List<MLocation> getMLocationGroup(final List<MLocation> mLocationList) {
        List<MLocation> result = new ArrayList<>();
        for (MLocation mLocation : mLocationList) {
            if (mLocation.isGroupLocation()) { //
                result.add(mLocation);
            }
        }
        return result;
    }


    private List<MLocation> getMLocationClose(final List<MLocation> mLocationList,MLocation userLocation ) {
        List<MLocation> result = new ArrayList<>();
        for (MLocation mLocation : mLocationList) {
            if (isInRadius(mLocation)){
                result.add(mLocation);
            }
        }
        return result;
    }

    @Override
    protected List<String> getIds(User current_user) {
        final String userId = current_user.getID();
        final Database database = Database.getInstance();
        radiusListener = userId + "_radiusListener";
        //  Get user Radius value and set listener for updates
        //  If it was already fetched, no need to read again, there is a listener
        if(myRadius < 0){
            database.readObj(current_user, Database.Tables.USERS,
                    radiusCallback, radiusListener);

            // 1 get the mLocation from DB -

            ArrayList<MLocation> mLocationList = new ArrayList<>();
            //mLocationList.add();
            //mLocationList.add();

            // 2 find which ones are groups -

            List<MLocation> mLocationGroupList = getMLocationGroup(mLocationList);

            // 3 keep only the group which we are in the radius -

            List<MLocation> mLocationGroupCloseList = getMLocationClose(mLocationGroupList,new MLocation()); //user.getLocation());
            GroupLocationFetcher groupLocationFetcher = new GroupLocationFetcher();
            database.readAllTableOnce(Database.Tables.LOCATIONS, groupLocationFetcher);

            System.out.println(groupLocationFetcher.getGroupLocations().size());
        }

        ArrayList<String> test = new ArrayList<>();
        test.add("1");
        return test;
    }
}
