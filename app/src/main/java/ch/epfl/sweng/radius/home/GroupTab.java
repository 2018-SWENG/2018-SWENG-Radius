package ch.epfl.sweng.radius.home;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.DBLocationObserver;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.GroupLocationFetcher;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.customLists.customGroups.CustomGroupTab;

public class GroupTab extends CustomGroupTab implements DBLocationObserver {
    public GroupTab(){
        OthersInfo.getInstance().addLocationObserver(this);
    }


    @Override
    public void onLocationChange(String id) {
        if(this.adapter != null && !Database.DEBUG_MODE)
            super.setUpAdapter();
    }
}
