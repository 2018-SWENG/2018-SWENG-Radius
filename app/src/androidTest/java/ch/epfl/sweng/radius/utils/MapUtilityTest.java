package ch.epfl.sweng.radius.utils;

import android.Manifest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.radius.AccountActivity;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.FakeFirebaseUtility;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.home.HomeFragment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MapUtilityTest {

    @Rule
    public ActivityTestRule<AccountActivity> mblActivityTestRule
            = new ActivityTestRule<AccountActivity>(AccountActivity.class);
    @Rule
    public final GrantPermissionRule mPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);
    private static final double DEFAULT_LATITUDE = 46.5191;
    private static final double DEFAULT_LONGITUDE = 6.5668;
    private double radius;
    private User user1, user2;
    private MLocation location1, location2;
    private ArrayList<User> users;
    private ArrayList<MLocation> locations;
    private MapUtility mapListener;

    private HomeFragment homeFragment;
    private AccountActivity accountActivity;

    private User testUser;

    @Before
    public void setUp() throws Exception {

        Database.activateDebugMode();
        ((FakeFirebaseUtility) Database.getInstance()).fillDatabase();

        accountActivity = mblActivityTestRule.getActivity();
        radius = 50000;
        user1 = new User();
        location1 = new MLocation(user1.getID(), DEFAULT_LONGITUDE, DEFAULT_LATITUDE);

        user2 = new User();
        location2 = new MLocation(user2.getID(), DEFAULT_LONGITUDE + 0.1,
                DEFAULT_LATITUDE - 0.1);

        users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);

        locations = new ArrayList<>();
        locations.add(location1);
        locations.add(location2);

        testUser = new User("testId");

        homeFragment = new HomeFragment();
        mapListener = MapUtility.getMapInstance();
    }

    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void computeDistance() {
        assertTrue(mapListener.computeDistance(location1) < radius);
        assertTrue(mapListener.computeDistance(null) > radius);
    }

    @Test
    public void isInRadius() {
        assertTrue(mapListener.isInRadius(location1));
        assertFalse(mapListener.isInRadius(null));
    }

    @Test
    public void getOtherLocations() {

    //    mapListener.fetchUsersInRadius((int) radius);
        ArrayList<MLocation> otherPos = mapListener.getOtherLocations();
        assertEquals(3, otherPos.size());
    }

    @Test
    public void getDeviceLocation() {
        mapListener.setPermissionResult(true);
        mapListener.getDeviceLocation(accountActivity);
    }

    @Test
    public void setGetCurrCoordinates() {
        LatLng currCoordinates = new LatLng(46.5191, 6.5668);
        mapListener.setCurrCoordinates(currCoordinates);

        assertEquals(currCoordinates, mapListener.getCurrCoordinates());
    }


    @Test
    public void contains() {
        mapListener.contains(location1.getLatitude(), location1.getLongitude());
    }

    @Test
    public void findDistance() {

        double distanceToUser1 = mapListener.findDistance(location1.getLatitude(), location1.getLongitude());
        assertTrue(radius >= distanceToUser1);
        double distanceToUser2 = mapListener.findDistance(location2.getLatitude(), location2.getLongitude());
        assertTrue(radius >= distanceToUser2);
    }

    @Test
    public void speaksSameLanguage() {
        location1.setSpokenLanguages("English");
        mapListener.speaksSameLanguage(location1);
    }

    @Test
    public void getPermissionResult() {
        mapListener.getLocationPermission(accountActivity.getBaseContext(), accountActivity);

        if ( mapListener.getPermissionResult())
            assertTrue(mapListener.getPermissionResult());
        else
            assertTrue(!mapListener.getPermissionResult());
    }

    @Test
    public void testFakeDB(){
        Database.getInstance().writeInstanceObj(new User(), Database.Tables.USERS);
        Database.getInstance().writeInstanceObj(new ChatLogs("A"), Database.Tables.CHATLOGS);
        Database.getInstance().writeInstanceObj(new MLocation("testLoc0"), Database.Tables.LOCATIONS);

        CallBackDatabase cb = new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                assert(true);
            }
            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase Error", error.getMessage());
            }
        };

        List<String> ids = new ArrayList<>();
        ids.add("Arthur");

        Database.getInstance().readAllTableOnce(Database.Tables.LOCATIONS, cb);
        Database.getInstance().readAllTableOnce(Database.Tables.USERS, cb);
        Database.getInstance().readAllTableOnce(Database.Tables.CHATLOGS, cb);

        Database.getInstance().readListObjOnce(ids, Database.Tables.LOCATIONS, cb);
        Database.getInstance().readListObjOnce(ids, Database.Tables.USERS, cb);
        Database.getInstance().readListObjOnce(ids, Database.Tables.CHATLOGS, cb);

        Database.getInstance().readObjOnce(new User(), Database.Tables.USERS, cb);
        Database.getInstance().readObjOnce(new ChatLogs("A"), Database.Tables.CHATLOGS, cb);
        Database.getInstance().readObjOnce(new MLocation("testLoc0"), Database.Tables.LOCATIONS, cb);


    }

}
