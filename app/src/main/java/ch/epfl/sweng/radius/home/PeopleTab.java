package ch.epfl.sweng.radius.home;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.customLists.CustomTab;


public class PeopleTab extends CustomTab {
    public PeopleTab() {

    }
    protected  List<String> getUsersIds(User current_user){
        final String userId = current_user.getID();
        return Arrays.asList(userId, "testUser1", "testUser2", "testUser3", "testUser4");
    }
}
