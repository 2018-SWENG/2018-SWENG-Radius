package ch.epfl.sweng.radius;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    @Test
    public void canSignIn() {
        onView(withId(R.id.googleButton)).perform(click());
        // perform(closeSoftKeyboard());
        //onView(withId(R.id.mainButton)).perform(click());
        // onView(withId(R.id.greetingMessage)).check(matches(withText("Hello from unit test!")));

    }
}
