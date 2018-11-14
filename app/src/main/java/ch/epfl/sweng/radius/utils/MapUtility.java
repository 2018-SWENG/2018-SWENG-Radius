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
import com.google.firebase.database.DatabaseError;

import org.mockito.internal.matchers.Null;

import java.util.ArrayList;
import java.util.HashMap;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.profile.ProfileFragment;

public class MapUtility {
    private static final String TAG = "MapUtility";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOC_PERMIT_REQUEST_CODE = 1234;
    private static final double DEFAULT_LATITUDE = 46.5191;
    private static final double DEFAULT_LONGITUDE = 6.5668;

    private static FusedLocationProviderClient mblFusedLocationClient;
    private static boolean mblLocationPermissionGranted;
    private Location currentLocation;
    private MLocation myPos;//private static MLocation myPos;
    private double radius;
    private LatLng currCoordinates;

    private static HashMap<String, MLocation> otherPos;


    public MapUtility(double radius) {
        this.radius = radius;
        currCoordinates = new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        this.myPos = new MLocation(Database.getInstance().getCurrent_user_id(),
                DEFAULT_LONGITUDE,
                DEFAULT_LATITUDE);
        if(otherPos == null)
            otherPos = new HashMap<>();
    }

    public void fetchUsersInRadius(final int radius){
        final Database database = Database.getInstance();

        database.readAllTableOnce(Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                database.readAllTableOnce(Database.Tables.LOCATIONS, new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        for(MLocation loc : (ArrayList<MLocation>) value){
                            if(isInRadius(loc, radius)) {
                                recordLocationIfVisible(loc);
                            }
                        }
                    }
                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase", error.getMessage());
                    }
                });
            }
            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase Error", error.getMessage());
            }
        });
    }

    private void recordLocationIfVisible(final MLocation location) {
        final Database database = Database.getInstance();
        database.readObjOnce(new User(location.getID()),
                Database.Tables.USERS, new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        if (((User) value).isVisible()) {
                            otherPos.put(location.getID(), location);
                        }
                    }
                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase Error", error.getMessage());
                    }
                });
    }

    public boolean isInRadius(MLocation loc, int radius){
        return computeDistance(loc) <= radius * 1000;
    }

    public ArrayList<MLocation> getOtherLocations() {
        return new ArrayList<>(otherPos.values());
    }

    public void setMyPos(MLocation myPos) {
        this.myPos = myPos;
    }

    public HashMap<String, MLocation> getOtherPos() {
        return otherPos;
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

                            try {
                                LatLng currentCoordinates = new LatLng( currentLocation.getLatitude(), currentLocation.getLongitude());
                                setCurrCoordinates(currentCoordinates);
                              
                            } catch(NullPointerException e) {}
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
        Log.e("Map","Distance is :" + Double.toString(distance[0]) + "currCoordinates.latitude" + currCoordinates.latitude + "currCoordinates.longitude" + currCoordinates.longitude);
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

}
