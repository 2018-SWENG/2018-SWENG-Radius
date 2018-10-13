package ch.epfl.sweng.radius;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import ch.epfl.sweng.radius.database.User;

public class HomeFragment extends Fragment implements OnMapReadyCallback, RadiusCircle {

    //constants
    private static final String TAG = "HomeFragment";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOC_PERMIT_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final double DEFAULT_RADIUS = 50000; //In meters

    //properties
    private static GoogleMap mobileMap;
    private static boolean mblLocationPermissionGranted;
    private static MapView mapView;
    private static Location currentLocation;
    // might need to delete them later or switch to them
    private static double latitude;
    private static double longtitude;

    private static CircleOptions radiusOptions;
    private static Circle radiusCircle;
    // might need to delete them later or switch to them
    private static double radius; // don't delete radius
    private static LatLng currCoordinates;

    private FusedLocationProviderClient mblFusedLocationClient;

    //testing
    private static ArrayList<User> users;
    private Button testMark;
    private Button testLoc;
    private Button testRad;
    private Button testRad2;

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
        /*Bundle args = new Bundle();
        args.putDouble("radiusValue", radiusValue);
        fragment.setArguments(args);*/
        return fragment;
    }

    public HomeFragment() {
        users = new ArrayList<User>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currCoordinates = new LatLng(savedInstanceState
                    .getDouble("latitude", 0), savedInstanceState.getDouble("longtitude", 0));

            radius = savedInstanceState.getDouble("radius", DEFAULT_RADIUS);
            latitude = currCoordinates.latitude; // might need to delete them later
            longtitude = currCoordinates.longitude;
        }
        else {
            radius = DEFAULT_RADIUS;
        }

        if (currentLocation == null) {
            LocationListener locListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longtitude = location.getLongitude();

                    LatLng currCoordinates = new LatLng(latitude, longtitude);
                    moveCamera(currCoordinates, DEFAULT_ZOOM);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
        }
        else {
            LatLng currCoordinates = new LatLng(latitude, longtitude);
            moveCamera(currCoordinates, DEFAULT_ZOOM);
        }
        //getLocationPermission();
    }

    @Override
    public View onCreateView(LayoutInflater infltr, ViewGroup containr, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*if (savedInstanceState != null) {
            currCoordinates = new LatLng(savedInstanceState
                    .getDouble("latitude", 0), savedInstanceState.getDouble("longtitude", 0));
            if ( getArguments() != null) {
                radius = getArguments().getDouble("radiusValue", DEFAULT_RADIUS) * 1000; //converting km to m.
            }
            radius = savedInstanceState.getDouble("radius", DEFAULT_RADIUS);
            latitude = currCoordinates.latitude; // might need to delete them later
            longtitude = currCoordinates.longitude;
        }
        else {
            radius = DEFAULT_RADIUS;
        }*/

        return infltr.inflate(R.layout.fragment_home, containr, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //For testing purposes,delete later
        testMark = view.findViewById(R.id.testMark);
        testMark.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User marc = new User(); marc.setLocation(new LatLng(46.524434, 6.570222));
                User jean = new User(); jean.setLocation(new LatLng(46.514874, 6.567602));
                User marie = new User(); marie.setLocation(new LatLng(46.521877, 6.588810));
                users.add(marc); users.add(jean); users.add(marie);
                markNearbyUsers();
            }
        });

        testRad = view.findViewById(R.id.testRad);
        testRad.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRadius(500);markNearbyUsers();
            }
        });

        testRad2 = view.findViewById(R.id.testRad2);
        testRad2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRadius(2000);markNearbyUsers();
            }
        });

        testLoc = view.findViewById(R.id.testLoc);
        testLoc.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileMap.clear();
                LatLng newLocation = new LatLng(46.521202, 6.552371);
                currentLocation.setLongitude(newLocation.longitude);
                currentLocation.setLatitude(newLocation.latitude);
                initCircle(newLocation);
                markNearbyUsers(); //mobileMap.addCircle(radiusOptions);
            }
        });

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            currCoordinates = new LatLng(savedInstanceState
                    .getDouble("latitude", 0), savedInstanceState.getDouble("longtitude", 0));
            radius = savedInstanceState.getDouble("radius", 350);
            latitude = currCoordinates.latitude; // might need to delete them later
            longtitude = currCoordinates.longitude;
        }
        else {
            radius = 750;
        }
    }*/

    @Override
    public void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);

        outstate.putDouble("radius", radiusCircle.getRadius());
        outstate.putDouble("latitude", currentLocation.getLatitude());
        outstate.putDouble("lontitude", currentLocation.getLongitude());
        //outstate.putBoolean("permissionGranted", mblLocationPermissionGranted);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mobileMap = googleMap;

        getLocationPermission();

        if (mblLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getContext(),
                   Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                   && ActivityCompat.checkSelfPermission(getContext(),
                   Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            mobileMap.setMyLocationEnabled(true);
        }
    }

    private void getDeviceLocation() {
        Log.d( TAG, "getDeviceLocation: getting the device's current location");

        mblFusedLocationClient = LocationServices.getFusedLocationProviderClient( getActivity());

        try {
            if ( mblLocationPermissionGranted) {
                Task location = mblFusedLocationClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if ( task.isSuccessful()) {
                            Log.d( TAG, "onComplete: found location.");
                            currentLocation = (Location) task.getResult();

                            LatLng currentCoordinates = new LatLng( currentLocation.getLatitude(),
                                    currentLocation.getLongitude());
                            latitude = currentLocation.getLatitude();
                            longtitude = currentLocation.getLongitude();

                            initCircle(currentCoordinates);
                            markNearbyUsers();
                            moveCamera( currentCoordinates, DEFAULT_ZOOM);
                        }
                        else {
                            Log.d( TAG, "onComplete: current location is null.");
                            Toast.makeText( getContext(), "Unable to get current location",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch ( SecurityException e) {
            Log.e( TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void initCircle(LatLng currentCoordinates) {
        radiusOptions = new CircleOptions().center(currentCoordinates)
                .strokeColor(Color.RED)
                .fillColor(Color.parseColor("#22FF0000"))
                .radius(radius);

        radiusCircle = mobileMap.addCircle(radiusOptions);
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d( TAG, "moveCamera: moving the camera to: lat: "
                     + latLng.latitude + " long: " + latLng.longitude);
        mobileMap.moveCamera(CameraUpdateFactory.newLatLngZoom( latLng, zoom));
    }

    private void getLocationPermission() {
        Log.d( TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        //if we have permission to access location set
        // location permission to true else ask for permissions
        if ( ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission( getContext(), COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                mblLocationPermissionGranted = true;
                //getDeviceLocation();
        }
        else {
            ActivityCompat.requestPermissions( getActivity(), permissions, LOC_PERMIT_REQUEST_CODE);
        }

    }

    /*public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        Log.d( TAG, "onRequestPermissionResult: called.");
        mLocationPermissionGranted = false;

        switch ( requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if ( grantResults.length > 0) {
                    for ( int i = 0; i < grantResults.length; i++) {
                        if ( grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Log.d( TAG, "onRequestPermissionResult: permission denied.");
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    Log.d( TAG, "onRequestPermissionResult: permission granted.");
                    mLocationPermissionGranted = true;
                }
            }
        }
    }*/

    public double getRadius() {
        return radiusCircle.getRadius();
    }

    public double getLatitude() {
        return currentLocation.getLatitude();
    }

    public double getLongitude() {
        return currentLocation.getLongitude();
    }

    /**
     * Sets a new radius for the circle and updates the UI.
     * @param radius - double - new radius of the circle (in meters)
     * */
    public void setRadius(double radius) {
        this.radius = radius;
        mobileMap.clear();
        LatLng currentCoordinates = new LatLng( currentLocation.getLatitude(),
                currentLocation.getLongitude());
        radiusOptions = new CircleOptions().center(currentCoordinates)
                .strokeColor(Color.RED)
                .fillColor(Color.parseColor("#22FF0000"))
                .radius(radius);
        radiusCircle = mobileMap.addCircle(radiusOptions);
        //radiusCircle.setRadius(radius);
    }

    public void setLatitude(double latitude) {
        currentLocation.setLatitude(latitude);
    }

    public void setLongitude(double longitude) {
        currentLocation.setLongitude(longitude);
    }

    /**
     * Checks if the other users in the list of users are within the specified distance of the user.
     * @param p2latitude - double - latitude of the user that is being checked
     * @param p2longtitude - double - longtitude of the user that is being checked
     * */
    public boolean contains(double p2latitude, double p2longtitude) {
        double distance = findDistance(p2latitude, p2longtitude);
        return radiusCircle.getRadius() >= distance;
    }

    /**
     * Finds the distance between two users.
     * @param p2latitude - double - latitude of the second location
     * @param p2longtitude - double - longtitude of the second location
     * @return distance-double- the distance between the current location and the a second location
     * */
    public double findDistance(double p2latitude, double p2longtitude) {
        float[] distance = new float[3];
        Location.distanceBetween( currentLocation.getLatitude(), currentLocation.getLongitude(),
                p2latitude, p2longtitude, distance);

        return distance[0];
    }

    /**
     * Marks the other users that are within the distance specified by the users.
     * */
    public void markNearbyUsers() {
        mobileMap.clear();
        radiusCircle = mobileMap.addCircle(radiusOptions);

        if (users == null) { //write a test to check if users are null or not when the markNearbyUsers method is called
            return;
        }

        for (int i = 0; i < users.size(); i++) {
            if ( contains(users.get(i).getLocation().latitude,
                    users.get(i).getLocation().longitude))
            {
                String status = users.get(i).getStatus();
                String userName = users.get(i).getNickname();
                //radiusCircle = mobileMap.addCircle(radiusOptions);
                mobileMap.addMarker(new MarkerOptions().position(users.get(i).getLocation())
                        .title(userName + ": "  + status));
            }
        }
    }

    /**
     * Adds a user.
     * @param latitude - double - latitude of the new user that is being added to the list of users
     * @param longtitude -double-longtitude of the new user that is being added to the list of users
     * */
    public void addUser(double latitude, double longtitude) {
        if ( latitude >= -90 && latitude <= 90 && longtitude >= -180 && longtitude <= 180) {
            User temp = new User(); temp.setLocation(new LatLng(latitude, longtitude));
            users.add(temp);
        }
    }

    /**
     * Deletes user.
     * @param index - int - index of the user
     * */
    public void deleteUser(int index) {
        if ( index < users.size() && index >= 0)
            users.remove(index);
    }

    public int returnNoOfUsers() { return users.size(); }
}
