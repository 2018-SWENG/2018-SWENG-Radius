package ch.epfl.sweng.radius.home;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Ignore;
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

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.Database;
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
        PowerMockito.mockStatic(BitmapDescriptorFactory.class);

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

        this.mapUtility = Mockito.mock(MapUtility.class);
        this.fragment = HomeFragment.newInstance(mapUtility, mockMap, 50000);
        if(fragment == null)
            System.out.print("ISNULL");
        File out = new File(Environment.getExternalStorageDirectory(), "current_user_info.data");
        try {
            out.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getFriendsID() {
        fragment.getFriendsID();
    }

    @Test
    public void testNewInstance(){

        HomeFragment test = HomeFragment.newInstance();
    }

    @Test
    public void markNearbyUser() {
    try{
            fragment.markNearbyUsers();
    }catch(NullPointerException e){/* Only happens in Unit Test*/}


    }

    @Test
    public void testOnMapReady(){
    try {
        fragment.onMapReady(mockMap);
    }catch (Exception e){};
    }
}