package ch.epfl.sweng.radius;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.preference.Preference;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.PreferenceMatchers;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.FrameLayout;

import org.hamcrest.Matchers;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class PreferencesActivityTest  extends ActivityInstrumentationTestCase2<PreferencesActivity> {

    @Rule
    public ActivityTestRule<PreferencesActivity> mblActivityTestRule
            = new ActivityTestRule<PreferencesActivity>(PreferencesActivity.class);

    @Rule
    public final GrantPermissionRule mPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);

    private PreferencesActivity mblPreferenceActivity;
    private FrameLayout fcontainer;
    private PreferencesActivity.MyPreferenceFragment fragment;

    public PreferencesActivityTest(Class<PreferencesActivity> activityClass) {
        super(activityClass);
    }

    public PreferencesActivityTest() {
        super(PreferencesActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        Intent intent = new Intent();
        mblPreferenceActivity = mblActivityTestRule.launchActivity(intent);
        fragment =  (PreferencesActivity.MyPreferenceFragment) mblPreferenceActivity
                .getFragmentManager().findFragmentByTag("preferencesFragment");
    }

    @Test
    public void testLaunch() {
        assertNotNull(fragment);
        getInstrumentation().waitForIdleSync();

        Preference incognitoSwitch = fragment.findPreference("incognitoSwitch");
        Preference notifCheckBox = fragment.findPreference("notificationsCheckbox");
        Preference nightModeSwitch = fragment.findPreference("nightModeSwitch");
        Preference logoutButton = fragment.findPreference("logOutButton");
        Preference deleteAccount = fragment.findPreference("deleteAccount");

        assertNotNull(incognitoSwitch);
        assertNotNull(notifCheckBox);
        assertNotNull(nightModeSwitch);
        assertNotNull(logoutButton);
        assertNotNull(deleteAccount);
    }

    @Test
    public void testIncognitoMode(){
        Espresso.onView(AllOf.allOf(withText(R.string.incognitoTitle)))
                .perform(click());
    }

    @Test
    public void testNotifications(){
        Espresso.onView(AllOf.allOf(withText(R.string.notificationsTitle)))
                .perform(click());
    }

    @Test
    public void testNightMode(){
        Espresso.onView(AllOf.allOf(withText(R.string.nightModeTitle)))
                .perform(click());
    }

    @Test
    public void testLogOut(){
        Espresso.onView(AllOf.allOf(withText(R.string.logoutTitle)))
                .perform(click());
    }

    @Test
    public void testDeleteAccount(){
        Espresso.onView(AllOf.allOf(withText(R.string.deleteAccountTitle)))
                .perform(click());
    }



    @After
    public void tearDown() throws Exception {
        super.tearDown();
        this.mblPreferenceActivity = null;
    }
}
