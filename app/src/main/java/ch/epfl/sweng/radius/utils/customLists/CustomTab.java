package ch.epfl.sweng.radius.utils.customLists;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.UserInfo;

public abstract class CustomTab extends Fragment {
    protected final Database database = Database.getInstance();
    protected CustomListAdapter adapter;

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
        adapter = getAdapter(items);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Load the friends from the DB
        setUpAdapter();

        // Inflate the layout for this fragment
        return view;
    }


    private void setUpAdapter() {
        setUpAdapterWithList(getIds(UserInfo.getInstance().getCurrentUser()));

    }

    protected abstract void setUpAdapterWithList(List<String> listIds);

    protected abstract List<String> getIds(User current_user);
}
