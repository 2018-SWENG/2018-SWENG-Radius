package ch.epfl.sweng.radius.home;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.friends.FriendsTab;
import ch.epfl.sweng.radius.utils.CustomLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.CustomLists.CustomListItem;
import ch.epfl.sweng.radius.utils.CustomLists.CustomTab;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PeopleTab extends CustomTab {


    public PeopleTab() {

    }
    protected  void setUpAdapterWithUser(User current_user){
        final String userId = current_user.getID();
        database.readListObjOnce(Arrays.asList(userId, "testUser1", "testUser2", "testUser3", "testUser4"),
                Database.Tables.USERS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        PeopleTab.super.setUpAdapterWithList((List<User>)value);
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase", error.getMessage());
                    }

                });

    }





}
