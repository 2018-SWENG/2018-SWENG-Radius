package ch.epfl.sweng.radius.friends;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.epfl.sweng.radius.database.DBUserObserver;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;
import ch.epfl.sweng.radius.utils.customLists.customUsers.CustomUserTab;

public class RequestsTab extends CustomUserTab implements DBUserObserver{
    public RequestsTab() {
        super();
        OthersInfo.getInstance().addUserObserver(this);
    }

    @Override
    public List<MLocation> getList(){
        return new ArrayList(OthersInfo.getInstance().getRequestList());
    }

    @Override
    public void onUserChange(String id) {
        if(this.adapter != null && !Database.DEBUG_MODE)
            super.setUpAdapter();
    }
}
