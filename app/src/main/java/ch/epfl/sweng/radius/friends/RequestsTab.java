package ch.epfl.sweng.radius.friends;

import java.util.List;

import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.customLists.customUsers.CustomUserTab;

public class RequestsTab extends CustomUserTab {
    public RequestsTab() {
        super();
    }

    protected List<String> getIds(User current_user) {
        return UserInfo.getInstance().getCurrentUser().getFriendsInvitations();
    }
}
