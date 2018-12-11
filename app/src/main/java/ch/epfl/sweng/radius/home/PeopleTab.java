package ch.epfl.sweng.radius.home;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.DBLocationObserver;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.utils.customLists.customUsers.CustomUserTab;

// TODO : On activity end, clear myUser empty Chaltogs (no message) and repush do
    // TODO     the same for userIDs

public class PeopleTab extends CustomUserTab implements DBLocationObserver {
    List<String> userIDs = new ArrayList<>();

    public PeopleTab() {
        OthersInfo.getInstance().addLocationObserver(this);

    }

    @Override
    public List<MLocation> getList(){
        return new ArrayList(OthersInfo.getInstance().getUsersInRadius().values());
    }
}
