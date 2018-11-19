package ch.epfl.sweng.radius.home;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.CustomLists.CustomTab;


public class TopicsTab extends CustomTab {

    public TopicsTab() {

    }

    protected  List<String> getUsersIds(User current_user){
        return new ArrayList<>();
    }
}