package ch.epfl.sweng.radius.utils;

import android.support.annotation.NonNull;
import android.test.AndroidTestCase;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;
@Ignore
public class ChatLogDbUtilityTest {

    private static ChatLogs localInstance;
    private static ChatLogDbUtility cLUtil;
    private  FirebaseAuth auth;
    private  CountDownLatch authSignal;

    @BeforeClass
    public static void setUpClass() throws Exception {

        localInstance = new ChatLogs("chatTest00");

        cLUtil = new ChatLogDbUtility(localInstance);
    }

    @Before
    public void setUp() throws InterruptedException {
        authSignal = new CountDownLatch(1);


        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            auth.signInWithEmailAndPassword("urbi@orbi.it", "12345678").addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {

                            final AuthResult result = task.getResult();
                            final FirebaseUser user = result.getUser();
                            authSignal.countDown();
                        }
                    });
        } else {
            authSignal.countDown();
        }
        authSignal.await(100, TimeUnit.SECONDS);

    }

    @After
    public void tearDown() throws Exception {
       ChatLogs n = new ChatLogs("chatTest00");
       cLUtil.writeChatLogs(n);
        if(auth != null) {

            auth.signOut();
            auth = null;
        }

    }

    @Test
    public void addMessage() throws InterruptedException {
        Date date = new Date();

        cLUtil.addMessage(new Message("userTest00", "coucou", date));
        cLUtil.addMessage(new Message("userTest00", "coucou2", date));

        localInstance = cLUtil.readChatLogs();

        Log.e("Chatlog", String.valueOf(localInstance.getMessages().size()));

        if (localInstance.getMessages().size() != 5) throw new AssertionError();

        }

    @Test
    public void deleteMessage() throws InterruptedException {


        cLUtil.deleteMessage(0);
        localInstance = cLUtil.readChatLogs();

        if (localInstance.getMessages().size() != 4) throw new AssertionError();
    }

    @Test
    public void getMessage() throws InterruptedException {
        Date date = new Date();

        cLUtil.addMessage(new Message("userTest00", "coucou", date));

        Message msg = cLUtil.getMessage(localInstance.getMessages().size()-1);
        Log.e("Chatlog", msg.getContentMessage());
    }

    @Test
    public void getChatLogs() throws InterruptedException {

        ChatLogs otherChat = new ChatLogs("chatTest01");

        cLUtil.writeChatLogs(otherChat);

        ChatLogs ret = cLUtil.getChatLogs("chatTest01");
        if (ret == null) throw new AssertionError();
    }

    @Test
    public void testOtherChatLogsMethods(){



        ChatLogs test = new ChatLogs(new ArrayList<String>());
        if (test == null) throw new AssertionError();


        cLUtil.addMessage(new Message("userTest00", "coucou", new Date()));
        cLUtil.addMessage(new Message("userTest00", "coucou2", new Date()));
        cLUtil.addMessage(new Message("userTest00", "coucou3", new Date()));

        List<Message> msgs = localInstance.getLastNMessages(2);
        if (msgs == null || msgs.size() != 3) throw new AssertionError();

        test.addMembersId("userTest00");
        if (test.getMembersId().size() != 1 || !test.getMembersId().get(0).equals("userTest00"))
            throw new AssertionError();

        test.addMessage(new Message("userTest00", "Coucou", new Date()));
        if (test.getMessages().size() != 1) throw new AssertionError();

        test.deleteMessage(0);
        if (test.getMessages().size() != 0) throw new AssertionError();

        ChatLogs n = new ChatLogs("chatTest00");
        cLUtil.writeChatLogs(n);

    }


}