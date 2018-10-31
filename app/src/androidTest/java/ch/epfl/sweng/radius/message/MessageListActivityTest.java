package ch.epfl.sweng.radius.message;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.FrameLayout;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.AccountActivity;
import ch.epfl.sweng.radius.ProfileFragment;
import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.message.MessageListActivity;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.utils.ChatLogDbUtility;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MessageListActivityTest extends ActivityInstrumentationTestCase2<MessageListActivity> {

    @Rule
    public ActivityTestRule<MessageListActivity> mblActivityTestRule
            = new ActivityTestRule<MessageListActivity>(MessageListActivity.class);

    private final String CHAT_TEST_ID = "ChatTestId";
    private MessageListActivity mlActivity;
    private User user1, user2;
    private ChatLogs chatLogs;
    private String databaseMessageUrl;
    private Firebase chatReference;


    public MessageListActivityTest() {
        super(MessageListActivity.class);
    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ch.epfl.sweng.radius", appContext.getPackageName());
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        Intent intent = new Intent();
        mlActivity = mblActivityTestRule.launchActivity(intent);
        user1 = new User();
        user2 = new User();
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add(user1.getUserID());
        userIds.add(user2.getUserID());
        chatLogs = new ChatLogs(userIds);
        databaseMessageUrl = "https://radius-1538126456577.firebaseio.com/messages/";

        Firebase.setAndroidContext(mlActivity);
        chatReference = new Firebase(databaseMessageUrl + CHAT_TEST_ID);
    }

    @Test
    public void setUpUI() {
        assertNotNull(mlActivity.findViewById(R.id.reyclerview_message_list));
        assertNotNull(mlActivity.findViewById(R.id.layout_chatbox));
        assertNotNull(mlActivity.findViewById(R.id.edittext_chatbox));
        assertNotNull(mlActivity.findViewById(R.id.button_chatbox_send));

    }

    @Test
    public void setUpSendButton() {

        onView(withId(R.id.edittext_chatbox)).perform(typeText("Coucou"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.button_chatbox_send)).perform(click());

        assert (mlActivity.findViewById(R.id.edittext_chatbox).toString().isEmpty());
    }

    @Ignore
    @Test
    public void sendMessage() {
        //Methode a tester dans ChatLogDbUtility lorsque cette derniere sera disponible
    }

    @Ignore
    @Test
    public void receiveMessage() {
        //Methode a tester dans ChatLogDbUtility lorsque cette derniere sera disponible
    }
}
