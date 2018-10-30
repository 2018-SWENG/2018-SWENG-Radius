package ch.epfl.sweng.radius;

import android.Manifest;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class ProfileFragmentTest  extends ActivityInstrumentationTestCase2<AccountActivity> {


    @Rule
    public ActivityTestRule<AccountActivity> mblActivityTestRule
            = new ActivityTestRule<AccountActivity>(AccountActivity.class);
    @Rule
    public final GrantPermissionRule mPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);

    private AccountActivity mblAccountActivity;
    private FrameLayout fcontainer;
    private Fragment fragment;

    public ProfileFragmentTest(Class<AccountActivity> activityClass) {
        super(activityClass);
    }

    public ProfileFragmentTest() {
        super(AccountActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        Intent intent = new Intent();
        mblAccountActivity = mblActivityTestRule.launchActivity(intent);

    }

    @Test
    public void testLaunch() {
        FrameLayout fcontainer = mblAccountActivity.findViewById(R.id.fcontainer);
        assertNotNull(fcontainer);

        Fragment fragment = new ProfileFragment();
        mblAccountActivity.getSupportFragmentManager().beginTransaction()
                .add(fcontainer.getId(), fragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();

        View view = fragment.getView().findViewById(R.id.profileLayout); assertNotNull(view);
        view = fragment.getView().findViewById(R.id.userPhoto); assertNotNull(view);
        view = fragment.getView().findViewById(R.id.userNickname); assertNotNull(view);
        view = fragment.getView().findViewById(R.id.userStatus); assertNotNull(view);
        view = fragment.getView().findViewById(R.id.nicknameInput); assertNotNull(view);
        view = fragment.getView().findViewById(R.id.statusInput); assertNotNull(view);
        view = fragment.getView().findViewById(R.id.radiusLabel); assertNotNull(view);
        view = fragment.getView().findViewById(R.id.radiusValue); assertNotNull(view);
        view = fragment.getView().findViewById(R.id.radiusBar); assertNotNull(view);
        view = fragment.getView().findViewById(R.id.languagesButton); assertNotNull(view);
        view = fragment.getView().findViewById(R.id.spokenLanguages); assertNotNull(view);
        view = fragment.getView().findViewById(R.id.saveButton); assertNotNull(view);
    }

    @Test
    public void ztestChangeNicknameAndStatus() {
        onView(withId(R.id.navigation_profile)).perform(click());
        onView(withId(R.id.nicknameInput)).perform(typeText("User Nickname"));
        onView(withId(R.id.nicknameInput)).perform(closeSoftKeyboard());
        onView(withId(R.id.statusInput)).perform(typeText("User Status"));
        onView(withId(R.id.statusInput)).perform(closeSoftKeyboard());
    }

   /*@Test
    public void testLanguageButton() {
        onView(withId(R.id.navigation_profile)).perform(click());
        onView(withId(R.id.languagesButton)).perform(click());
    }*/

    @After
    public void tearDown() {
        mblAccountActivity = null;
    }
}