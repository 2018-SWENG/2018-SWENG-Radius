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
import java.util.HashMap;

import ch.epfl.sweng.radius.database.DBObserver;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserFetchCallback;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.profile.ProfileFragment;

public class MapUtility implements DBObserver {
    private static final String TAG = "MapUtility";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOC_PERMIT_REQUEST_CODE = 1234;
    private static final double DEFAULT_LATITUDE = 46.5191;
    private static final double DEFAULT_LONGITUDE = 6.5668;

    private static FusedLocationProviderClient mblFusedLocationClient;
    private static boolean mblLocationPermissionGranted;
    private Location currentLocation;
    private static MLocation myPos;//private static MLocation myPos;
    private static LatLng currCoordinates;

    private static HashMap<String, MLocation> otherPos;


    public MapUtility(double radius) {
        currCoordinates = new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        myPos = UserInfo.getInstance().getCurrentPosition();
        if(otherPos == null)
            otherPos = new HashMap<>();
    }

    public void fetchUsersInRadius(final int radius) {
        final Database database = Database.getInstance();
        database.readAllTableOnce(Database.Tables.LOCATIONS, new UserFetchCallback(radius));
    }

    public static boolean isInRadius(MLocation loc, int radius){
        if(loc == null)
            return false;
        return findDistance(loc.getLatitude(), loc.getLongitude()) <= radius ;
    }

    public ArrayList<MLocation> getOtherLocations() {
        return new ArrayList<>(otherPos.values());
    }

    public void setMyPos(MLocation mPos) {
        myPos = mPos;
    }

    public HashMap<String, MLocation> getOtherPos() {
        return otherPos;
    }

    public void getDeviceLocation(final FragmentActivity activity) {
        mblFusedLocationClient = LocationServices.getFusedLocationProviderClient( activity);
        try {
            if ( mblLocationPermissionGranted) {
                Task location = mblFusedLocationClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if ( task.isSuccessful() && task.getResult() != null) {
                            currentLocation = (Location) task.getResult();
                            LatLng currentCoordinates = new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude());
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

    public double computeDistance(MLocation loc){

        if(loc == null)
            return Double.MAX_VALUE;

        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(loc.getLatitude() - myPos.getLatitude());
        double lngDiff = Math.toRadians(loc.getLongitude()-myPos.getLongitude());
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(myPos.getLatitude())) *
                        Math.cos(Math.toRadians(loc.getLatitude())) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }

    public void setCurrCoordinates(LatLng curCoordinates) {

        currCoordinates = curCoordinates;
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
        Log.e("MapUtility", Boolean.toString(myPos.getRadius() >= distance) + " radius " + myPos.getRadius());
        return myPos.getRadius() >= distance;
    }

    /**
     * Finds the distance between two users.
     * @param p2latitude - double - latitude of the second location
     * @param p2longtitude - double - longtitude of the second location
     * @return distance-double- the distance between the current location and the a second location
     * */
    public static double findDistance(double p2latitude, double p2longtitude) {
        float[] distance = new float[3];
        Location.distanceBetween( myPos.getLatitude(), myPos.getLongitude(),
                p2latitude, p2longtitude, distance);
        Log.e("Map","Distance is : " + Double.toString(distance[0]) + " currCoordinates.latitude " + myPos.getLatitude() + " currCoordinates.longitude " + myPos.getLongitude());
        return distance[0];
    }

    public static double findDistance(MLocation loc1, MLocation loc2) {
        float[] distance = new float[3];
        Location.distanceBetween( loc1.getLatitude(), loc1.getLongitude(),
                loc2.getLatitude(), loc2.getLongitude(), distance);
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

    public void setPermissionResult(boolean permission) {
        mblLocationPermissionGranted = permission;
    }

    @Override
    public void onDataChange(String id) {
        if(id.equals(Database.Tables.LOCATIONS.toString()))
            myPos = UserInfo.getInstance().getCurrentPosition();
    }
}
