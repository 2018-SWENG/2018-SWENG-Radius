package ch.epfl.sweng.radius.messages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;
import ch.epfl.sweng.radius.utils.customLists.customUsers.CustomUserListAdapter;
import ch.epfl.sweng.radius.utils.customLists.customUsers.CustomUserListItem;

/*
public class MessagesFragment extends Fragment {
    List<CustomUserListItem> chatList;
    ListView listView;
*/
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;


public class MessagesFragment extends Fragment {
    private final Database database = Database.getInstance();
    private CustomUserListAdapter adapter;
    private User current_user;

    public MessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.messagesList);

        ArrayList<CustomUserListItem> items = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new CustomUserListAdapter(items, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Load the messages List from the DB
        setUpAdapter();

        // Inflate the layout for this fragment
        return view;
    }

    private final CallBackDatabase readListConv = new CallBackDatabase() {
        @Override
        public void onFinish(Object value) {
            ArrayList<User> users = (ArrayList<User>)value;
            final ArrayList <CustomUserListItem> conversations = new ArrayList<>();

            for (User user:users) {
                if(!user.getID().equals(database.getCurrent_user_id())) {
                    conversations.add(new CustomUserListItem(user.getID(),
                            current_user.getChatList().get(user.getID()), user.getNickname()));
                }
            }
            adapter.setItems(conversations);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onError(DatabaseError error) {
            Log.e("Firebase Error", error.getMessage());
        }
    };

    private void setUpAdapter(){
        database.readObjOnce(new User(database.getCurrent_user_id()),
                Database.Tables.USERS, new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        current_user = (User) value;
                        List<String> usersConv = new ArrayList<String>();
                        usersConv.addAll(current_user.getChatList().keySet());

                        database.readListObjOnce(usersConv, Database.Tables.USERS, readListConv);
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase Error", error.getMessage());
                    }
        });
    }
}