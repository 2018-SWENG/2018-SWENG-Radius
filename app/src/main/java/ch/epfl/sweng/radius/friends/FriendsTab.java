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

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.CustomListAdapter;
import ch.epfl.sweng.radius.utils.CustomListItem;
import ch.epfl.sweng.radius.utils.UserInfos;


public class FriendsTab extends Fragment {


    public FriendsTab() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.friends_tab, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.friendsList);
        //mock data for testing purposes

        final ArrayList<CustomListItem> items = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        final CustomListAdapter adapter = new CustomListAdapter(items, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Load the friends from the DB
        setUpAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }

    private void setUpAdapter(final CustomListAdapter adapter){
        final Database database = Database.getInstance();

        database.readObjOnce(new User(database.getCurrent_user_id()),
                Database.Tables.USERS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                database.readListObjOnce(((User)value).getFriends(),
                        Database.Tables.USERS, new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        ArrayList<CustomListItem> users = new ArrayList<>();
                        String convId;
                        String userId = UserInfos.getUserId();
                        for (User friend: (ArrayList<User>) value) {
                            convId = friend.getConvFromUser(userId);

                            // si la conv n'existe pas, il faut la créer
                            if(convId.isEmpty()){
                                ArrayList<String> ids = new ArrayList();
                                ids.add(userId);
                                ids.add(friend.getID());
                                convId = new ChatLogs(ids).getID();
                            }
                            users.add(new CustomListItem(friend, convId));
                        }
                        adapter.setItems(users); adapter.notifyDataSetChanged();
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
    }
}
