package ch.epfl.sweng.radius.utils.customLists;

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
import ch.epfl.sweng.radius.utils.customLists.customUsers.CustomUserListAdapter;

public abstract class CustomTab extends Fragment {
    protected final Database database = Database.getInstance();
    private CustomUserListAdapter adapter;

    public CustomTab() {
    }

    public abstract CustomListAdapter getAdapter(List<CustomListItem> items);

    public abstract CallBackDatabase getAdapterCallback();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.friends_tab, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.friendsList);

        ArrayList<CustomListItem> items = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(getAdapter(items));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Load the friends from the DB
        setUpAdapter();

        // Inflate the layout for this fragment
        return view;
    }

    private void setUpAdapter() {
        database.readObjOnce(new User(database.getCurrent_user_id()),
                Database.Tables.USERS, new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        setUpAdapterWithList(getIds((User) value));
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase Error", error.getMessage());
                    }
                });
    }

    protected void setUpAdapterWithList(List<String> listIds){
        database.readListObjOnce(listIds,
                Database.Tables.USERS, getAdapterCallback());
    }

    protected abstract List<String> getIds(User current_user);
}
