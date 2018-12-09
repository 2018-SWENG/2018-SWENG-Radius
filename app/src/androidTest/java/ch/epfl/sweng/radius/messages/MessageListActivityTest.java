package ch.epfl.sweng.radius.messages;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.ChatlogsUtil;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;

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
    public final GrantPermissionRule mPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);

    /* TODO CLEAN FAKE DB AND START FROM ACCOUNT ACTIVITY OR SOMETHING*/
    @Rule
    public ActivityTestRule<MessageListActivity> mblActivityTestRule
            = new ActivityTestRule<MessageListActivity>(MessageListActivity.class, false, true){
        @Override
        protected Intent getActivityIntent() {

            user1 = new User("testUser1");
            user2 = new User("testUser2");
            ArrayList<String> userIds = new ArrayList<>();
            userIds.add(user1.getID());
            userIds.add(user2.getID());
            chatLogs = new ChatLogs(userIds);
            chatLogs.addMessage(new Message("testUser1", "22", new Date()));
            chatLogs.addMessage(new Message("testUser2", "22", new Date()));
            Database.getInstance().writeInstanceObj(chatLogs, Database.Tables.CHATLOGS);
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, MessageListActivity.class);
            result.putExtra("chatId", chatLogs.getChatLogsId());
            result.putExtra("otherId", user2.getID());
            result.putExtra("locType", 0);

            result.setAction(chatLogs.getID());
            return result;
        }

    };

    private MessageListActivity mlActivity = mblActivityTestRule.getActivity();
    private User user1, user2;
    private ChatLogs chatLogs;
    private String databaseMessageUrl;
    private Firebase chatReference;
    private boolean vd = Database.activateDebugMode();


    public MessageListActivityTest() {
        super(MessageListActivity.class);
    }




    @Before
    public void setUp() throws Exception {
        super.setUp();
        Database.activateDebugMode();
        UserInfo.getInstance().fetchDataFromDB();
        OthersInfo.getInstance().fetchUsersInMyRadius();
        ChatlogsUtil.getInstance(mlActivity);
        UserInfo.getInstance().getCurrentUser();
        user2 = new User("testUser2");
        Context targetContext = InstrumentationRegistry.getInstrumentation()
                .getTargetContext();
        Intent intent = new Intent(targetContext, MessageListActivity.class);
        intent.putExtra("chatId", "10");
        intent.putExtra("otherId", user2.getID());
        intent.putExtra("locType", 0);
        intent.setAction("10");
        mlActivity = mblActivityTestRule.getActivity();
        mblActivityTestRule.launchActivity(intent);
        Thread.sleep(5000);

    }

    @Test
    public void setUpUI() {
        try {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    assertNotNull(mlActivity.findViewById(R.id.reyclerview_message_list));
                    assertNotNull(mlActivity.findViewById(R.id.layout_chatbox));
                    assertNotNull(mlActivity.findViewById(R.id.edittext_chatbox));
                    assertNotNull(mlActivity.findViewById(R.id.button_chatbox_send));
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ch.epfl.sweng.radius", appContext.getPackageName());
    }


    @Test
    public void setUpSendButton() {
        try {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
        onView(withId(R.id.edittext_chatbox)).perform(typeText("Coucou"));
        Espresso.closeSoftKeyboard();
        mlActivity.usersInRadius();
        onView(withId(R.id.button_chatbox_send)).perform(click());

        assert (mlActivity.findViewById(R.id.edittext_chatbox).toString().isEmpty());
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
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
        Fragment frag = MessagesFragment.newInstance("A", "B");

        mlActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mlActivity.setEnabled(false);
            }
        });

        onView(withId(R.id.edittext_chatbox)).perform(typeText("Test"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.button_chatbox_send)).perform(click());
        assertEquals("You can't text this user.", ((EditText) mlActivity.findViewById(R.id.edittext_chatbox)).getText().toString());
    }

    @Test
    public void sendMessage() {
        mlActivity.showNotification("Coucou", "Coucou", "MyTestTopic");
    }

    @Ignore
    @Test
    public void receiveMessage() {
        //Methode a tester dans ChatLogDbUtility lorsque cette derniere sera disponible
    }

}