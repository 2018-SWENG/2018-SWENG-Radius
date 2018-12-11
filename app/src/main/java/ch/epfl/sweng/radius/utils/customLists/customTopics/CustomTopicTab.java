package ch.epfl.sweng.radius.utils.customLists.customTopics;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;
import ch.epfl.sweng.radius.utils.customLists.CustomTab;

public abstract class CustomTopicTab extends CustomTab {

    @Override
    public CustomListAdapter getAdapter(List<CustomListItem> items) {
        ArrayList<Integer> removableTopicPositions = getRemovableTopicPositions(items);
        return new CustomTopicListAdapter(items, getContext(), removableTopicPositions);
    }

    public CallBackDatabase getAdapterCallback() {
        return new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                ArrayList<CustomListItem> topicItems = new ArrayList<>();

                //Add dummy first item
                topicItems.add(0, new CustomListItem("Dummy","Dummy","Dummy"));

                String topicId;
                String convId;
                for (ChatLogs topics: (List<ChatLogs>) value) {
                    topicId = topics.getID();
                    convId = topics.getChatLogsId();
                    topicItems.add(new CustomListItem(topicId, convId, topicId));
                }
                if(adapter != null){
                    ArrayList<Integer> removableTopicPositions = getRemovableTopicPositions(topicItems);
                    ((CustomTopicListAdapter) adapter).setRemovableTopicPositions(removableTopicPositions);
                    adapter.setItems(topicItems);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", error.getMessage());
            }
        };
    }

    @Override
    protected void setUpAdapterWithList(List<String> listIds) {
        List<String> ids = new ArrayList<>(OthersInfo.getInstance().getTopicsPos().keySet());
        database.readListObjOnce(ids, Database.Tables.CHATLOGS, getAdapterCallback());
    }

    private ArrayList<Integer> getRemovableTopicPositions(List<CustomListItem> items) {
        HashMap<String, MLocation> topics = OthersInfo.getInstance().getTopicsPos();
        ArrayList<Integer> removableTopicPositions = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            MLocation location = topics.get(items.get(i).getItemId());
            if (location != null && location.isRemovableTopic()) {
                removableTopicPositions.add(i);
            }
        }
        return removableTopicPositions;
    }

}
