package ch.epfl.sweng.radius;

import android.Manifest;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class AccountActivityTest extends ActivityInstrumentationTestCase2<AccountActivity> {
    @Rule
    public final ActivityTestRule<AccountActivity> mActivityRule =
            new ActivityTestRule<>(AccountActivity.class);


    public AccountActivityTest(){
        super(AccountActivity.class);
    }

    private AccountActivity mActivity;

    @Rule
    public final GrantPermissionRule mPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

    public AccountActivityTest(Class<AccountActivity> activityClass) {
        super(activityClass);
    }


    @Before
    public void setUp() throws Exception {
        super.setUp();
        Database.activateDebugMode();
        OthersInfo.getInstance();

        MLocation testUser = new MLocation("userTest1");
        testUser.setTitle("testNickname");
        testUser.setMessage("testStatus");
        testUser.setInterests("testInterests");
        testUser.setSpokenLanguages("English");

        Intent intent = new Intent();
        mActivity = mActivityRule.launchActivity(intent);
    }

    @Test
    public void testViewExists() {
        View view = mActivity.findViewById(R.id.navigationView);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.navigation_home);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.navigation_messages);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.navigation_friends);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.navigation_profile);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.action_settings);
        assertNotNull(view);
    }

    @Test
    public void testNavigationToHome() {
        Espresso.onView(withId(R.id.navigation_home)).perform(click());
    }

    @Test
    public void testNavigationToMessages() {
        Espresso.onView(withId(R.id.navigation_messages)).perform(click());
    }

    @Test
    public void testNavigationToSettings() {
        try {
            Thread.sleep(5000);
            MLocation temp = new MLocation("testUser2");
            temp.setUrlProfilePhoto("./app/src/androidTest/java/ch/epfl/sweng/radius/utils/default.png");
            temp.setTitle("testUser2"); temp.setMessage("Helping witht the tests !");
            temp.setVisible(true); Database.getInstance().writeInstanceObj(temp, Database.Tables.LOCATIONS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.action_settings)).perform(click());

    }

    @Test
    public void testNavigationToProfile() {
        Espresso.onView(withId(R.id.navigation_profile)).perform(click());
    }

    @Test
    public void testNavigationToFriends() {
        Espresso.onView(withId(R.id.navigation_friends)).perform(click());
    }


    @Test
    public void testLeaveApp(){

        AccountActivity.myTimer mt = new AccountActivity.myTimer();
        mt.isSet();
        mt.run();

    }
}