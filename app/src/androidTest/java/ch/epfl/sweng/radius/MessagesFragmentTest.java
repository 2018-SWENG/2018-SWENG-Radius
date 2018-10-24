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
    /*
    @Rule
    public ActivityTestRule<BrowseProfilesActivity> bpActivityTestRule
            = new ActivityTestRule<BrowseProfilesActivity>(BrowseProfilesActivity.class);
    */
    private AccountActivity mblAccountActivity;
    //private BrowseProfilesActivity browseProfilesActivity;
    private FrameLayout fcontainer;
    private Fragment fragment;
    private ListView chats;
    private Instrumentation instrumentation;
    private Instrumentation.ActivityMonitor monitor;

    @Before
    public void setUp() throws Exception {
        mblAccountActivity = mblActivityTestRule.getActivity();
        //browseProfilesActivity = bpActivityTestRule.getActivity();
        fcontainer = mblAccountActivity.findViewById(R.id.fcontainer);
        fragment = new MessagesFragment();
        instrumentation = getInstrumentation();
        monitor = instrumentation.addMonitor(BrowseProfilesActivity.class.getName(), null, false);
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

        chats = fragment.getView().findViewById(R.id.listView);
        assertNotNull(chats);
    }

    @Test
    public void testChatsList() {
        //onView(withId(R.id.listView)).check(matches((isDisplayed())));
        //onView(withId(R.id.username)).check(matches((isDisplayed())));
        //onView(withId(R.id.profilePic)).check(matches((isDisplayed())));

    }

    @Test
    public void testBrowseProfilesActivity() {
        mblAccountActivity.getSupportFragmentManager().beginTransaction()
                .add(fcontainer.getId(), fragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();

        final ListView listview = mblAccountActivity.findViewById(R.id.listView);
        assertNotNull(listview);

        //Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                int position = 0;
                listview.performItemClick(listview.getChildAt(position), position, listview.getAdapter().getItemId(position));

            }
        });

        //Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(BrowseProfilesActivity.class.getName(), null, false);
        Activity browseProfilesActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 30000);
        //assertNotNull(browseProfilesActivity);

        //TextView username = browseProfilesActivity.findViewById(R.id.clickedName);
        //assertThat(username.getText().toString(), is("john doe"));
        //ImageView image = (ImageView) browseProfilesActivity.findViewById(R.id.clickedPic);
        //assertThat(image.getId(), is (1));
    }

    @After
    public void tearDown() throws Exception {
        mblAccountActivity = null;
    }
}