package ch.epfl.sweng.radius.friends;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.List;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.CustomLists.CustomTab;

public class RequestsTab extends CustomTab {
    public RequestsTab(){ super();}

    protected  void setUpAdapterWithUser(User current_user){
        database.readListObjOnce(current_user.getFriendsInvitations(),
                Database.Tables.USERS, new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        RequestsTab.super.setUpAdapterWithList((List<User>)value);
                    }
                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase", error.getMessage());
                    }
                });
    }}

