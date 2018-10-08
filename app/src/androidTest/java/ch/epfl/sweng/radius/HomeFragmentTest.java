package ch.epfl.sweng.radius;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class HomeFragmentTest {

    private AccountActivity mActivity = null;
    //private FrameLayout fcontainer = null;
    private Fragment fragment;

    @Rule
    public final ActivityTestRule<AccountActivity> mActivityRule =
            new ActivityTestRule<>(AccountActivity.class);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityRule.getActivity();
        fragment = new HomeFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(1, fragment).commit();//.commitAllowingStateLoss();
    }

    @Test
    public void testLaunch() {
        //test if the fragment launches or not
        //fragment.getView().findViewById(R.id.map);
    }

    /*@Test
    public void usersTest() {
        onView(withId(R.id.map)).perform();
        FrameLayout fcontainer = (FrameLayout) mActivity.findViewById(R.id.fcontainer);

        assertNotNull(fcontainer);
        Fragment fragment = new HomeFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(fcontainer.getId(), fragment).commitAllowingStateLoss();
        ((HomeFragment)fragment).addUser(46.522917, 6.564787);
        ((HomeFragment)fragment).addUser(46.514859, 6.569271);
        ((HomeFragment)fragment).addUser(47.363532, 8.531554);
        ((HomeFragment)fragment).addUser(-91, -181);
        ((HomeFragment)fragment).addUser(-91, 181);
        ((HomeFragment)fragment).addUser(91, 181);
        ((HomeFragment)fragment).addUser(91, -181);
        ((HomeFragment) fragment).markNearbyUsers();

        ((HomeFragment) fragment).deleteUser(3);
        ((HomeFragment) fragment).markNearbyUsers();
    }*/

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}