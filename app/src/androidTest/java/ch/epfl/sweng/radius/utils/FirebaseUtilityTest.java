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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.DatabaseObject;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;

import static java.lang.Thread.sleep;

public class FirebaseUtilityTest extends AndroidTestCase {
    private static final String TAG = "Firebase";

    private CountDownLatch authSignal = null;
    private FirebaseAuth auth;

    private User user;

    private FirebaseUtility fbutil;

    @Before
    public void setUp() throws InterruptedException {
        authSignal = new CountDownLatch(1);

        user = new User("userTest00");


        fbutil = new FirebaseUtility(user, "users");

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
        user = new User("userTest00");
        user.setStatus("Being tested on");
        fbutil.setInstance(user);

        fbutil.writeInstanceObj();
    }


    @Test(expected = FirebaseApiNotAvailableException.class)
    public void testListenUser() throws InterruptedException {

        try {
            fbutil.readObj();

            user = (User) fbutil.getInstance();
            Log.e(TAG, user.getStatus());

            if ((!"Being tested on".equals(user.getStatus()))) throw new AssertionError();
        }
        catch(Exception e){

        }
    }

    @Test(expected = FirebaseApiNotAvailableException.class)
    public void testWriteUser() {

        user.setStatus("Testing writing instance User to DB");

        try {
            fbutil.writeInstanceObj();

            fbutil.readObj();

            user = (User) fbutil.getInstance();

            if((!"Testing writing instance User to DB".equals(user.getStatus()))) throw new AssertionError();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test(expected = FirebaseApiNotAvailableException.class)
    public void testIsNew() throws FirebaseApiNotAvailableException {

        if(fbutil.isNew()) throw new AssertionError();

        User userbis = new User("userTest01");

        fbutil.setInstance(userbis);

        if(!fbutil.isNew()) throw new AssertionError();


    }

    @Test(expected = FirebaseApiNotAvailableException.class)
    public void testReadOtherObject() throws InterruptedException {

        User new_user = new User("testUser01");

        new_user = (User) fbutil.readOtherObject(new_user.getID());

        if((!"New User testUser01".equals(new_user.getNickname()))) throw new AssertionError();

    }

    @Test(expected = FirebaseApiNotAvailableException.class)
    public void testListenInstanceObject() throws InterruptedException {
            fbutil.listenInstanceObject();

            FirebaseUtility otherfb = new FirebaseUtility(user, "users");

            user.setStatus("Trying to trigger listener.");

            otherfb.setInstance(user);

            otherfb.writeInstanceObj();

            // To ensure data has been read
            sleep(1000);

            user = (User) fbutil.getInstance();

            System.out.println(user.getStatus());

        Log.w("FirebaseDebug", user.getStatus());

    }

    @Test(expected = FirebaseApiNotAvailableException.class)
    public void testWriteUser1() {

        User new_user = new User("testUser01");

        new_user.setStatus("Testing writing other user to DB");
        try {
            fbutil.writeOtherObj(new_user);

            fbutil.setInstance(new_user);
            fbutil.readObj();

            new_user = (User) fbutil.getInstance();

            if((!"Testing writing other user to DB".equals(new_user.getStatus()))) throw new AssertionError();

            // Remove test entry and re-set variable
         //   fbutil.removeUser();

         //   fbutil.setUser(user);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }






}