package ch.epfl.sweng.radius.utils.customLists.customGroups;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;
import ch.epfl.sweng.radius.utils.customLists.CustomTab;


public abstract class CustomGroupTab extends CustomTab {

    public CustomListAdapter getAdapter(List<CustomListItem> items){
        return new CustomGroupListAdapter(items, getContext());
    }

    public CallBackDatabase getAdapterCallback(){
        return new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {

                ArrayList<CustomListItem> groupsItems = new ArrayList<>();
                String groupId;
                String convId;
                for (ChatLogs groups : (List<ChatLogs>) value) {
                    groupId = groups.getID();
                    convId = groups.getChatLogsId();
                    // The groupId serves also as a name
                    groupsItems.add(new CustomListItem(groupId, convId, groupId));
                }
                if(adapter != null){
                    adapter.setItems(groupsItems);
                    adapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", error.getMessage());
            }
        };
    }

    public CustomGroupTab() { }

    @Override
    protected void setUpAdapterWithList(List<String> listIds){
        List<String> ids = new ArrayList<>(OthersInfo.getInstance().getGroupsPos().keySet());

        database.readListObjOnce(ids,
                Database.Tables.CHATLOGS, getAdapterCallback());
    }
}
