package ch.epfl.sweng.radius.message;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ch.epfl.sweng.radius.AccountActivity;
import ch.epfl.sweng.radius.ProfileFragment;
import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.message.MessageListActivity;
import ch.epfl.sweng.radius.database.Message;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MessageListActivityTest {

    @Rule
    public ActivityTestRule<MessageListActivity> mblActivityTestRule
            = new ActivityTestRule<MessageListActivity>(MessageListActivity.class);

    private MessageListActivity mlActivity;
    private User user1, user2;
    private ChatLogs chatLogs;
    private String databaseMessageUrl;



    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ch.epfl.sweng.radius", appContext.getPackageName());
    }

    @Before
    public void setUp() throws Exception {
        mlActivity = mblActivityTestRule.getActivity();
        user1 = new User();
        user2 = new User();
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add(user1.getUserID());
        userIds.add(user2.getUserID());
        chatLogs = new ChatLogs(userIds);
        databaseMessageUrl = "https://radius-1538126456577.firebaseio.com/messages/";
    }

    @Test
    public void setInfo(){
        //assertTrue(chatLogs);

    }

    @Test
    public void setUpUI(){
        RecyclerView recyclerView = mlActivity.findViewById(R.id.reyclerview_message_list);
        assertNotNull(recyclerView);

    }

    @Test
    public void setUpSendButton(){

        onView(withId(R.id.testMark)).perform(click());
    }

    @Test
    public void setUpListener(){

    }

    @Test
    public void addMessage(){

    }
}
