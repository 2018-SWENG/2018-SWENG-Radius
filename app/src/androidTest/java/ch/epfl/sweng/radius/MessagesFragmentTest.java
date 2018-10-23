package ch.epfl.sweng.radius;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.radius.browseProfiles.BrowseProfilesActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class MessagesFragmentTest {


    @Rule
    public ActivityTestRule<AccountActivity> mblActivityTestRule
            = new ActivityTestRule<AccountActivity>(AccountActivity.class);

    private AccountActivity mblAccountActivity;
    private FrameLayout fcontainer;
    private Fragment fragment;

    @Before
    public void setUp() throws Exception {
        mblAccountActivity = mblActivityTestRule.getActivity();
        fcontainer = mblAccountActivity.findViewById(R.id.fcontainer);
        fragment = new MessagesFragment();
    }

    @Test
    public void testLaunch() {
        assertNotNull(fcontainer);

        mblAccountActivity.getSupportFragmentManager().beginTransaction()
                .add(fcontainer.getId(), fragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();

        View view = fragment.getView().findViewById(R.id.listView);
        assertNotNull(view);
        view = fragment.getView().findViewById(R.id.username);
        assertNotNull(view);
        view = fragment.getView().findViewById(R.id.profilePic);
        assertNotNull(view);
    }

    @Test
    public void testChatsList() {
        onView(withId(R.id.listView)).check(matches((isDisplayed())));
        //onView(withId(R.id.username)).check(matches((isDisplayed())));
        //onView(withId(R.id.profilePic)).check(matches((isDisplayed())));

    }

    @Test
    public void testBrowseProfilesActivity() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

        final ListView listview = fragment.getView().findViewById(R.id.listView);

        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                int position = 0;
                listview.performItemClick(listview.getChildAt(position), position, listview.getAdapter().getItemId(position));
            }
        });

        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(BrowseProfilesActivity.class.getName(), null, false);
        Activity browseProfilesActivity = instrumentation.waitForMonitorWithTimeout(monitor, 5000);

        TextView username = browseProfilesActivity.findViewById(R.id.clickedName);
        assertThat(username.getText().toString(), is("john doe"));
        //ImageView image = (ImageView) browseProfilesActivity.findViewById(R.id.clickedPic);
        //assertThat(image.getId(), is (1));
    }

    @After
    public void tearDown() throws Exception {
        mblAccountActivity = null;
    }
}