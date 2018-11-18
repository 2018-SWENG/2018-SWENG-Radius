package ch.epfl.sweng.radius.utils.CustomLists.customGroups;

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


public abstract class CustomGroupTab extends Fragment {
    protected final Database database = Database.getInstance();
    protected CustomGroupListAdapter adapter;
    protected User myUser;

   /* private CallBackDatabase adapterCallback = new CallBackDatabase() {
        @Override
        public void onFinish(Object value) {
            ArrayList<CustomGroupListItem> usersItems = new ArrayList<>();
            String convId;
            String userId = database.getCurrent_user_id();

            adapter.setItems(usersItems); adapter.notifyDataSetChanged();
            database.writeInstanceObj(myUser, Database.Tables.USERS);
        }
        @Override
        public void onError(DatabaseError error) {
            Log.e("Firebase", error.getMessage());
        }
    };
*/
    public CustomGroupTab() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.friends_tab, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.friendsList);

        ArrayList<CustomGroupListItem> items = new ArrayList<>();

        //TODO REMOVE THIS LINE : test purposes
        items.add(new CustomGroupListItem("testGroupId","group_EPFL","convIdGroup"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter = new CustomGroupListAdapter(items, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Load the friends from the DB
        setUpAdapter();

        // Inflate the layout for this fragment
        return view;
    }

    private void setUpAdapter(){
        database.readObjOnce(new User(database.getCurrent_user_id()),
                Database.Tables.USERS, new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        setUpAdapterWithList(getIds((User)value));
                    }
                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase Error", error.getMessage());
                    }
                });
    }

    protected void setUpAdapterWithList(List<String> listIds){
        myUser = new User(database.getCurrent_user_id());
        database.readObjOnce(myUser, Database.Tables.USERS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                myUser = (User) value;
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase Error", error.getMessage());
            }
        });

        //database.readListObjOnce(listIds,Database.Tables.USERS, adapterCallback);
    }

    protected abstract List<String> getIds(User current_user);
}
