package ch.epfl.sweng.radius.utils;

import android.support.annotation.NonNull;
import android.test.AndroidTestCase;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseApiNotAvailableException;

//import org.junit.Test;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;

public class FirebaseUtilityTest extends AndroidTestCase {
    private static final String TAG = "Firebase";


//    private static Logger logger = Logger.getLogger(FirebaseUtilityTest.class);

    private CountDownLatch authSignal = null;
    private FirebaseAuth auth;

    private User user;
    private Message msg;
    private ChatLogs logs;

    private FirebaseUtility user_fbutil;
  //  private FirebaseUtility logs_fbutil;
  //  private FirebaseUtility msg_fbutil;


    @Override
    public void setUp() throws InterruptedException {
        authSignal = new CountDownLatch(1);

        user = new User("usertTest00");


        user_fbutil = new FirebaseUtility(user);

        // logs_fbutil = new FirebaseUtility(logs);
        // msg_fbutil = new FirebaseUtility(msg);


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

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        if(auth != null) {

            auth.signOut();
            auth = null;
        }
    }

    @Test
    public void isNew() {

    }

    @Test
    public void testListenUser() throws InterruptedException {

        try {
            user_fbutil.listenUser();

            user = user_fbutil.getUser();
            Log.e(TAG, user.getStatus());

            if ((!"Being tested on".equals(user.getStatus()))) throw new AssertionError();
        }
        catch(Exception e){

        }
    }

    @Test
    public void testWriteUser() {

        user.setStatus("Testing writing instance User to DB");

        try {
            user_fbutil.writeUser();

            user_fbutil.listenUser();

            user = user_fbutil.getUser();

            if((!"Testing writing instance User to DB".equals(user.getStatus()))) throw new AssertionError();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteUser1() {

        User new_user = new User("testUser01");

        new_user.setStatus("Testing writing other user to DB");
        try {
            user_fbutil.writeUser(new_user);

            user_fbutil.setUser(new_user);
            user_fbutil.listenUser();

            new_user = user_fbutil.getUser();

            if((!"Testing writing other user to DB".equals(new_user.getStatus()))) throw new AssertionError();

            // Remove test entry and re-set variable
            user_fbutil.removeUser();

            user_fbutil.setUser(user);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void listenMessage() {
    }

    @Test
    public void writeMessage() {
    }

    @Test
    public void writeMessage1() {
    }

    @Test
    public void listenChatLogs() {
    }

    @Test
    public void writeChatLogs() {
    }

    @Test
    public void writeChatLogs1() {
    }

    @Test
    public void getUser() {
    }

    @Test
    public void setUser() {
    }

}