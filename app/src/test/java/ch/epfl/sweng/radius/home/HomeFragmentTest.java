package ch.epfl.sweng.radius.home;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.Location;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.utils.MapUtility;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GoogleMap.class, BitmapDescriptorFactory.class})
public class HomeFragmentTest {

    private final GoogleMap mockMap = PowerMockito.mock(GoogleMap.class);
    private BitmapDescriptorFactory moc = PowerMockito.mock(BitmapDescriptorFactory.class);
    private MapUtility mapUtility;
    private HomeFragment fragment;
    @Before
    public void setUp(){
        Database.activateDebugMode();
        this.mapUtility = Mockito.mock(MapUtility.class);
        this.fragment = HomeFragment.newInstance(mapUtility, mockMap, 50000);
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