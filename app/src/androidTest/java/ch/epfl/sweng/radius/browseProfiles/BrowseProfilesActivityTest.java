package ch.epfl.sweng.radius.browseProfiles;

import android.Manifest;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.UserInfo;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class BrowseProfilesActivityTest extends ActivityInstrumentationTestCase2<BrowseProfilesActivity> {
    @Rule
    public final ActivityTestRule<BrowseProfilesActivity> mActivityRule =
            new ActivityTestRule<>(BrowseProfilesActivity.class);

    private BrowseProfilesActivity mblBrowseProfilesActivity;

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
        intent.putExtra("Clicked Name", "testUser2");
        intent.putExtra("UID", "testUser2");
        mActivity = mActivityRule.launchActivity(intent);
    }

    @Test
    public void testLaunch() {
        onView(withText("Options")).perform(click());
    }

    @Test
    public void testReportUserFor() {
        onView(withText("Options")).perform(click());
        onView(withText("Report User for:")).perform(click());
    }

    @Test
    public void testBlock() {
        onView(withText("Options")).perform(click());
        onView(withText("Block User")).perform(click());
        onView(withText("Options")).perform(click());
        onView(withText("Unblock user")).perform(click());
    }

    @Test
    public void testReportUserForLanguage() {
        onView(withText("Options")).perform(click());
        onView(withText("Report User for:")).perform(click());
        onView(withText("Language")).perform(click());
    }

    @Test
    public void testReportUserForSpam() {
        onView(withText("Options")).perform(click());
        onView(withText("Report User for:")).perform(click());
        onView(withText("Spam")).perform(click());
    }

    @Test
    public void testViewExists() {
        View view = mActivity.findViewById(R.id.userPhoto);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.userLanguages);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.userStatus);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.userNickname);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.userInterests);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.add_user);
        assertNotNull(view);
        view = mActivity.findViewById(R.id.block_user);
        assertNotNull(view);
    }

    @Test
    public void testAddFriends() {
        Espresso.onView(withId(R.id.add_user)).perform(click());
        //Try clicking again
        Espresso.onView(withId(R.id.add_user)).perform(click());
        //assertTrue(UserInfo.getInstance().getCurrentUser().getFriendsRequests().contains("testUser2"));
    }

    @After
    public void tearDown() throws Exception {
        mblBrowseProfilesActivity = null;
    }

}