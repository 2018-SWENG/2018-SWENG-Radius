package ch.epfl.sweng.radius.utils.customLists.customUsers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.DBLocationObserver;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.database.UserUtils;
import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;
import ch.epfl.sweng.radius.utils.customLists.CustomTab;


public abstract class CustomUserTab extends CustomTab implements DBLocationObserver {

    public CustomListAdapter getAdapter(List<CustomListItem> items) {
        return new CustomUserListAdapter(items, getContext());
    }

    public CustomUserTab() {
        UserUtils.getInstance().addLocationObserver(this);
    }

    @Override
    protected void setUpAdapterWithList(List<String> listIds){
        ArrayList<CustomListItem> usersItems = new ArrayList<>();
        List<MLocation> locs = getList();
        for(MLocation loc : locs)
            if(loc.isVisible()){
                usersItems.add(new CustomListItem(loc.getID(), UserInfo.getInstance().getCurrentUser().getConvFromUser(loc.getID())
                        , loc.getTitle()));
                Log.e("Setupadapterwith list: ", "--------------------------------------" + loc.getID() +"----------------------------------------");
            }

        if (adapter != null) {
            adapter.setItems(usersItems);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLocationChange(String id) {
        if(this.adapter != null && !Database.DEBUG_MODE)
            super.setUpAdapter();
    }

    abstract public List<MLocation> getList();
}
