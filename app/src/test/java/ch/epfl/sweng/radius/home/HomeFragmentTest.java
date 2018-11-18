package ch.epfl.sweng.radius.home;

import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.FakeFirebaseUtility;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.utils.MapUtility;

import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GoogleMap.class, BitmapDescriptorFactory.class, FirebaseDatabase.class, ContextCompat.class})
public class HomeFragmentTest {

    private final GoogleMap mockMap = PowerMockito.mock(GoogleMap.class);
    private BitmapDescriptorFactory moc = PowerMockito.mock(BitmapDescriptorFactory.class);
    private MapUtility mapUtility;
    private HomeFragment fragment;
    DatabaseReference mockedDb   = Mockito.mock(DatabaseReference.class);
    FirebaseDatabase mockedFb   = Mockito.mock(FirebaseDatabase.class);
    String curRef;
    @Before
    public void setUp(){
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

        this.mapUtility = Mockito.mock(MapUtility.class);
        this.fragment = HomeFragment.newInstance(mapUtility, mockMap, 50000);
        if(fragment == null)
            System.out.print("ISNULL");
    }

    @Test
    public void getUsersInRadius() {
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                return null;
            }

        }).when(mapUtility).fetchUsersInRadius(any(Integer.class));
        doAnswer(new Answer<List<MLocation>>() {
            @Override
            public List<MLocation> answer(InvocationOnMock invocation) throws Throwable {
                MLocation loc = new MLocation();
                ArrayList<MLocation> ret = new ArrayList<>();
                ret.add(loc);
                return ret;
            }

        }).when(mapUtility).getOtherLocations();

        fragment.setMyPos(new MLocation());

        fragment.getUsersInRadius();

    }

    @Test
    public void markNearbyUsers() {
        doAnswer(new Answer<List<MLocation>>() {
            @Override
            public List<MLocation> answer(InvocationOnMock invocation) throws Throwable {
                MLocation loc = new MLocation();
                ArrayList<MLocation> ret = new ArrayList<>();
                ret.add(loc);
                return ret;
            }

        }).when(mapUtility).getOtherLocations();
        doAnswer(new Answer<List<MLocation>>() {
            @Override
            public List<MLocation> answer(InvocationOnMock invocation) throws Throwable {
                MLocation loc = new MLocation();
                ArrayList<MLocation> ret = new ArrayList<>();
                ret.add(loc);
                return ret;
            }

        }).when(mapUtility).getOtherPos();

        fragment.setMyPos(new MLocation());

        fragment.markNearbyUsers();
    }

    @Test
    public void getFriendsID() {
    }

    @Test
    public void markNearbyUser() {
    }
}