package ch.epfl.sweng.radius.utils.customLists.customUsers;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.DBUserObserver;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;
import ch.epfl.sweng.radius.utils.customLists.CustomTab;


public abstract class CustomUserTab extends CustomTab implements DBUserObserver {

    public CustomListAdapter getAdapter(List<CustomListItem> items) {
        return new CustomUserListAdapter(items, getContext());
    }

    private ArrayList<CustomListItem> getItems (List<User> values, String userId){

        ArrayList<CustomListItem> ret = new ArrayList<>();
        for (User user :  values) {
            Log.e("Refactor CustomUserTab", "Current feëtched userID is " + user.getID());
            String convId = user.getConvFromUser(userId);
            MLocation userLoc = OthersInfo.getInstance().getUsersInRadius().containsKey(userId) ?
                    OthersInfo.getInstance().getUsersInRadius().get(userId) :
                    OthersInfo.getInstance().getConvUsers().get(userId);

            if(userLoc == null){
                Log.e("CustomUserTab", "User " + userId + " not found!");
                continue;
            }
            if (!user.getID().equals(userId)) {
                ret.add(new CustomListItem(user.getID(), convId, userLoc.getTitle()));
            }
        }

        return ret;
    }

    public CallBackDatabase getAdapterCallback() {
        return new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                ArrayList<CustomListItem> usersItems = new ArrayList<>();
               // adapter = getAdapter(usersItems);
                String convId;
                String userId = UserInfo.getInstance().getCurrentUser().getID();

                usersItems = getItems((List<User>) value, userId);
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
        UserInfo.getInstance().addUserObserver(this);
    }


    @Override
    protected void setUpAdapterWithList(List<String> listIds){
        Log.e("MessageList", "Size of User listIds is :" + Integer.toString(listIds.size()));

        database.readListObjOnce(listIds,
                Database.Tables.USERS, getAdapterCallback());
    }

    protected abstract List<String> getIds(User current_user);

    @Override
    public void onUserChange(String id) {
        if (id.equals(Database.Tables.USERS)){
            super.setUpAdapter();
        }
    }
}
