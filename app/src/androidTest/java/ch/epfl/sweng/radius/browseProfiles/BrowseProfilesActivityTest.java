package ch.epfl.sweng.radius.browseProfiles;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
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

public class BrowseProfilesActivityTest {

    @Rule
    public final ActivityTestRule<BrowseProfilesActivity> mActivityRule =
            new ActivityTestRule<>(BrowseProfilesActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    /*@Test
    public void testOnCreate() {
        View view = mActivityRule.getActivity().findViewById(R.id.navigationView);
        assertNotNull(view);
        //openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        //onView(withText("Block User")).perform(click());
    }*/

    @After
    public void tearDown() throws Exception {

    }
}