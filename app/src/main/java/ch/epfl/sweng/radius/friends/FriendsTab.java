package ch.epfl.sweng.radius.friends;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.epfl.sweng.radius.database.DBUserObserver;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;
import ch.epfl.sweng.radius.utils.customLists.customUsers.CustomUserTab;

public class FriendsTab extends CustomUserTab implements DBUserObserver{
    public FriendsTab(){ super();
        OthersInfo.getInstance().addUserObserver(this);
    }

    @Override
    protected void setUpAdapterWithList(List<String> listIds){
        ArrayList<CustomListItem> usersItems = new ArrayList<>();
        Collection<MLocation> locs = OthersInfo.getInstance().getFriendList();
        Log.e("Test", locs.size() + " size");
        for(MLocation loc : locs)
            usersItems.add(new CustomListItem(loc.getID(), UserInfo.getInstance().getCurrentUser().getConvFromUser(loc.getID())
                        , loc.getTitle()));

        if (adapter != null) {
            adapter.setItems(usersItems);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onUserChange(String id) {
        Log.e("Test", "User change");
        setUpAdapterWithList(new ArrayList());
    }
}

