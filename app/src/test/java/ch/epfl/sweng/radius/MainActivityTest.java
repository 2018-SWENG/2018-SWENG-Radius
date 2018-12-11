package ch.epfl.sweng.radius;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseAuth.class, Auth.class, GoogleAuthProvider.class})
public class MainActivityTest {

    private AuthCredential mockedGoogle = Mockito.mock(AuthCredential.class);
    private Auth mock = Mockito.mock(Auth.class);
    private Intent mockedIntent = Mockito.mock(Intent.class);
    private MainActivity activity;
    private GoogleSignInResult mockedRes = Mockito.mock(GoogleSignInResult.class);
    private GoogleSignInAccount mockedAccount = Mockito.mock(GoogleSignInAccount.class);
    private FirebaseAuth mockedAuth = Mockito.mock(FirebaseAuth.class);
    ArgumentCaptor< OnCompleteListener> onCompleteArg = ArgumentCaptor.forClass(OnCompleteListener.class);
    ArgumentCaptor<Activity> aArg = ArgumentCaptor.forClass(Activity.class);
    Task<AuthResult> authRes = Mockito.mock(Task.class);
    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockedAuth);
        PowerMockito.mockStatic(Auth.class);
        PowerMockito.mockStatic(GoogleAuthProvider.class);
   //     when(Auth.GoogleSignInApi.getSignInResultFromIntent(mockedIntent)).thenReturn(mockedRes);
        when(mockedRes.isSuccess()).thenReturn(true);
        when(mockedAccount.getIdToken()).thenReturn("t");
        when(GoogleAuthProvider.getCredential(any(String.class), any(String.class)))
                .thenReturn(mockedGoogle);
        when(mockedIntent.hasExtra(any(String.class))).thenReturn(false);
        when(mockedAuth.signInWithCredential(any(AuthCredential.class))).thenReturn(authRes);
      when(authRes.addOnCompleteListener(onCompleteArg.capture())).thenReturn(authRes);

        when(authRes.isSuccessful()).thenReturn(true);
        activity = new MainActivity();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Ignore
    public void onCreate() {
    }

    @Test
    @Ignore
    public void onStart() {
    }

    @Test
    public void onActivityResult() {
        activity.onActivityResult(1, 22, null);

    }

    @Test
    public void firebaseAuthWithGoogle(){
        activity.firebaseAuthWithGoogle(mockedAccount);
        verify(authRes).addOnCompleteListener(aArg.capture(), onCompleteArg.capture());
        OnCompleteListener res = onCompleteArg.getValue();
        res.onComplete(authRes);

    }


}