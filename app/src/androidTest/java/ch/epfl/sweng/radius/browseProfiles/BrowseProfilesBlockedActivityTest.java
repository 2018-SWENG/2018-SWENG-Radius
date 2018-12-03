package ch.epfl.sweng.radius.browseProfiles;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.Database;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class BrowseProfilesBlockedActivityTest extends ActivityInstrumentationTestCase2<BrowseProfilesBlockedActivity>{
    @Rule
    public final ActivityTestRule<BrowseProfilesBlockedActivity> mActivityRule =
            new ActivityTestRule<>(BrowseProfilesBlockedActivity.class);

    public BrowseProfilesBlockedActivityTest(){
        super(BrowseProfilesBlockedActivity.class);
    }

    private BrowseProfilesBlockedActivity mActivity;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Database.activateDebugMode();
        Intent intent = new Intent();
        mActivity = mActivityRule.launchActivity(intent);
    }

    @Test
    public void testInformationMessage() {
        Espresso.onView(withId(R.id.blockedMessage)).check(matches(withText("Sorry, you cannot browse this profile")));
    }

}
