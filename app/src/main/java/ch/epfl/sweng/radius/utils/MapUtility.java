package ch.epfl.sweng.radius.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.profile.ProfileFragment;

public class MapUtility {
    private static final String TAG = "MapUtility";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOC_PERMIT_REQUEST_CODE = 1234;
    private static final double DEFAULT_LATITUDE = 46.5191;
    private static final double DEFAULT_LONGTITUDE = 6.5668;

    private static FusedLocationProviderClient mblFusedLocationClient;
    private static boolean mblLocationPermissionGranted;
    private static Location currentLocation;
    private static double radius;
    private static LatLng currCoordinates;

    private static ArrayList<User> users;

    public MapUtility(double radius, ArrayList<User> users) {
        MapUtility.radius = radius;
        MapUtility.users = users;
        currCoordinates = new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGTITUDE);
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
        MapUtility.currCoordinates = currCoordinates;
    }

    public LatLng getCurrCoordinates() {
        return currCoordinates;
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
        Location.distanceBetween( currCoordinates.latitude, currCoordinates.longitude,
                p2latitude, p2longtitude, distance);

        return distance[0];
    }

    public boolean speaksSameLanguage(User user) {
        String[] languagesSpoken = user.getProfileInfo().getSpokenLanguages().split(" ");
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

    public void setPermissionResult(boolean permission) {
        mblLocationPermissionGranted = permission;
    }

}
