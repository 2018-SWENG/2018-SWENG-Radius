package ch.epfl.sweng.radius.messages;


import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.radius.AccountActivity;
import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.browseProfiles.BrowseProfilesActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class MessagesFragmentTest {


    @Rule
    public ActivityTestRule<AccountActivity> mblActivityTestRule
            = new ActivityTestRule<AccountActivity>(AccountActivity.class);

    private AccountActivity mblAccountActivity;
    private FrameLayout fcontainer;
    private Fragment fragment;
    private ListView chats;
    private Instrumentation instrumentation;
    private Instrumentation.ActivityMonitor monitor;

    @Before
    public void setUp() throws Exception {
        mblAccountActivity = mblActivityTestRule.getActivity();
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
    public void testBrowseProfilesActivity() {
        mblAccountActivity.getSupportFragmentManager().beginTransaction()
                .add(fcontainer.getId(), fragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();

        final ListView listview = mblAccountActivity.findViewById(R.id.listView);
        assertNotNull(listview);

        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                int position = 0;
                listview.performItemClick(listview.getChildAt(position), position, listview.getAdapter().getItemId(position));

            }
        });
    }

    @After
    public void tearDown() throws Exception {
        mblAccountActivity = null;
    }
}