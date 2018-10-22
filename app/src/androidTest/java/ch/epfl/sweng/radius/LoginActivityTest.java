package ch.epfl.sweng.radius;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);
    @Test
    public void canSignIn() {
        //onView(withId(R.id.googleButton)).perform(click());
        // perform(closeSoftKeyboard());
        //onView(withId(R.id.mainButton)).perform(click());
        // onView(withId(R.id.greetingMessage)).check(matches(withText("Hello from unit test!")));

    }
}
