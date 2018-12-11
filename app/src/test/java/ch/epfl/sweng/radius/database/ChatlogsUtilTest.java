package ch.epfl.sweng.radius.database;

import android.content.Context;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ Database.class, FirebaseDatabase.class })
public class ChatlogsUtilTest {

    private ChatlogsUtil test;
    private Context mockedContext = Mockito.mock(Context.class);
    private ChatLogs testChat;
    @Before
    public void setUp() throws Exception {
        Database.DEBUG_MODE = true;

        Database.activateDebugMode();
        testChat = new ChatLogs("11");
        Database.getInstance().readObjOnce(testChat, Database.Tables.CHATLOGS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        testChat = (ChatLogs) value;
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
    }

    @Test
    @Ignore
    public void fetchGroupChatsAndListen() {

     //   test.fetchGroupChatsAndListen();
    }

    @Test(expected = NullPointerException.class)
    public void receiveMessage() {
        test = ChatlogsUtil.getInstance(mockedContext);

        test.upToDate = 0;

        test.receiveMessage(testChat, new Message("testUser1", "Heloo", new Date()),
                0);
    }

    @Test
    public void fetchSingleChatAndListen() {
        test = ChatlogsUtil.getInstance(mockedContext);

        MLocation newTopic = new MLocation("TOPIK"); newTopic.setLocationType(2);
        MLocation newGroup = new MLocation("GROUP"); newGroup.setLocationType(1);
        Database.getInstance().writeInstanceObj(newGroup, Database.Tables.LOCATIONS);
        Database.getInstance().writeInstanceObj(newTopic, Database.Tables.LOCATIONS);
        test.fetchSingleChatAndListen("TOPIK", 2);
        test.fetchSingleChatAndListen("GROUP", 1);
    }

    @Test
    public void fetchListChatAndListen() {
        test = ChatlogsUtil.getInstance(null);

        List<String> ids = new ArrayList<>();
        ids.add("testUser1");
    //    test.fetchListChatAndListen(ids, 0);
    }

    @Test
    public void testGetNewChat(){
        test = ChatlogsUtil.getInstance(mockedContext);

        String s = test.getNewChat("testUser4");
        test.getChat(s, 0);
        test.getChat("MyTopicTest", 2);
        test.getChat("groupTest", 1);
    }

    @Test
    public void onUserChange() {
        test = ChatlogsUtil.getInstance(mockedContext);

        ChatlogsUtil.getInstance();
        test.onUserChange("is");
    }
}