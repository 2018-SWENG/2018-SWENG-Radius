package ch.epfl.sweng.radius;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.ArrayList;
import java.util.Map;

import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.FirebaseUtility;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.User;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseAuth.class, FirebaseDatabase.class })
public class PreferencesActivityTest {

    private PreferencesActivity test;
    FirebaseAuth        mockedAuth = Mockito.mock(FirebaseAuth.class);
    FirebaseUser mockedUser = Mockito.mock(FirebaseUser.class);
    Task mockedTask = Mockito.mock(Task.class);


    @Before
    public void setUp() throws Exception {
        Database.activateDebugMode();
        test = new PreferencesActivity();
        }

    @Test
    public void deleteUser() {
        PreferencesActivity.deleteUser();
        restoreCurrentUser();
    }

    @Test
    public void setupPositiveButton()
    {
        PowerMockito.mockStatic(FirebaseAuth.class);
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockedAuth);
        when(mockedAuth.getCurrentUser()).thenReturn(mockedUser);
        when(mockedUser.getUid()).thenReturn("Coucou");
        when(mockedTask.isSuccessful()).thenReturn(true);
        when(mockedUser.delete()).thenReturn(mockedTask);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                OnCompleteListener onCompleteListener = (OnCompleteListener) invocation.getArguments()[0];

                onCompleteListener.onComplete(mockedTask);
                return null;
            }

        }).when(mockedTask).addOnCompleteListener(any(OnCompleteListener.class));
    }

    private void restoreCurrentUser() {

        User currentUSer = new User("testUser1");
        currentUSer.addChat("testUser2", "10");
        currentUSer.addChat("testUser3", "11");
        currentUSer.addChat("testUser5", "12");
        currentUSer.addFriendRequest(new User("testUser5"));
        ArrayList<String> blockedUser = new ArrayList<>();
        blockedUser.add("testUser3");currentUSer.setBlockedUsers(blockedUser);

        currentUSer.addFriendRequest(new User("testUser3"));

        Database.getInstance().writeInstanceObj(currentUSer, Database.Tables.USERS);

        MLocation currentLoc = new MLocation("testUser1");
        currentLoc.setUrlProfilePhoto("./app/src/androidTest/java/ch/epfl/sweng/radius/utils/default.png");
        currentLoc.setTitle("testUser1");
        currentLoc.setRadius(30000); currentLoc.setMessage("Being tested on");
        currentLoc.setInterests("Tests, mostly");

        Database.getInstance().writeInstanceObj(currentLoc, Database.Tables.LOCATIONS);

    }

}