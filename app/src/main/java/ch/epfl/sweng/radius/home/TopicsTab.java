package ch.epfl.sweng.radius.home;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.DBLocationObserver;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.MapUtility;
import ch.epfl.sweng.radius.utils.customLists.customTopics.CustomTopicTab;
import ch.epfl.sweng.radius.utils.customLists.customUsers.CustomUserTab;

public class TopicsTab extends CustomTopicTab implements DBLocationObserver {

    private List<String> topicIDs = new ArrayList<>();

    private CallBackDatabase locationsCallback = new CallBackDatabase() {
        @Override
        public void onFinish(Object value) {
            ArrayList<MLocation> locations = (ArrayList<MLocation>) value;
            for (MLocation location: locations){
                if (checkLocationConditions(location)) {
                    topicIDs.add(location.getID());
                }
            }
        }

        @Override
        public void onError(DatabaseError error) {
            Log.e("TopicsTab", "Database read error all locations");
        }
    };

    private boolean checkLocationConditions(MLocation location) {
        return MapUtility.isInRadius(location) && location.isVisible() &&
                location.getLocationType() == 0;
    }

    public TopicsTab() {
        OthersInfo.getInstance().addLocationObserver(this);
    }

    protected  List<String> getIds(User current_user) {
        Database.getInstance().readAllTableOnce(Database.Tables.LOCATIONS, locationsCallback);
        List<String> result = new ArrayList<>(OthersInfo.getInstance().getTopicsPos().keySet());
        return result;
    }

    @Override
    public void onLocationChange(String id) {
        if(this.adapter != null && !Database.DEBUG_MODE)
            super.setUpAdapter();
    }

}