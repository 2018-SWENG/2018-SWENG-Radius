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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

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
            Manifest.permission.ACCESS_FINE_LOCATION);

    public AccountActivityTest(Class<AccountActivity> activityClass) {
        super(activityClass);
    }


    @Before
    public void setUp() throws Exception {

        super.setUp();

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
        view = mActivity.findViewById(R.id.navigation_settings);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.navigation_profile);
        assertNotNull(view);
    }

    @Test
    public void testNavigationToHome() {
        Espresso.onView(withId(R.id.navigation_home)).perform(click());
    }

    @Test
    public void testNavigationToMessages() {
        Espresso. onView(withId(R.id.navigation_messages)).perform(click());
    }

    @Test
    public void testNavigationToSettings() {
        Espresso.onView(withId(R.id.navigation_settings)).perform(click());
    }

    @Test
    public void testNavigationToProfile() {
        Espresso.onView(withId(R.id.navigation_profile)).perform(click());
    }


}