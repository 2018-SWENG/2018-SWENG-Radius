package ch.epfl.sweng.radius.utils.customLists.customUsers;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.DBObserver;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;
import ch.epfl.sweng.radius.utils.customLists.CustomTab;


public abstract class CustomUserTab extends CustomTab implements DBObserver {

    public CustomListAdapter getAdapter(List<CustomListItem> items) {
        return new CustomUserListAdapter(items, getContext());
    }

    public CallBackDatabase getAdapterCallback() {
        return new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                ArrayList<CustomListItem> usersItems = new ArrayList<>();
               // adapter = getAdapter(usersItems);
                String convId;
                String userId = UserInfo.getInstance().getCurrentUser().getID();

                for (User user : (List<User>) value) {
                    convId = user.getConvFromUser(userId);
                    if (!user.getID().equals(userId)) {
                        usersItems.add(new CustomListItem(user.getID(), convId, user.getNickname()));
                    }
                }
                adapter.setItems(usersItems);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase", error.getMessage());
            }
        };

    }


    public CustomUserTab() {
        UserInfo.getInstance().addObserver(this);
    }


    @Override
    protected void setUpAdapterWithList(List<String> listIds){
        Log.e("MessageList", "Size of User listIds is :" + Integer.toString(listIds.size()));

        database.readListObjOnce(listIds,
                Database.Tables.USERS, getAdapterCallback());
    }

    protected abstract List<String> getIds(User current_user);

    @Override
    public void onDataChange(String id) {
        if (id.equals(Database.Tables.USERS)){
            super.setUpAdapter();
        }
    }
}
