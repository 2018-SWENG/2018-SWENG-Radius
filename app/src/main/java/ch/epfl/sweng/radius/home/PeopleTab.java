package ch.epfl.sweng.radius.home;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.messages.MessageListActivity;
import ch.epfl.sweng.radius.utils.CustomListAdapter;
import ch.epfl.sweng.radius.utils.CustomListItem;
import ch.epfl.sweng.radius.utils.MapUtility;
import ch.epfl.sweng.radius.utils.UserInfos;


public class PeopleTab extends Fragment {

    // TODO : On activity end, clear myUser empty Chaltogs (no message) and repush do
    // TODO     the same for userIDs

    public PeopleTab() {
        this.usersInRadius = new ArrayList<>();
    }

    private List<User> usersInRadius;
    private ArrayList<CustomListItem> userItems;
    private int myRadius;
    private MLocation myLocation;
    private String radiusListener;
    private String locationListener;
    private User myUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.people_tab, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.peopleTab);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        final CustomListAdapter adapter = new CustomListAdapter(new ArrayList<CustomListItem>()
                , getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        userItems = new ArrayList<>();
        //mock data for testing purposes
        setUpAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }

    private void setUpAdapter(final CustomListAdapter adapter){
        final Database database = Database.getInstance();
        final String userId = database.getCurrent_user_id();
        userItems.clear();
        radiusListener = userId + "radiuslistener";
        myUser = new User(userId);
        //  Get user Radius value and set listener for updates
        database.readObj(myUser, Database.Tables.USERS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                myRadius = ((User)value).getRadius();
                myUser = (User) value;
                updateList();
            }

            @Override
            public void onError(DatabaseError error) {

            }
        }, radiusListener);

        // Get my Location
        database.readObjOnce(new MLocation(userId), Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                myLocation = (MLocation) value;

            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("PeopleTab", "Database read error on my Location");
            }
        });
        final List<String> userIDs = new ArrayList<>();
        // Get all other locations in Radius and add corresponding user to List
        // TODO Setup a Listener instead of reading once
        database.readAllTableOnce(Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                ArrayList<MLocation> locations = (ArrayList<MLocation>) value;
                for(MLocation loc : locations){
                    // TODO Fix for non-user locations by checking TBD location type
                    if(isInRadius(loc)){
                        userIDs.add(loc.getID());
                    }

                }
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("PeopleTab", "Database read error all locations");
            }
        });

        // Fill local User list
        database.readAllTableOnce( Database.Tables.USERS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                ArrayList<User> res = (ArrayList<User>) value;
               Log.e("PeopleTab", "Fetched ID size :" + userIDs.size());
               for(String s : userIDs)
                   Log.e("PeopleTab", "Fetched ID size :" + s);

                for(User temp : res) {
                    Log.e("PeopleTab", "Fetched users ID :" + temp.getID());

                    if (userIDs.contains(temp.getID())) {
                        Log.e("PeopleTab", "Fetched users size :" + userItems.size());

                        if (temp.getID() == userId){
                            Log.e("PeopleTab", "Its a meee");
                            continue;
                        }


                        String convID = myUser.getConvFromUser(temp.getID());
                        if (convID == null) {
                            ArrayList<String> membersID = new ArrayList<>();
                            membersID.add(myUser.getID());
                            membersID.add(temp.getID());
                            convID = new ChatLogs(membersID).getID();
                            temp.addChat(myUser.getID(), convID);
                            database.writeInstanceObj(temp, Database.Tables.USERS);
                            myUser.addChat(temp.getID(), convID);
                        } else if (!convID.equals(myUser.getConvFromUser(temp.getID())))
                            throw new AssertionError("Cannot have two different " +
                                    "chatID for the same chat");

                        userItems.add(new CustomListItem(temp, convID));
                    }
                }
                Log.e("PeopleTab", "Fetched user size :" + userItems.size());

                // Set adapter
                adapter.setItems(userItems);
                adapter.notifyDataSetChanged();
                // Update local user with modifications
                database.writeInstanceObj(myUser, Database.Tables.USERS);
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("PeopleTab", "Database read error on my UserList");
            }
        });

    }

    private boolean isInRadius(MLocation loc) {

        return findDistance(loc.getLatitude(), loc.getLongitude()) < myRadius*1000;
    }

    public double findDistance(double p2latitude, double p2longtitude) {
        float[] distance = new float[3];
        Location.distanceBetween( myLocation.getLatitude(), myLocation.getLongitude(),
                p2latitude, p2longtitude, distance);
        Log.e("Map","Distance is :" + Double.toString(distance[0])
                + "currCoordinates.latitude" + myLocation.getLatitude()
                + "currCoordinates.longitude" + myLocation.getLongitude());
        return distance[0];
    }

    private void updateList() {
        if(locationListener == null)
            return;

        // TODO Update View
    }
}
