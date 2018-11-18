package ch.epfl.sweng.radius.messages;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.radius.AccountActivity;
import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.FakeFirebaseUtility;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MessagesFragmentTest extends ActivityInstrumentationTestCase2<AccountActivity> {

    @Rule
    public ActivityTestRule<AccountActivity> mblActivityTestRule
            = new ActivityTestRule<AccountActivity>(AccountActivity.class);

    private AccountActivity mblAccountActivity;
    private ListView chats;

    public MessagesFragmentTest() {
        super(AccountActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Database.activateDebugMode();
        ((FakeFirebaseUtility) Database.getInstance()).fillDatabase();

        mblAccountActivity = mblActivityTestRule.getActivity();

        Intent intent = new Intent();
        mblAccountActivity = mblActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testLaunch() {
        FrameLayout fcontainer = mblAccountActivity.findViewById(R.id.fcontainer);
        Fragment fragment = new MessagesFragment();
        mblAccountActivity.getSupportFragmentManager().beginTransaction()
                .add(fcontainer.getId(), fragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();

        View view = fragment.getView().findViewById(R.id.listView);
        assertNotNull(view);
        /* Those are not present anymore
        view = fragment.getView().findViewById(R.id.username);
       assertNotNull(view);
        view = fragment.getView().findViewById(R.id.profilePic);
       assertNotNull(view);
       */

        chats = fragment.getView().findViewById(R.id.listView);
        assertNotNull(chats);
    }

    @Test
    public void testBrowseProfilesActivity() {
        Espresso.onView(withId(R.id.navigation_messages)).perform(click());
/*
        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.profilePic))
                .perform(click());
        Espresso.onView(withId(R.id.clickedName)).equals("john doe");
*/
    }

    @Test
    public void testIfChatIsOpening(){
/*
        Espresso.onView(withId(R.id.navigation_messages)).perform(click());
        onData(anything())
                .inAdapterView(withId(R.id.listView))
                .atPosition(0)
                .onChildView(withId(R.id.username))
                .perform(click());
        Espresso.onView(withId(R.id.clickedName)).equals("john doe");
*/
    }

    @After
    public void tearDown() throws Exception {
        mblAccountActivity = null;
    }
}