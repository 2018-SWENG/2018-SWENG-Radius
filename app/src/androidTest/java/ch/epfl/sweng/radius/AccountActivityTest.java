package ch.epfl.sweng.radius;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class AccountActivityTest {
    @Rule
    public final ActivityTestRule<AccountActivity> mActivityRule =
            new ActivityTestRule<>(AccountActivity.class);

    @Test
    public void testViewExists() {
        View view = mActivityRule.getActivity().findViewById(R.id.navigationView);
        assertNotNull(view);
        view = mActivityRule.getActivity().findViewById(R.id.navigation_home);
        assertNotNull(view);
        view = mActivityRule.getActivity().findViewById(R.id.navigation_messages);
        assertNotNull(view);
        view = mActivityRule.getActivity().findViewById(R.id.navigation_settings);
        assertNotNull(view);
        view = mActivityRule.getActivity().findViewById(R.id.navigation_profile);
        assertNotNull(view);
    }

    @Test
    public void testNavigationToHome() {
        onView(withId(R.id.navigation_home)).perform(click());
    }

    @Test
    public void testNavigationToMessages() {
        onView(withId(R.id.navigation_messages)).perform(click());
    }

    @Test
    public void testNavigationToSettings() {
        onView(withId(R.id.navigation_settings)).perform(click());
    }

    @Test
    public void testNavigationToProfile() {
        onView(withId(R.id.navigation_profile)).perform(click());
    }


}