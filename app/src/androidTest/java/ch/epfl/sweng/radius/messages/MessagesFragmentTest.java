package ch.epfl.sweng.radius.messages;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.regex.Matcher;

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
    private RecyclerView chats;
    private Fragment fragment;

    public MessagesFragmentTest() {
        super(AccountActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Database.activateDebugMode();

        mblAccountActivity = mblActivityTestRule.getActivity();

        Intent intent = new Intent();
        mblAccountActivity = mblActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testLaunch() {
        FrameLayout fcontainer = mblAccountActivity.findViewById(R.id.fcontainer);
        fragment = new MessagesFragment();
        mblAccountActivity.getSupportFragmentManager().beginTransaction()
                .add(fcontainer.getId(), fragment).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();

        View view = fragment.getView().findViewById(R.id.messageLayout);
        assertNotNull(view);

        chats = fragment.getView().findViewById(R.id.messagesList);
        assertNotNull(chats);
    }

    @Test
    public void testBrowseProfilesActivity() {
        Espresso.onView(withId(R.id.navigation_messages)).perform(click());
        Espresso.onView(withId(R.id.messagesList)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        MyViewAction.clickChildViewWithId(R.id.profile_picture)));
    }

    @Test
    public void testIfChatIsOpening(){
        Espresso.onView(withId(R.id.navigation_messages)).perform(click());
        Espresso.onView(withId(R.id.messagesList)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        MyViewAction.clickChildViewWithId(R.id.username)));
    }

    @After
    public void tearDown() throws Exception {
        mblAccountActivity = null;
    }
}

class MyViewAction {

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public org.hamcrest.Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

}