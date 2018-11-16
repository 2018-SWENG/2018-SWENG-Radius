package ch.epfl.sweng.radius.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import ch.epfl.sweng.radius.friends.FriendsTab;
import ch.epfl.sweng.radius.utils.CustomLists.CustomTab;


public class TopicsTab extends CustomTab {


    public TopicsTab() {

    }

    protected  void setUpAdapterWithUser(User current_user){
        super.setUpAdapterWithList(new ArrayList<User>());

    }


}