package ch.epfl.sweng.radius.home;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.CustomLists.CustomTab;

public class GroupTab extends CustomTab {

    public GroupTab(){}

    @Override
    protected List<String> getUsersIds(User current_user) {
        // 1 get the groups from the DB

        // 2 keep only the group which we are in the radius

        // 3 show the lists of group in the group tabs

        // 4 clickListener to go to the groupConv
        return new ArrayList<>();
    }
}
