package ch.epfl.sweng.radius;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.friendsList.FriendsListAdapter;
import ch.epfl.sweng.radius.friendsList.FriendsListItem;
import ch.epfl.sweng.radius.utils.MapUtility;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    //constants
    private static final String TAG = "HomeFragment";
    private static final float DEFAULT_ZOOM = 13f;
    private static final double DEFAULT_RADIUS = 50000; //In meters

    //properties
    private static GoogleMap mobileMap;
    private static MapView mapView;
    private static CircleOptions radiusOptions;
    private static double radius;

    //testing
    private static MapUtility mapListener;
    private static ArrayList<User> users;
    private Button testMark;

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
        radius = radiusValue * 1000; // converting to meters.
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        radius = DEFAULT_RADIUS;
        users = new ArrayList<User>();
    }

    @Override
    public View onCreateView(LayoutInflater infltr, ViewGroup container, Bundle savedInstanceState) {
        View view = infltr.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.friendsList);
        //mock data for testing purposes
        FriendsListItem items[] = { new FriendsListItem("John Doe",R.drawable.image1),
                new FriendsListItem("Jane Doe",R.drawable.image2),
                new FriendsListItem("Alison Star",R.drawable.image3),
                new FriendsListItem("Mila Noon",R.drawable.image4),
                new FriendsListItem("David Doyle",R.drawable.image5)};

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        FriendsListAdapter adapter = new FriendsListAdapter(items, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        testMark = view.findViewById(R.id.testMark);
        mapListener = new MapUtility(radius, users);

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mobileMap = googleMap; //use map utility here
        testMark.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User marc = new User(); marc.setLocation(new LatLng(46.524434, 6.570222));
                marc.setSpokenLanguages("English German");
                User jean = new User(); jean.setLocation(new LatLng(46.514874, 6.567602));
                jean.setSpokenLanguages("French");
                User marie = new User(); marie.setLocation(new LatLng(46.521877, 6.588810));
                marie.setSpokenLanguages(""); users.add(marc); users.add(jean); users.add(marie); markNearbyUsers();
            }
        });

        mapListener.getLocationPermission(getContext(), getActivity()); // Use map utility here

        if (mapListener.getPermissionResult()) {
            mapListener.getDeviceLocation(getActivity()); // use map utility here

            if (ActivityCompat.checkSelfPermission(getContext(),
                   Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                   && ActivityCompat.checkSelfPermission(getContext(),
                   Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }

            mobileMap.setMyLocationEnabled(true); initMap();
        }
    }

    public void initMap() {
        if (mapListener.getCurrCoordinates() != null) {
            initCircle(mapListener.getCurrCoordinates());
            moveCamera(mapListener.getCurrCoordinates(), DEFAULT_ZOOM);
            markNearbyUsers();
        }
    }

    public void initCircle(LatLng currentCoordinates) {
        radiusOptions = new CircleOptions().center(currentCoordinates)
                .strokeColor(Color.RED)
                .fillColor(Color.parseColor("#22FF0000"))
                .radius(radius);

        mobileMap.addCircle(radiusOptions);
    }

    public void moveCamera(LatLng latLng, float zoom) {
        Log.d( TAG, "moveCamera: moving the camera to: lat: "
                + latLng.latitude + " long: " + latLng.longitude);
        mobileMap.moveCamera(CameraUpdateFactory.newLatLngZoom( latLng, zoom));
    }

    /**
     * Marks the other users that are within the distance specified by the users.
     * */
    public void markNearbyUsers() {
        mobileMap.clear();
        mobileMap.addCircle(radiusOptions);

        for (int i = 0; users != null && i < users.size(); i++) {
            String status = users.get(i).getStatus();
            String userName = users.get(i).getNickname();
            markNearbyUser(i, status, userName);
        }
    }

    public void markNearbyUser(int indexOfUser, String status, String userName) {
        if ( mapListener.contains(users.get(indexOfUser).getLocation().latitude,
                users.get(indexOfUser).getLocation().longitude) && !mapListener.speaksSameLanguage(users.get(indexOfUser)))
        {
            mobileMap.addMarker(new MarkerOptions().position(users.get(indexOfUser).getLocation())
                    .title(userName + ": " + status));

        } else if (mapListener.contains(users.get(indexOfUser).getLocation().latitude,
                users.get(indexOfUser).getLocation().longitude) && mapListener.speaksSameLanguage(users.get(indexOfUser))) {
            mobileMap.addMarker(new MarkerOptions().position(users.get(indexOfUser).getLocation())
                    .title(userName + ": " + status).icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
    }
}
