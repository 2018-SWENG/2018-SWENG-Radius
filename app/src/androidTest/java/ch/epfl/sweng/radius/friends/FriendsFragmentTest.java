package ch.epfl.sweng.radius.friends;

import android.Manifest;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.radius.AccountActivity;
import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.Database;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


public class FriendsFragmentTest extends ActivityInstrumentationTestCase2<AccountActivity> {

    @Rule
    public final GrantPermissionRule mPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule
    public ActivityTestRule<AccountActivity> mblActivityTestRule
            = new ActivityTestRule<AccountActivity>(AccountActivity.class);


    private FrameLayout fcontainer;private AccountActivity mblAccountActivity;
    private Fragment fragment;

    public FriendsFragmentTest(Class<AccountActivity> activityClass) {
        super(activityClass);
    }

    public FriendsFragmentTest(){
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

    }


    @Test
    public void testLaunch() {
        FrameLayout fcontainer = mblAccountActivity.findViewById(R.id.fcontainer);
        assertNotNull(fcontainer);

        Fragment fragment = new FriendsFragment();

        mblAccountActivity.getSupportFragmentManager().beginTransaction()
                .add(fcontainer.getId(), fragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();

        View view = fragment.getView().findViewById(R.id.tabLayout);
        assertNotNull(view);

        view = fragment.getView().findViewById(R.id.viewPager);
        assertNotNull(view);
    }

    @Test
    public void testPeopleTab(){
        Espresso.onView(withId(R.id.navigation_friends)).perform(click());

        Espresso.onView(withText("FRIENDS"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        Espresso.onView(withText("REQUESTS"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        Espresso.onView(withText("FRIENDS"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void testTopicsTab(){
        Espresso.onView(withId(R.id.navigation_friends)).perform(click());

        Espresso.onView(withText("REQUESTS"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        Espresso.onView(withText("FRIENDS"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
        Espresso.onView(withText("REQUESTS"))
                .check(ViewAssertions.matches(isDisplayed()))
                .perform(click());
    }

    @After
    public void tearDown() {
        mblAccountActivity = null;
    }
}