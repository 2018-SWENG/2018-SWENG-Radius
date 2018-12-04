package ch.epfl.sweng.radius.storage;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.storage.Storage.StorageFile;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseStorage.class, MimeTypeMap.class })
public class FirebaseStorageUtilityTest {

    StorageReference mockedRef = Mockito.mock(StorageReference.class);
    FirebaseStorage mockedStorage = Mockito.mock(FirebaseStorage.class);
    UploadTask mockedTask = Mockito.mock(UploadTask.class);
    Task<android.net.Uri> mTask = Mockito.mock(Task.class);
    Activity mockedActivity = Mockito.mock(Activity.class);
    MimeTypeMap mockedMap = Mockito.mock(MimeTypeMap.class);
    Uri mockedUri = Mockito.mock(Uri.class);
    ContentResolver mockedCR = Mockito.mock(ContentResolver.class);

    FirebaseStorageUtility test;
    @Before
    public void setUp() throws Exception {

        Database.activateDebugMode();
        PowerMockito.mockStatic(MimeTypeMap.class);
        when(MimeTypeMap.getSingleton()).thenReturn(mockedMap);
        when(mockedMap.getExtensionFromMimeType(anyString())).thenReturn("test");
        when(mockedActivity.getContentResolver()).thenReturn(mockedCR);

        PowerMockito.mockStatic(FirebaseStorage.class);
        when(FirebaseStorage.getInstance()).thenReturn(mockedStorage);

        when(mockedStorage.getReference(anyString())).thenReturn(mockedRef);
        when(mockedRef.child(anyString())).thenReturn(mockedRef);

        when(mockedRef.putFile(any(Uri.class))).thenReturn(mockedTask);
        when(mockedRef.getDownloadUrl()).thenReturn(mTask);


        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                OnSuccessListener onSuccessListener = (OnSuccessListener) invocation.getArguments()[0];

                UploadTask.TaskSnapshot mockedSnap = Mockito.mock((UploadTask.TaskSnapshot.class));

                onSuccessListener.onSuccess(mockedSnap);
                return null;
            }

        }).when(mockedTask).addOnSuccessListener(any(OnSuccessListener.class));

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                OnSuccessListener onSuccessListener = (OnSuccessListener) invocation.getArguments()[0];

                Uri uri = Mockito.mock(Uri.class);
                when(uri.toString()).thenReturn("Testingg");
                onSuccessListener.onSuccess(uri);
                return null;
            }

        }).when(mTask).addOnSuccessListener(any(OnSuccessListener.class));


        StorageFile stf = StorageFile.PROFILE_PICTURES;
        stf.toString();

        test = (FirebaseStorageUtility) Storage.getInstance();
    }

    @Test
    public void uploadFile() {
        test.uploadFile(mockedUri, mockedActivity);
    }
}