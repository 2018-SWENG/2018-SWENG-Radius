package ch.epfl.sweng.radius;

import android.Manifest;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.radius.browseProfiles.BrowseProfilesActivity;
import ch.epfl.sweng.radius.database.Database;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class BrowseProfilesActivityTest extends ActivityInstrumentationTestCase2<BrowseProfilesActivity> {
    @Rule
    public final ActivityTestRule<BrowseProfilesActivity> mActivityRule =
            new ActivityTestRule<>(BrowseProfilesActivity.class);


    public BrowseProfilesActivityTest(){
        super(BrowseProfilesActivity.class);
    }

    private BrowseProfilesActivity mActivity;

    @Rule
    public final GrantPermissionRule mPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);

    public BrowseProfilesActivityTest(Class<BrowseProfilesActivity> activityClass) {
        super(activityClass);
    }


    @Before
    public void setUp() throws Exception {
        super.setUp();
        Database.activateDebugMode();


        Intent intent = new Intent();
        intent.putExtra("Clicked Picture", R.drawable.image1);
        intent.putExtra("Clicked Name", "testUser3");
        intent.putExtra("UID", "testUser3");
        mActivity = mActivityRule.launchActivity(intent);
    }

    @Test
    public void testViewExists() {
        View view = mActivity.findViewById(R.id.clickedPic);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.clickedName);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.other_status);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.block_user);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.add_user);
        assertNotNull(view);
    }

    @Test
    public void testAddFriends() {
        Espresso.onView(withId(R.id.add_user)).perform(click());
    }


}