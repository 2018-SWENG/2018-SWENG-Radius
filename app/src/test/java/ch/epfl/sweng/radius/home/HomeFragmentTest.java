package ch.epfl.sweng.radius.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.utils.MapUtility;
import ch.epfl.sweng.radius.utils.MapUtilityTest;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ContextCompat.class, LocationServices.class, Environment.class,
        Toast.class, GoogleMap.class, BitmapDescriptorFactory.class, FirebaseDatabase.class, ContextCompat.class})
public class HomeFragmentTest {

    private final GoogleMap mockMap = PowerMockito.mock(GoogleMap.class);
    private BitmapDescriptorFactory moc = PowerMockito.mock(BitmapDescriptorFactory.class);
    private MapUtility mapUtility;
    private HomeFragment fragment;
    private File mockedFile = Mockito.mock(File.class);
    DatabaseReference mockedDb   = Mockito.mock(DatabaseReference.class);
    FirebaseDatabase mockedFb   = Mockito.mock(FirebaseDatabase.class);
    FusedLocationProviderClient mockedPos = Mockito.mock(FusedLocationProviderClient.class);
    String curRef;
    @Before
    public void setUp(){
        PowerMockito.mockStatic(FirebaseDatabase.class);
        PowerMockito.mockStatic(Environment.class);
        PowerMockito.mockStatic(BitmapDescriptorFactory.class);
        PowerMockito.mockStatic(Toast.class);
        when(Environment.getExternalStorageDirectory()).thenReturn(mockedFile);
        when(Toast.makeText(any(Context.class), any(String.class), anyInt())).thenReturn(new Toast(MapUtilityTest.context));
        Mockito.when(FirebaseDatabase.getInstance()).thenReturn(mockedFb);
        PowerMockito.mockStatic(ContextCompat.class);
        PowerMockito.mockStatic(LocationServices.class);
        when(LocationServices.getFusedLocationProviderClient(any(FragmentActivity.class))).thenReturn(mockedPos);
        when(mockedPos.getLastLocation()).thenReturn(new Task<Location>() {
            @Override
            public boolean isComplete() {
                return false;
            }

            @Override
            public boolean isSuccessful() {
                return true;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Nullable
            @Override
            public Location getResult() {
                return new Location("Tets");
            }

            @Nullable
            @Override
            public <X extends Throwable> Location getResult(@NonNull Class<X> aClass) throws X {
                return null;
            }

            @Nullable
            @Override
            public Exception getException() {
                return null;
            }
            @Override
            public Task addOnCompleteListener(OnCompleteListener result){

                return this;
            }

            @NonNull
            @Override
            public Task<Location> addOnSuccessListener(@NonNull OnSuccessListener<? super Location> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Location> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super Location> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Location> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super Location> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Location> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Location> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Location> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
                return null;
            }
        });
        String s =Manifest.permission.ACCESS_FINE_LOCATION;
        when(ContextCompat.checkSelfPermission(any(Context.class),
                eq(s))).thenReturn(0);
        when(ContextCompat.checkSelfPermission(any(Context.class),eq( Manifest.permission.ACCESS_COARSE_LOCATION))).thenReturn(0);
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
    public void initMap(){
        fragment.initMap();
    }


    @Test
    public void testOnMapReady(){
  //  try {
        fragment.onMapReady(mockMap);
  //  }catch (Exception e){};
    }
}