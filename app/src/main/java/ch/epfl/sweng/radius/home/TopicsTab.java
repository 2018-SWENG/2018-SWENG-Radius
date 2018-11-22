package ch.epfl.sweng.radius.home;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.DBObserver;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.customLists.customUsers.CustomUserTab;


public class TopicsTab extends CustomUserTab implements DBObserver {

    public TopicsTab() {
        OthersInfo.getInstance().addObserver(this);


    }

    protected  List<String> getIds(User current_user){
        return new ArrayList<>();
    }

    @Override
    public void onDataChange(String id) {

    }
}