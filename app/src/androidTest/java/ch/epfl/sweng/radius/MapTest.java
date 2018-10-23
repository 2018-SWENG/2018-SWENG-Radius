package ch.epfl.sweng.radius;

import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.MapUtility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MapTest {

    @Rule
    public ActivityTestRule<AccountActivity> mblActivityTestRule
            = new ActivityTestRule<AccountActivity>(AccountActivity.class);

    private double radius;
    private User user1, user2;
    private ArrayList<User> users;
    private MapUtility mapListener;

    private HomeFragment homeFragment;
    private AccountActivity accountActivity;

    @Before
    public void setup() {
        accountActivity = mblActivityTestRule.getActivity();
        radius = 3000;
        user1 = new User();
        user1.setLocation(new LatLng(46.524434, 6.570222));
        user2 = new User();
        user2.setLocation(new LatLng(0, 0));
        users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        homeFragment = new HomeFragment();
        mapListener = new MapUtility(radius, users);
    }

    @Test
    public void testSpeaksSameLanguages() {
        user1.setSpokenLanguages("English");
        assertTrue(!mapListener.speaksSameLanguage(user1));
    }

    @Test
    public void testUserLanguageNull() {
        user2.setSpokenLanguages(null);
        assertTrue(!mapListener.speaksSameLanguage(user2));
    }

    @Test
    public void testContains() {
        assertTrue(mapListener.contains(user1.getLocation().latitude, user1.getLocation().longitude));
        assertTrue(!mapListener.contains(user2.getLocation().latitude, user2.getLocation().longitude));
    }

    @Test
    public void testFindDistance() {
        double distanceToUser1 = mapListener.findDistance(user1.getLocation().latitude, user1.getLocation().longitude);
        assertTrue(radius >= distanceToUser1);
        double distanceToUser2 = mapListener.findDistance(user2.getLocation().latitude, user2.getLocation().longitude);
        assertTrue(radius <= distanceToUser2);
    }

    @Test
    public void testGetSetLocation() {
        LatLng currCoordinates = new LatLng(46.5191, 6.5668);
        mapListener.setCurrCoordinates(currCoordinates);

        assertEquals(currCoordinates, mapListener.getCurrCoordinates());
    }

    @Test
    public void testGetDeviceLocation() {
        assertTrue(mapListener.getPermissionResult());
        mapListener.getDeviceLocation(accountActivity);
    }

    @Test
    public void testGetDeviceLocationPermission() {
        mapListener.getLocationPermission(accountActivity.getBaseContext(), accountActivity);
        assertTrue(mapListener.getPermissionResult());
    }

    @After
    public void tearDown() {

    }

}
