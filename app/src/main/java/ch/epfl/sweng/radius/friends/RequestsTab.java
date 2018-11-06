package ch.epfl.sweng.radius.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.FirebaseUtility;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.CustomListAdapter;
import ch.epfl.sweng.radius.utils.CustomListItem;


public class RequestsTab extends Fragment {


    public RequestsTab() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.requests_tab, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.requestsTab);
        //mock data for testing purposes

        final ArrayList<CustomListItem> items = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        final CustomListAdapter adapter = new CustomListAdapter(items, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final Database database =  Database.getInstance();
        User currentUser = new User(database.getCurrent_user_id());
        database.readObjOnce(currentUser, Database.Tables.USERS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                User userStoredInTheDB = (User)value;
                database.readListObjOnce(userStoredInTheDB.getFriendsInvitations(),
                        Database.Tables.USERS,
                        new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        ArrayList<CustomListItem> friends = new ArrayList<>();
                        for (User friend: (ArrayList<User>) value) {
                            friends.add(new CustomListItem(friend));
                        }
                        adapter.setItems(friends);
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase", error.getMessage());
                    }
                });
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase Error", error.getMessage());
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
