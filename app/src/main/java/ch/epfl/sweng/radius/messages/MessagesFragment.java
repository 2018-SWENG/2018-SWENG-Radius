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

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.DBUserObserver;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;
import ch.epfl.sweng.radius.utils.customLists.customUsers.CustomUserListAdapter;


public class MessagesFragment extends Fragment implements DBUserObserver {
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

        ArrayList<CustomListItem> items = new ArrayList<>();
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
            final ArrayList <CustomListItem> conversations = new ArrayList<>();

            for (User user:users) {
                if(!user.getID().equals(current_user.getID())) {
                    conversations.add(new CustomListItem(user.getID(),
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
        current_user = UserInfo.getInstance().getCurrentUser();
        List<String> usersConv = new ArrayList<>(current_user.getChatList().keySet());

        database.readListObjOnce(usersConv, Database.Tables.USERS, readListConv);
    }

    @Override
    public void onUserChange(String id) {

    }
}