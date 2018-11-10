package ch.epfl.sweng.radius.utils;

import android.test.AndroidTestCase;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.Ignore;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.concurrent.CountDownLatch;

import ch.epfl.sweng.radius.database.FirebaseUtility;
import ch.epfl.sweng.radius.database.User;

//import org.junit.Test;
@Ignore
//@RunWith(PowerMockRunner.class)
//@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest(FirebaseUtility.class)
public class FirebaseUtilityTest extends AndroidTestCase {
    private static final String TAG = "Firebase";

    private CountDownLatch authSignal = null;
    private FirebaseAuth auth;

    final static String mockDBPath    = "./src/androidTest/java/ch/epfl/sweng/radius/utils/db.json";

    private User user;

    private FirebaseUtility fbutil;

/*
    @Before
    public void setUp() {

        user = new User("userTest00");
        user.addChat("userTest00", "Hello you");
        user.addFriendRequest("userTest01");

        String otherID = user.getConvFromUser("userTest01");

        //fbutil = Mockito.mock(FirebaseUtility.class);
        fbutil = new FirebaseUtility(user, "users");


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

    @Test
    public void testConstructors(){


    }


    @Test(expected = FirebaseApiNotAvailableException.class)
    public void testListenUser() {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test(expected = FirebaseApiNotAvailableException.class)
    public void testIsNew() {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


*/




}
