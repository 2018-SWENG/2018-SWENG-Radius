package ch.epfl.sweng.radius;


import android.Manifest;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.utils.AppData;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);
    @Rule
    public final GrantPermissionRule mPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);

    public LoginActivityTest(){
        super(LoginActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        //AppData.INSTANCE.setTestMode(true);
        //mActivityRule.finishActivity();
        //mActivityRule.launchActivity(new Intent());
        Database.activateDebugMode();
    }

    @Test
    public void canSignIn() {
        //onView(withId(R.id.googleButton)).perform(click());
        //perform(closeSoftKeyboard());
        //onView(withId(R.id.mainButton)).perform(click());
        // onView(withId(R.id.greetingMessage)).check(matches(withText("Hello from unit test!")));

    }

    /*
    @After
    public void tearDown() {
        AppData.INSTANCE.setTestMode(false);
    }
    */
}
