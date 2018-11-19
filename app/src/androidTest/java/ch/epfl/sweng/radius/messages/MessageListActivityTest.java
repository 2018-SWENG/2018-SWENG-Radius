package ch.epfl.sweng.radius.messages;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.firebase.client.Firebase;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.FakeFirebaseUtility;
import ch.epfl.sweng.radius.database.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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
        Database.activateDebugMode();
        Intent intent = new Intent();
        intent.putExtra("chatId", "0");
        intent.putExtra("otherId", "userTest1");
        mlActivity = mblActivityTestRule.launchActivity(intent);
        user1 = new User();
        user2 = new User();
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add(user1.getID());
        userIds.add(user2.getID());
        chatLogs = new ChatLogs(userIds);
        databaseMessageUrl = "https://radius-1538126456577.firebaseio.com/messages/";

        Firebase.setAndroidContext(mlActivity);
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

        MessagesFragment.newInstance("A", "B");

    }

    @Test
    public void testSetEnabledTrue() {
        mlActivity.setEnabled(true);
        onView(withId(R.id.edittext_chatbox)).perform(typeText("Test"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.button_chatbox_send)).perform(click());

        assert (mlActivity.findViewById(R.id.edittext_chatbox).toString().isEmpty());
    }

    @Test
    public void testSetEnabledFalse() {
        mlActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mlActivity.setEnabled(false);
            }
        });

        onView(withId(R.id.edittext_chatbox)).perform(typeText("Test"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.button_chatbox_send)).perform(click());
        assertEquals("You can't text this user.",((EditText) mlActivity.findViewById(R.id.edittext_chatbox)).getText().toString());
    }

    @Ignore
    @Test
    public void sendMessage() {
    }

    @Ignore
    @Test
    public void receiveMessage() {
        //Methode a tester dans ChatLogDbUtility lorsque cette derniere sera disponible
    }
}
