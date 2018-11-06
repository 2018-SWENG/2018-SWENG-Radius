package ch.epfl.sweng.radius.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.radius.AccountActivity;
import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.Database;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

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
        Database.activateDebugMode();
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
    public void testChangeNicknameAndStatus() {
        Espresso.onView(withId(R.id.navigation_profile)).perform(click());
        Espresso.onView(withId(R.id.nicknameInput)).perform(typeText("User Nickname"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.statusInput)).perform(typeText("User Status"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.saveButton)).perform(scrollTo(),click());
    }

    @Test
    public void testSaveInstanceState() {
        Espresso.onView(withId(R.id.navigation_home)).perform(click());
        Espresso.onView(withId(R.id.navigation_profile)).perform(click());
        Espresso.onView(withId(R.id.statusInput)).perform(typeText("User Status"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.nicknameInput)).perform(typeText("User Nickname"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.navigation_home)).perform(click());
        Espresso.onView(withId(R.id.navigation_profile)).perform(click());
    }


   @Test
    public void testLanguageButton() {
       Espresso.onView(withId(R.id.navigation_profile)).perform(click());
       Espresso.onView(withId(R.id.languagesButton)).perform(scrollTo(),click());
       Espresso.onView(withText("OK"))
               .inRoot(RootMatchers.isDialog())
               .check(ViewAssertions.matches(isDisplayed()))
               .perform(click());

       Espresso.onView(withId(R.id.languagesButton)).perform(click());
       Espresso.onView(withText("DISMISS"))
               .inRoot(RootMatchers.isDialog())
               .check(ViewAssertions.matches(isDisplayed()))
               .perform(click());

       Espresso.onView(withId(R.id.languagesButton)).perform(click());
       Espresso.onView(withText("CLEAR ALL"))
               .inRoot(RootMatchers.isDialog())
               .check(ViewAssertions.matches(isDisplayed()))
               .perform(click());

       Espresso.onView(withId(R.id.languagesButton)).perform(click());
       Espresso.onView(withText("German"))
               .inRoot(RootMatchers.isDialog())
               .check(ViewAssertions.matches(isDisplayed()))
               .perform(click());
       Espresso.onView(withText("OK"))
               .inRoot(RootMatchers.isDialog())
               .check(ViewAssertions.matches(isDisplayed()))
               .perform(click());

   }

    @Ignore
    @Test
    public void testRestoreState() {
        rotateScreen();
    }

    private void rotateScreen() {
        Context context = InstrumentationRegistry.getTargetContext();
        int orientation
                = context.getResources().getConfiguration().orientation;

        Activity activity = mblActivityTestRule.getActivity();
        activity.setRequestedOrientation(
                (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Test
    public void testSeekBar() {
        Espresso.onView(withId(R.id.navigation_profile)).perform(click());
        Espresso.onView(withId(R.id.radiusBar)).perform(setProgress(10));

    }

    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }
            @Override
            public String getDescription() {
                return "Set a progress on a SeekBar";
            }
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }

    @After
    public void tearDown() {
        mblAccountActivity = null;
    }
}