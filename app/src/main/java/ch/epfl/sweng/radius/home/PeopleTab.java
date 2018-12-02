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
import ch.epfl.sweng.radius.utils.customLists.customUsers.CustomUserTab;

// TODO : On activity end, clear myUser empty Chaltogs (no message) and repush do
    // TODO     the same for userIDs

public class PeopleTab extends CustomUserTab implements DBLocationObserver {
    List<String> userIDs = new ArrayList<>();

    public PeopleTab() {
        OthersInfo.getInstance().addLocationObserver(this);

    }
    protected  List<String> getIds(User current_user){
        final Database database = Database.getInstance();
        //  Get user Radius value and set listener for updates
        //  If it was already fetched, no need to read again, there is a listener
        // Get all other locations in Radius and add corresponding user to List
        // TODO Setup a Listener instead of reading once
        List<String> res = new ArrayList<>(OthersInfo.getInstance().getUsersInRadius().keySet());
        return res;
    }

    private boolean isInRadius(MLocation loc) {

        return MapUtility.isInRadius(loc);
    }

    @Override
    public void onLocationChange(String id) {
        if(this.adapter != null && !Database.DEBUG_MODE)
            super.setUpAdapter();
    }
}
