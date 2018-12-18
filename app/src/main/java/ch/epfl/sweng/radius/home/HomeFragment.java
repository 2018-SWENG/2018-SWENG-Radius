package ch.epfl.sweng.radius.home;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.DBLocationObserver;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.MapUtility;
import ch.epfl.sweng.radius.utils.NotificationUtility;
import ch.epfl.sweng.radius.utils.TabAdapter;

public class HomeFragment extends Fragment implements OnMapReadyCallback, DBLocationObserver {

    //constants
    private static final String TAG = "HomeFragment";
    private static float ZOOM = 13f/2;

    //properties
    private static GoogleMap mobileMap; //make sure the fragment doesn't crash if the map is null
    private static MapView mapView;
    private static CircleOptions radiusOptions;
    private static double radius;
    private static LatLng coord;
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView zoomInButton;

    //testing
    public static MapUtility mapListener = MapUtility.getMapInstance();
    private static Map<String, String> friendsID;
    private static ArrayList<MLocation> usersLoc;
    private static List<MarkerOptions> mapMarkers = new ArrayList<>();
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        radius = UserInfo.getInstance().getCurrentPosition().getRadius(); // converting to meters.
        coord = new LatLng(UserInfo.getInstance().getCurrentPosition().getLatitude(),
                UserInfo.getInstance().getCurrentPosition().getLongitude());
        return fragment;
    }*/

    // For debug purpose only
    public static HomeFragment newInstance(MapUtility mapUtility, GoogleMap googleMap, int radiusValue){
        HomeFragment fragment = new HomeFragment();
        radius = radiusValue;
        mobileMap = googleMap;
        usersLoc = new ArrayList<>();
        coord = new LatLng(UserInfo.getInstance().getCurrentPosition().getLatitude(),
                UserInfo.getInstance().getCurrentPosition().getLongitude());
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        UserInfo.getInstance().addLocationObserver(this);
        OthersInfo.getInstance().addLocationObserver(this);
        super.onCreate(savedInstanceState);
        radius = UserInfo.getInstance().getCurrentPosition().getRadius();
        friendsID = new HashMap<>();
        usersLoc = new ArrayList<>();
        coord = new LatLng(UserInfo.getInstance().getCurrentPosition().getLatitude(),
                UserInfo.getInstance().getCurrentPosition().getLongitude());
    }

    @Override
    public View onCreateView(LayoutInflater infltr, ViewGroup container, Bundle savedInstanceState) {
        View view = infltr.inflate(R.layout.fragment_home, container, false);

        // Create the tab layout under the map
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        adapter = new TabAdapter(this.getChildFragmentManager());
        adapter.addFragment(new PeopleTab(), "People");
        adapter.addFragment(new GroupTab(), "Groups");
        adapter.addFragment(new TopicsTab(), "Topics");

        viewPager.setAdapter(adapter); tabLayout.setupWithViewPager(viewPager);
        getReadWritePermission(getContext(), getActivity());

        return view;
    }

    public void getReadWritePermission(Context context, FragmentActivity activity){
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, 123);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mapListener = MapUtility.getMapInstance();

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();mapView.getMapAsync(this);

        zoomInButton = view.findViewById(R.id.zoomButton);
        zoomInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (coord != null) {
                    moveCamera(coord, ZOOM);

                }
            }
        });
    }

    /*private void setUpZoomButton(View view) {
        zoomInButton = view.findViewById(R.id.zoomButton);
        zoomInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (coord != null) {
                    moveCamera(coord, ZOOM);
                }
            }
        });
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "Map is ready", Toast.LENGTH_SHORT).show();
        if(googleMap == null)
            return;

        mobileMap = googleMap; //use map utility here
        mapListener.getLocationPermission(getContext(), getActivity());

        if (mapListener.getPermissionResult()) {
            mapListener.getDeviceLocation(getActivity()); // use map utility here
            UserInfo.getInstance().getCurrentPosition().setLongitude(mapListener.getCurrCoordinates().longitude);
            UserInfo.getInstance().getCurrentPosition().setLatitude(mapListener.getCurrCoordinates().latitude);
            UserInfo.getInstance().updateLocationInDB();
            if (ActivityCompat.checkSelfPermission(getContext(),
                   Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                   && ActivityCompat.checkSelfPermission(getContext(),
                   Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }

            //mobileMap.setMyLocationEnabled(true);
            try
            {
                getActivity().runOnUiThread(new Runnable(){
                    public void run(){
                        initMap();
                    }
                });
            }catch(NullPointerException e){/* Only happens in Unit Test*/}

            }
    }


    public void initMap() {

        if (mapListener.getCurrCoordinates() != null) {

            MLocation curPos = UserInfo.getInstance().getCurrentPosition();
            coord = new LatLng(curPos.getLatitude(), curPos.getLongitude());
            initCircle(coord);moveCamera(coord, ZOOM);

            // Do locations here
            markNearbyUsers();
        }
    }

    private void initCircle(LatLng currentCoordinates) {
        radiusOptions = new CircleOptions().center(currentCoordinates)
                .strokeColor(Color.RED)
                .fillColor(Color.parseColor("#22FF0000"))
                .radius(radius);
        try
        {
            getActivity().runOnUiThread(new Runnable(){
                public void run(){
                    if(mobileMap != null)
                        mobileMap.addCircle(radiusOptions);
                }
        });
        }catch(NullPointerException e){/* Only happens in Unit Test*/}

        if (radiusOptions != null){
            double radius = radiusOptions.getRadius();
            double scale = radius / 300;
            ZOOM =(int) (16 - Math.log(scale) / Math.log(2));
        }
    }

    private void moveCamera(final LatLng latLng, final float zoom) {
        try
        {
            getActivity().runOnUiThread(new Runnable(){
                public void run(){
                    if(mobileMap != null)
                        mobileMap.moveCamera(CameraUpdateFactory.newLatLngZoom( latLng, zoom));
                }
            });
        }catch(NullPointerException e){/* Only happens in Unit Test*/}

    }
    /**
     * Marks the other users that are within the distance specified by the users.
     * */
    public void markNearbyUsers() {

        // Clear Markers
        try
        {
            getActivity().runOnUiThread(new Runnable(){
                public void run(){
                    if(mobileMap != null){
                        mobileMap.clear(); mobileMap.addCircle(radiusOptions);
                    }

                }
            });
        }catch(NullPointerException e){/* Only happens in Unit Test*/}

        usersLoc = new ArrayList<>(OthersInfo.getInstance().getUsersInRadius().values());

        getFriendsID();
        if(usersLoc != null) {
            for (int i = 0; i < usersLoc.size(); i++) {
                markNearbyUser(i, usersLoc.get(i).getMessage(), usersLoc.get(i).getTitle(), usersLoc.get(i).getID());
            }
        }
    }

    public void getFriendsID() {
        friendsID = UserInfo.getInstance().getCurrentUser().getFriends();
    }

    public void markNearbyUser(int indexOfUser, String status, String userName, String locID) {
        if(!usersLoc.get(indexOfUser).getVisible()) return;
        LatLng newPos = new LatLng(usersLoc.get(indexOfUser).getLatitude(),
                                    usersLoc.get(indexOfUser).getLongitude()    );
        float color = friendsID.containsKey(locID) ? BitmapDescriptorFactory.HUE_BLUE :
                                                        BitmapDescriptorFactory.HUE_RED;

        //Change the marker color to green if users speak the same language but they are not friends
        if(color != BitmapDescriptorFactory.HUE_BLUE){
            for(String language : UserInfo.getInstance().getCurrentPosition().getLanguageList()){
                if(usersLoc.get(indexOfUser).getLanguageList().contains(language)){
                    color = BitmapDescriptorFactory.HUE_GREEN;
                    break;
                }
            }
        }

        if(friendsID.containsKey(locID) && OthersInfo.getInstance().getNewUsersPos().containsKey(locID)){
            showNearFriendNotification(locID, userName);
        }

        final MarkerOptions marker = new MarkerOptions().position(newPos)
                .title(userName + ": " + status)
                .icon(BitmapDescriptorFactory.defaultMarker(color));
        mapMarkers.add(marker);
        try
        {
            getActivity().runOnUiThread(new Runnable(){
            public void run(){
                if(mobileMap != null && mobileMap.getProjection() != null)
                    mobileMap.addMarker(marker);
            }
            });
        }catch(NullPointerException e){/* Only happens in Unit Test*/}

    }

    @Override
    public void onLocationChange(String id) {
        radius = UserInfo.getInstance().getCurrentPosition().getRadius();
        coord = new LatLng(UserInfo.getInstance().getCurrentPosition().getLatitude(), UserInfo.getInstance().getCurrentPosition().getLongitude());
        if (getActivity() != null && !Database.DEBUG_MODE) {
            initCircle(coord);
            markNearbyUsers();
        }
    }

    public void showNearFriendNotification(String userID, String userNickname) {
        // Setup Intent to end here in case of click
        Intent notifIntent = new Intent(this.getActivity(), HomeFragment.class);
        PendingIntent pi = PendingIntent.getActivity(this.getActivity(), 0, notifIntent, 0);
        // Build and show notification
        NotificationUtility.getInstance(null, null, null, null).notifyFriendIsNear(userID, userNickname, pi);
    }
}
