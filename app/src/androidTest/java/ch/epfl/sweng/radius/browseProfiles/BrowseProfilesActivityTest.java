package ch.epfl.sweng.radius.browseProfiles;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.radius.AccountActivity;
import ch.epfl.sweng.radius.R;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class BrowseProfilesActivityTest extends ActivityInstrumentationTestCase2<BrowseProfilesActivity> {

    private BrowseProfilesActivity mblBrowseProfilesActivity;

    @Rule
    public final ActivityTestRule<BrowseProfilesActivity> mActivityRule =
            new ActivityTestRule<>(BrowseProfilesActivity.class);

    public BrowseProfilesActivityTest() {
        super(BrowseProfilesActivity.class);
    }

    /*@Before
    public void setUp() throws Exception {
        super.setUp();

        Intent intent = new Intent();
        mblBrowseProfilesActivity = mActivityRule.launchActivity(intent);
    }*/

    /*@Test
    public void testLaunch() {
        //View view = mblBrowseProfilesActivity.getActionBar().getCustomView().findViewById(R.id.options);//.performClick();
        //assertNotNull(view);
    }*/

    /*@Test
    public void testOnCreate() {
        View view = mActivityRule.getActivity().findViewById(R.id.navigationView);
        assertNotNull(view);
        //openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        //onView(withText("Block User")).perform(click());
    }*/

    /*@After
    public void tearDown() throws Exception {

    }*/
}