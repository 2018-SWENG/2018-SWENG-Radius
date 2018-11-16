package ch.epfl.sweng.radius.friends;

import java.util.List;

import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.customLists.CustomTab;

public class RequestsTab extends CustomTab {
    public RequestsTab() {
        super();
    }

    protected List<String> getUsersIds(User current_user) {
        return current_user.getFriendsInvitations();
    }
}
