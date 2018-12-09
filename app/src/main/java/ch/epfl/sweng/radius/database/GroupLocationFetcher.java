/*package ch.epfl.sweng.radius.database;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.utils.MapUtility;

public class GroupLocationFetcher implements CallBackDatabase {

    private final Database database = Database.getInstance();
    public List<String> groupLocationsIds;
    private MLocation currentUserLoc;

    public GroupLocationFetcher() {
        groupLocationsIds = new ArrayList<>();
        currentUserLoc = UserInfo.getInstance().getCurrentPosition();
    }

    @Override
    public void onFinish(Object value) {
        for(MLocation location : (ArrayList<MLocation>) value) {
            MapUtility mapUtility = MapUtility.getMapInstance();
            mapUtility.setMyPos(location);
            if(MapUtility.findDistance(currentUserLoc, location) < 50000
                    && location.getLocationType() == 1) {
                groupLocationsIds.add(location.getID());
                Log.e("MessageList", "Group ID is " + location.getID());
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
*/