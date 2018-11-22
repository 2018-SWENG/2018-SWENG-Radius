package ch.epfl.sweng.radius.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.DBLocationObserver;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.MapUtility;
import ch.epfl.sweng.radius.utils.TabAdapter;

public class HomeFragment extends Fragment implements OnMapReadyCallback, DBLocationObserver {

    //constants
    private static final String TAG = "HomeFragment";
    private static float ZOOM = 13f/2;
    private static final double DEFAULT_RADIUS = 50000; //In meters

    //properties
    private static GoogleMap mobileMap; //make sure the fragment doesn't crash if the map is null
    private static MapView mapView;
    private static CircleOptions radiusOptions;
    private static double radius;
    private static LatLng coord;
    private TabAdapter adapter;
    private TabLayout tabLayout;

    private ViewPager viewPager;

    //testing
    public static MapUtility mapListener = MapUtility.getMapInstance();
    private static ArrayList<User> users;
    private static List<String> friendsID;
    private static ArrayList<MLocation> usersLoc;
    private static List<MarkerOptions> mapMarkers = new ArrayList<>();
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param radiusValue Parameter 1.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(int radiusValue) {
        HomeFragment fragment = new HomeFragment();
        radius = UserInfo.getInstance().getCurrentPosition().getRadius(); // converting to meters.
        coord = new LatLng(UserInfo.getInstance().getCurrentPosition().getLatitude(),
                UserInfo.getInstance().getCurrentPosition().getLongitude());
        return fragment;
    }

    // For debug purpose only
    public static HomeFragment newInstance(MapUtility mapUtility, GoogleMap googleMap,
                                           int radiusValue) {
        HomeFragment fragment = new HomeFragment();
        radius = radiusValue*1000;
        mobileMap = googleMap;
    //    mapListener = mapUtility;
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
        radius = DEFAULT_RADIUS;
        users = new ArrayList<>();
        friendsID = new ArrayList<>();
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
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       mapListener = MapUtility.getMapInstance();

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");

        if(googleMap == null)
            return;

        mobileMap = googleMap; //use map utility here
        mapListener.getLocationPermission(getContext(), getActivity());

        if (mapListener.getPermissionResult()) {
            mapListener.getDeviceLocation(getActivity()); // use map utility here

            if (ActivityCompat.checkSelfPermission(getContext(),
                   Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                   && ActivityCompat.checkSelfPermission(getContext(),
                   Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }

                mobileMap.setMyLocationEnabled(true);
            getActivity().runOnUiThread(new Runnable(){
                public void run(){
                    initMap();
                }
            });

        }
    }

    private void initMap() {

        if (mapListener.getCurrCoordinates() != null) {

            MLocation curPos = UserInfo.getInstance().getCurrentPosition();
            coord = new LatLng(curPos.getLatitude(), curPos.getLongitude());
            initCircle(coord);
            if (radiusOptions != null){
                double radius = radiusOptions.getRadius();
                double scale = radius / 500;
                ZOOM =(int) (16 - Math.log(scale) / Math.log(2));
            }
            moveCamera(coord, ZOOM);
            // Push current location to DB
            // Write the location of the current user to the database
            Database.getInstance().readObjOnce(new MLocation("EPFL"), Database.Tables.LOCATIONS, new CallBackDatabase() {
                @Override
                public void onFinish(Object value) {
                    MLocation epfl2 = (MLocation) value;
                    epfl2.setIsGroupLocation(1);
                    epfl2.setRadius(2000);
                    Database.getInstance().writeInstanceObj(epfl2, Database.Tables.LOCATIONS);
                }

                @Override
                public void onError(DatabaseError error) {

                }
            });
          //  mapListener.setMyPos(myPos);

            // Do locations here
            markNearbyUsers();
        }
    }

    private void initCircle(LatLng currentCoordinates) {
        radiusOptions = new CircleOptions().center(currentCoordinates)
                .strokeColor(Color.RED)
                .fillColor(Color.parseColor("#22FF0000"))
                .radius(radius);
        getActivity().runOnUiThread(new Runnable(){
            public void run(){
                mobileMap.addCircle(radiusOptions);
            }
        });
    }

    private void moveCamera(final LatLng latLng, final float zoom) {
        Log.d( TAG, "moveCamera: moving the camera to: lat: "
                + latLng.latitude + " long: " + latLng.longitude);
        getActivity().runOnUiThread(new Runnable(){
            public void run(){
                mobileMap.moveCamera(CameraUpdateFactory.newLatLngZoom( latLng, zoom));
            }
        });
    }

    /**
     * Marks the other users that are within the distance specified by the users.
     * */
    public void markNearbyUsers() {

        // Clear Markers
      //  mapMarkers.removeAll(mapMarkers);
        getActivity().runOnUiThread(new Runnable(){
            public void run(){
                mobileMap.clear();

                mobileMap.addCircle(radiusOptions);
            }
        });
        usersLoc = new ArrayList<>(OthersInfo.getInstance().getUsersInRadius().values());

        if(usersLoc.size() > 3)
            Log.d( TAG, "moveCamera: moving the camera to: lat: " + usersLoc.size());

        getFriendsID();
        if(usersLoc != null) {
            for (int i = 0; i < usersLoc.size(); i++) {
                markNearbyUser(i, usersLoc.get(i).getMessage(), usersLoc.get(i).getTitle(),
                usersLoc.get(i).getID());
            }
        }
    }

    public void getFriendsID() {

        friendsID = UserInfo.getInstance().getCurrentUser().getFriends();

    }

    public void markNearbyUser(int indexOfUser, String status, String userName, String locID) {
        LatLng newPos = new LatLng(usersLoc.get(indexOfUser).getLatitude(),
                                    usersLoc.get(indexOfUser).getLongitude()    );
        float color = friendsID.contains(locID) ? BitmapDescriptorFactory.HUE_BLUE :
                                                        BitmapDescriptorFactory.HUE_RED;

        final MarkerOptions marker = new MarkerOptions().position(newPos)
                .title(userName + ": " + status)
                .icon(BitmapDescriptorFactory.defaultMarker(color));
        mapMarkers.add(marker);
        getActivity().runOnUiThread(new Runnable(){
            public void run(){
                mobileMap.addMarker(marker);

            }
        });


    }

    @Override
    public void onLocationChange(String id) {
        radius = UserInfo.getInstance().getCurrentPosition().getRadius();
        coord = new LatLng(UserInfo.getInstance().getCurrentPosition().getLatitude(),
                UserInfo.getInstance().getCurrentPosition().getLongitude());
        if (getActivity() != null)
            initMap();
    }
}
