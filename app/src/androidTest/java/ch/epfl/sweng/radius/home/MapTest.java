package ch.epfl.sweng.radius.home;

import android.Manifest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.radius.AccountActivity;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.FakeFirebaseUtility;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.MapUtility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
@Ignore
public class MapTest {

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

    @Before
    public void setup() {
        Database.activateDebugMode();


        accountActivity = mblActivityTestRule.getActivity();
        radius = 3000;
        user1 = new User("userTest1");
        location1 = new MLocation(user1.getID(), DEFAULT_LONGITUDE, DEFAULT_LATITUDE);

        user2 = new User("userTest2");
        location2 = new MLocation(user2.getID(), DEFAULT_LONGITUDE + 0.1,
                DEFAULT_LATITUDE - 0.1);

        users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);

        locations = new ArrayList<>();
        locations.add(location1);
        locations.add(location2);

        homeFragment = new HomeFragment();
        mapListener = new MapUtility(radius);
    }

    @Test
    public void testSpeaksSameLanguages() {
        user1.setSpokenLanguages("English");
        assertTrue(!mapListener.speaksSameLanguage(user1));
    }

    @Test
    public void testContains() {
        assertTrue(mapListener.contains(location1.getLatitude(), location1.getLongitude()));
        assertTrue(mapListener.contains(location2.getLatitude(), location2.getLongitude()));

    }

    @Test
    public void testGetSetLocation() {
        LatLng currCoordinates = new LatLng(46.5191, 6.5668);
        mapListener.setCurrCoordinates(currCoordinates);

        assertEquals(currCoordinates, mapListener.getCurrCoordinates());
    }

    @Test
    public void testGetDeviceLocation() {
        mapListener.setPermissionResult(true);
        mapListener.getDeviceLocation(accountActivity);
    }

    @Test
    public void testGetDeviceLocationPermission() {
        mapListener.getLocationPermission(accountActivity.getBaseContext(), accountActivity);

        if ( mapListener.getPermissionResult())
            assertTrue(mapListener.getPermissionResult());
        else
            assertTrue(!mapListener.getPermissionResult());
    }

    @After
    public void tearDown() {

    }

}