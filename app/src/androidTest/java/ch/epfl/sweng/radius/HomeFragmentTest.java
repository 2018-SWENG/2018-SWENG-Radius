package ch.epfl.sweng.radius;

import android.accounts.Account;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class HomeFragmentTest {

    @Rule
    public ActivityTestRule<AccountActivity> mblActivityTestRule = new ActivityTestRule<AccountActivity>(AccountActivity.class);

    private AccountActivity mblAccountActivity;
    private FrameLayout fcontainer;
    private Fragment fragment;

    @Before
    public void setUp() throws Exception {
        mblAccountActivity = mblActivityTestRule.getActivity();
        fcontainer = mblAccountActivity.findViewById(R.id.fcontainer);
        fragment = new HomeFragment();
    }

    @Test
    public void testLaunch() {
        //FrameLayout fcontainer = mblAccountActivity.findViewById(R.id.fcontainer);
        assertNotNull(fcontainer);

        //Fragment fragment = new HomeFragment();
        mblAccountActivity.getSupportFragmentManager().beginTransaction().add(fcontainer.getId(), fragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();

        View view = fragment.getView().findViewById(R.id.map);
        assertNotNull(view);
    }

    @Test
    public void addDeleteUsersTest() {
        //Adding new Users
        ((HomeFragment)fragment).addUser(46.524434, 6.570222);
        ((HomeFragment)fragment).addUser(46.514874, 6.567602);
        ((HomeFragment)fragment).addUser(46.521877, 6.588810);
        ((HomeFragment)fragment).addUser(-91, -181);
        ((HomeFragment)fragment).addUser(-91, 181);
        ((HomeFragment)fragment).addUser(91, 181);
        ((HomeFragment)fragment).addUser(91, -181);

        assertTrue(((HomeFragment) fragment).returnNoOfUsers() == 6);

        ((HomeFragment) fragment).deleteUser(5);
        assertTrue(((HomeFragment) fragment).returnNoOfUsers() == 5);
    }

    @After
    public void tearDown() throws Exception {
        mblAccountActivity = null;
    }
}