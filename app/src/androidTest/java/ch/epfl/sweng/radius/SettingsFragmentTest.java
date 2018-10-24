package ch.epfl.sweng.radius;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class SettingsFragmentTest extends ActivityInstrumentationTestCase2<AccountActivity> {


    @Rule
    public ActivityTestRule<AccountActivity> mblActivityTestRule
            = new ActivityTestRule<AccountActivity>(AccountActivity.class);

    private AccountActivity mblAccountActivity;
    private FrameLayout fcontainer;
    private Fragment fragment;

    public SettingsFragmentTest(Class<AccountActivity> activityClass) {
        super(activityClass);
    }

    public SettingsFragmentTest() {
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

    }

    @Test
    public void onCreatePreferences() {
    }

    @Test
    public void onResume() {
    }

    @Test
    public void onPause() {
    }

    @Test
    public void onSharedPreferenceChanged() {
        // TODO
    }

    @Test
    public void logOut(){
        // TODO
    }

    @Test
    public void revokeAccess(){
        // TODO
    }

    @After
    public void tearDown() throws Exception {
        mblAccountActivity = null;
    }
}