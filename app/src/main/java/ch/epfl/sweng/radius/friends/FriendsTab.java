package ch.epfl.sweng.radius.friends;

import java.util.List;

import ch.epfl.sweng.radius.database.DBObserver;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.customLists.customUsers.CustomUserTab;

public class FriendsTab extends CustomUserTab implements DBObserver {
    public FriendsTab(){ super();}

    protected List<String> getIds(User current_user){
        return current_user.getFriends();
    }

    @Override
    public void onDataChange(String id) {

    }
}

