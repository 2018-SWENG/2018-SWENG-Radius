package ch.epfl.sweng.radius.home;

import ch.epfl.sweng.radius.database.DBLocationObserver;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.utils.customLists.customTopics.CustomTopicTab;

public class TopicsTab extends CustomTopicTab implements DBLocationObserver {

    public TopicsTab() {
        OthersInfo.getInstance().addLocationObserver(this);
    }

    @Override
    public void onLocationChange(String id) {
        if(this.adapter != null && !Database.DEBUG_MODE)
            super.setUpAdapter();
    }

}