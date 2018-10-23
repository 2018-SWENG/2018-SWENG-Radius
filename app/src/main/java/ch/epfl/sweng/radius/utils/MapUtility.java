package ch.epfl.sweng.radius.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import ch.epfl.sweng.radius.ProfileFragment;
import ch.epfl.sweng.radius.RadiusCircle;
import ch.epfl.sweng.radius.database.User;

public class MapUtility {
    private static final String TAG = "MapUtility";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOC_PERMIT_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    //private static final double DEFAULT_RADIUS = 50000; //In meters

    private static FusedLocationProviderClient mblFusedLocationClient;
    private static boolean mblLocationPermissionGranted;
    private static Location currentLocation;
    private static CircleOptions radiusOptions;
    private static Circle radiusCircle;
    private static double radius;
    private static GoogleMap mobileMap;
    private static LatLng currCoordinates;

    private static ArrayList<User> users;

    public MapUtility(double radius, ArrayList<User> users) {
        this.radius = radius;
        this.users = users;
    }

    public void getDeviceLocation(final FragmentActivity activity) {
        mblFusedLocationClient = LocationServices.getFusedLocationProviderClient( activity);
        try {
            if ( mblLocationPermissionGranted) {
                Task location = mblFusedLocationClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if ( task.isSuccessful()) {
                            currentLocation = (Location) task.getResult();
                            LatLng currentCoordinates = new LatLng( currentLocation.getLatitude(),
                                    currentLocation.getLongitude());

                            setCurrCoordinates(currentCoordinates);
                            /*initCircle(currentCoordinates);
                            markNearbyUsers();
                            moveCamera( currentCoordinates, DEFAULT_ZOOM);*/
                        }
                        else {
                            Toast.makeText( activity.getApplicationContext(), "Unable to get current location",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch ( SecurityException e) {
            Log.e( TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    public void setCurrCoordinates(LatLng currCoordinates) {
        this.currCoordinates = currCoordinates;
    }

    public LatLng getCurrCoordinates() {
        return this.currCoordinates;
    }

    public void getLocationPermission(Context context, FragmentActivity activity) {
        Log.d( TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        //if we have permission to access location set
        // location permission to true else ask for permissions
        if ( ContextCompat.checkSelfPermission(context, FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission( context, COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mblLocationPermissionGranted = true;
        }
        else {
            ActivityCompat.requestPermissions( activity, permissions, LOC_PERMIT_REQUEST_CODE);
        }

    }

    /**
     * Checks if the other users in the list of users are within the specified distance of the user.
     * @param p2latitude - double - latitude of the user that is being checked
     * @param p2longtitude - double - longtitude of the user that is being checked
     * */
    public boolean contains(double p2latitude, double p2longtitude) {
        double distance = findDistance(p2latitude, p2longtitude);
        return radius >= distance;
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

    public boolean speaksSameLanguage(User user) {
        String[] languagesSpoken = user.getSpokenLanguages().split(" ");
        Fragment profileFragment = ProfileFragment.newInstance();
        String languagesSpokenByCurrUser = ((ProfileFragment)profileFragment).getLanguagesText();

        for (int i = 0; i < languagesSpoken.length; i++) {
            if (languagesSpokenByCurrUser != null && languagesSpokenByCurrUser.contains(languagesSpoken[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean getPermissionResult() {
        return mblLocationPermissionGranted;
    }

    /*private void initCircle(LatLng currentCoordinates) {
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
    }*/

    /*public double getRadius() {
        return radiusCircle.getRadius();
    }*/

    /**
     * Sets a new radius for the circle and updates the UI.
     * @param radius - double - new radius of the circle (in meters)
     * */
    /*public void setRadius(double radius) {
        this.radius = radius;
        mobileMap.clear();
        LatLng currentCoordinates = new LatLng( currentLocation.getLatitude(),
                currentLocation.getLongitude());
        radiusOptions = new CircleOptions().center(currentCoordinates).strokeColor(Color.RED)
                .fillColor(Color.parseColor("#22FF0000")).radius(radius);
        radiusCircle = mobileMap.addCircle(radiusOptions);
    }*/

    /**
     * Marks the other users that are within the distance specified by the users.
     * */
    /*public void markNearbyUsers() {
        mobileMap.clear();
        radiusCircle = mobileMap.addCircle(radiusOptions);

        for (int i = 0; users != null && i < users.size(); i++) {
            String status = users.get(i).getStatus();
            String userName = users.get(i).getNickname();
            markNearbyUser(i, status, userName);
        }
    }

    private void markNearbyUser(int indexOfUser, String status, String userName) {
        if ( contains(users.get(indexOfUser).getLocation().latitude,
                users.get(indexOfUser).getLocation().longitude) && !speaksSameLanguage(users.get(indexOfUser)))
        {
            mobileMap.addMarker(new MarkerOptions().position(users.get(indexOfUser).getLocation())
                    .title(userName + ": " + status));

        } else if (contains(users.get(indexOfUser).getLocation().latitude,
                users.get(indexOfUser).getLocation().longitude) && speaksSameLanguage(users.get(indexOfUser))) {
            mobileMap.addMarker(new MarkerOptions().position(users.get(indexOfUser).getLocation())
                    .title(userName + ": " + status).icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
    }*/

}
