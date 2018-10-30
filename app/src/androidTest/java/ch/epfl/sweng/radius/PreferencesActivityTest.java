package ch.epfl.sweng.radius;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.preference.Preference;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
    }

    @Test
    public void testLaunch() {
        fragment =  (PreferencesActivity.MyPreferenceFragment) mblPreferenceActivity
                .getFragmentManager().findFragmentByTag("preferencesFragment");
        assertNotNull(fragment);

        //mblPreferenceActivity.getFragmentManager().beginTransaction()
        //        .add(fcontainer.getId(), fragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();

        Preference incognitoSwitch = fragment.findPreference("incognitoSwitch");
        Preference notifCheckBox = fragment.findPreference("notificationCheckbox");
        Preference nightModeSwitch = fragment.findPreference("nightModeSwitch");
        Preference logoutButton = fragment.findPreference("logOutButton");
        Preference deleteAccount = fragment.findPreference("deleteAccount");

        assertNotNull(incognitoSwitch);
        assertNotNull(notifCheckBox);
        assertNotNull(nightModeSwitch);
        assertNotNull(logoutButton);
        assertNotNull(deleteAccount);

    }

    public void testLogout(){

    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        this.mblPreferenceActivity = null;
    }
}
