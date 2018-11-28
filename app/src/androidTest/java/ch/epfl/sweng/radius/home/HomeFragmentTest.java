package ch.epfl.sweng.radius.home;

import android.Manifest;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.FrameLayout;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Map;

import ch.epfl.sweng.radius.AccountActivity;
import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.MapUtility;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


public class HomeFragmentTest extends ActivityInstrumentationTestCase2<AccountActivity> {

    @Rule
    public ActivityTestRule<AccountActivity> mblActivityTestRule
            = new ActivityTestRule<AccountActivity>(AccountActivity.class);
    @Rule
    public final GrantPermissionRule mPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);

    private AccountActivity mblAccountActivity;

    public HomeFragmentTest(Class<AccountActivity> activityClass) {
        super(activityClass);
    }

    public HomeFragmentTest(){
        super(AccountActivity.class);
    }

    /**
     * Set up the test.
     * */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        Database.activateDebugMode();

        Intent intent = new Intent();
        mblAccountActivity = mblActivityTestRule.launchActivity(intent);
        MapUtility map = MapUtility.getMapInstance();
    }

    @Test
    public void testLaunch() {
        FrameLayout fcontainer = mblAccountActivity.findViewById(R.id.fcontainer);
        assertNotNull(fcontainer);

        Fragment fragment = new HomeFragment();

        mblAccountActivity.getSupportFragmentManager().beginTransaction()
                .add(fcontainer.getId(), fragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();

        View view = fragment.getView().findViewById(R.id.map);
        assertNotNull(view);
    }

    @Test
    public void testPeopleTab(){
        Espresso.onView(withText("TOPICS"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        Espresso.onView(withText("PEOPLE"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        Espresso.onView(withText("TOPICS"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void testGroupTab(){
        Espresso.onView(withText("TOPICS"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        Espresso.onView(withText("GROUPS"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        Espresso.onView(withText("TOPICS"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
    }

    public void testTopicTabFunctionalityValid() {
        Espresso.onView(withText("TOPICS")).check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        Espresso.onView(withText("NEW TOPIC")).check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        Espresso.onView(withText("OK")).check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
    }

    public void testOnMapDoesNotFailWithBadInput() {
        FrameLayout fcontainer = mblAccountActivity.findViewById(R.id.fcontainer);
        final Fragment fragment = new HomeFragment();

        mblAccountActivity.getSupportFragmentManager().beginTransaction()
                .add(fcontainer.getId(), fragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();

        mblAccountActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((HomeFragment)fragment).onMapReady(null);
            }
        });
    }

    @Test
    public void testTopicsTab(){
        Espresso.onView(withText("TOPICS"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        Espresso.onView(withText("PEOPLE"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        Espresso.onView(withText("TOPICS"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
    }

    @After
    public void tearDown() {
        mblAccountActivity = null;
    }
}
