package ch.epfl.sweng.radius.home;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.CustomLists.customUsers.CustomUserTab;


public class TopicsTab extends CustomUserTab {

    public TopicsTab() {

    }

    protected  List<String> getIds(User current_user){
        return new ArrayList<>();
    }
}