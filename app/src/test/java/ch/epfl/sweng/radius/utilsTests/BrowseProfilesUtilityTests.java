package ch.epfl.sweng.radius.utilsTests;

import android.support.v4.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.FakeFirebaseUtility;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.BrowseProfilesUtility;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;


@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseDatabase.class, ContextCompat.class})
public class BrowseProfilesUtilityTests {

    private BrowseProfilesUtility profileListener;
    private String profileOwner;
    DatabaseReference mockedDb   = Mockito.mock(DatabaseReference.class);
    FirebaseDatabase mockedFb   = Mockito.mock(FirebaseDatabase.class);
    String curRef;
    @Before
    public void setUp() {
        PowerMockito.mockStatic(FirebaseDatabase.class);

        Mockito.when(FirebaseDatabase.getInstance()).thenReturn(mockedFb);
        Mockito.when(mockedFb.getReference(any(String.class))).thenReturn(mockedDb);

        Mockito.when(mockedDb.child((String) Matchers.argThat(new ArgumentMatcher(){

            // Update current and print to console path to console
            @Override
            public boolean matches(Object argument) {
                curRef = (String) argument;
                return true;
            }

        }))).thenReturn(mockedDb);
        Database.activateDebugMode();
        ((FakeFirebaseUtility) Database.getInstance()).fillDatabase();

        profileOwner = "John Doe";
        profileListener = new BrowseProfilesUtility(profileOwner);
    }

    @Test
    public void testReportUser() {
        profileListener.reportUser("Language");
        profileListener.reportUser("Spam");
    }

    @Test
    public void testGetProfileOwner() {
        String profileOwnerName;
        profileOwnerName = profileListener.getProfileOwner();
        assertTrue(profileOwnerName.equals(profileOwner));
    }

    @Test
    public void testUnblockUser() {
        User currentUser = UserInfo.getInstance().getCurrentUser();
        currentUser.getBlockedUsers().add(profileOwner);
        profileListener.unblockUser();
    }

    @After
    public void tearDown() {
        profileListener = null;
        profileOwner = null;
    }

}
