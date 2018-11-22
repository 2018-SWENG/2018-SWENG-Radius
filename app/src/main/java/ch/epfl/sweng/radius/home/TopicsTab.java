package ch.epfl.sweng.radius.home;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.DBLocationObserver;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.customLists.customUsers.CustomUserTab;


public class TopicsTab extends CustomUserTab implements DBLocationObserver {

    public TopicsTab() {
        OthersInfo.getInstance().addLocationObserver(this);


    }

    protected  List<String> getIds(User current_user){
        return new ArrayList<>(OthersInfo.getInstance().getTopicsPos().keySet());
    }

    @Override
    public void onLocationChange(String id) {

    }
}