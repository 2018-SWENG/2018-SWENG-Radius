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
        List<MLocation> visibleUsers = new ArrayList<>();
        List<MLocation> usersInRadius = new ArrayList(OthersInfo.getInstance().getUsersInRadius().values());
        for(int i = 0; i < usersInRadius.size(); i++){
            MLocation loc = usersInRadius.get(i);
            if(loc.isVisible())
                visibleUsers.add(loc);
        }

        return visibleUsers;
    }
}
