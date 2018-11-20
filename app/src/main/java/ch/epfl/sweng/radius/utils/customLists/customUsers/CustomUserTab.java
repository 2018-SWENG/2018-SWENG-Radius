package ch.epfl.sweng.radius.utils.customLists.customUsers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;
import ch.epfl.sweng.radius.utils.customLists.CustomTab;


public abstract class CustomUserTab extends CustomTab {

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
                String userId = database.getCurrent_user_id();
                for (User user : (List<User>) value) {
                    convId = user.getConvFromUser(userId);

                    if (!user.getID().equals(database.getCurrent_user_id())) {
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


    public CustomUserTab() { }

    @Override
    protected void setUpAdapterWithList(List<String> listIds){
        database.readListObjOnce(listIds,
                Database.Tables.USERS, getAdapterCallback());
    }

    protected abstract List<String> getIds(User current_user);
}
