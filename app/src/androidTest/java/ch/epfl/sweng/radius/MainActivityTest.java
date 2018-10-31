package ch.epfl.sweng.radius;


import android.Manifest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    @Rule
    public final GrantPermissionRule mPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);
    @Test
    public void canSignIn() {
        //onView(withId(R.id.googleButton)).perform(click());
        // perform(closeSoftKeyboard());
        //onView(withId(R.id.mainButton)).perform(click());
        // onView(withId(R.id.greetingMessage)).check(matches(withText("Hello from unit test!")));

    }
}
