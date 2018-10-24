package ch.epfl.sweng.radius;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HomeFragmentTest {

    @Rule
    public ActivityTestRule<AccountActivity> mblActivityTestRule
            = new ActivityTestRule<AccountActivity>(AccountActivity.class);


    private FrameLayout fcontainer;private AccountActivity mblAccountActivity;
    private Fragment fragment;

    /**
     * Set up the test.
     * */
    @Before
    public void setUp() {
        mblAccountActivity = mblActivityTestRule.getActivity();
        fcontainer = mblAccountActivity.findViewById(R.id.fcontainer);
        fragment = new HomeFragment();
    }

    @Test
    public void testMarkButton() {
        onView(withId(R.id.navigation_profile)).perform(click());
        onView(withId(R.id.navigation_home)).perform(click());
        onView(withId(R.id.testMark)).perform(click());
    }

    @Test
    public void testOnCreate() {
        ((HomeFragment)fragment).onCreate(null);
    }

    @Test
    public void testLaunch() {
        assertNotNull(fcontainer);

        mblAccountActivity.getSupportFragmentManager().beginTransaction()
                .add(fcontainer.getId(), fragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();

        View view = fragment.getView().findViewById(R.id.map);
        assertNotNull(view);
    }

    @After
    public void tearDown() {
        mblAccountActivity = null;
    }
}