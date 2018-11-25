package ch.epfl.sweng.radius.utils.customLists.customTopics;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;
import ch.epfl.sweng.radius.utils.customLists.CustomTab;

public abstract class CustomTopicTab extends CustomTab {

    @Override
    public CustomListAdapter getAdapter(List<CustomListItem> items) {
        return new CustomTopicListAdapter(items, getContext());
    }

    @Override
    public CallBackDatabase getAdapterCallback() {
        return new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                ArrayList<CustomListItem> topicItems = new ArrayList<>();
                String topicId;
                String convId;
                for (ChatLogs topics : (List<ChatLogs>) value) {
                    topicId = topics.getID();
                    convId = topics.getChatLogsId();
                    topicItems.add(new CustomListItem(topicId, convId, topicId));
                }
                adapter.setItems(topicItems);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", error.getMessage());
            }
        };
    }

    @Override
    protected void setUpAdapterWithList(List<String> listIds) {
        database.readListObjOnce(listIds, Database.Tables.CHATLOGS, getAdapterCallback());
    }

    protected abstract List<String> getIds(User current_user);

}
