package ch.epfl.sweng.radius.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.User;
import android.support.v4.content.ContextCompat;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocationServices.class, Location.class,FusedLocationProviderClient.class, Toast.class, ContextCompat.class})
public class MapUtilityTest {

    private Location currentLocation = Mockito.mock(Location.class);
    ArgumentCaptor<OnCompleteListener> argument = ArgumentCaptor.forClass(OnCompleteListener.class);
    Task location = Mockito.mock(Task.class);
    Task ret = Mockito.mock(Task.class);
    Toast toast = Mockito.mock(Toast.class);
    private FusedLocationProviderClient mblFusedLocationClient = Mockito.mock(FusedLocationProviderClient.class);
    MapUtility mapUtility;
    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(ContextCompat.class);
        when(ContextCompat.checkSelfPermission(any(Context.class), anyString())).thenReturn(PackageManager.PERMISSION_GRANTED);
        Database.activateDebugMode();
        when(ret.isSuccessful()).thenReturn(true);
        when(ret.getResult()).thenReturn(new MLocation("coucou"));
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                OnCompleteListener answer = argument.capture();

                return answer;
            }

        }).when(location).addOnCompleteListener(argument.capture());
        when(mblFusedLocationClient.getLastLocation()).thenReturn(location);

        PowerMockito.mockStatic(LocationServices.class);
        when(LocationServices.getFusedLocationProviderClient(any(Activity.class)))
                .thenReturn(mblFusedLocationClient);

        PowerMockito.mockStatic(Toast.class);
        when(Toast.makeText(any(Context.class), any(CharSequence.class), anyInt())).thenReturn(toast);
        mapUtility = new MapUtility(2000);
        mapUtility.getLocationPermission(new Context() {
            @Override
            public AssetManager getAssets() {
                return null;
            }

            @Override
            public Resources getResources() {
                return null;
            }

            @Override
            public PackageManager getPackageManager() {
                return null;
            }

            @Override
            public ContentResolver getContentResolver() {
                return null;
            }

            @Override
            public Looper getMainLooper() {
                return null;
            }

            @Override
            public Context getApplicationContext() {
                return null;
            }

            @Override
            public void setTheme(int i) {

            }

            @Override
            public Resources.Theme getTheme() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public String getPackageName() {
                return null;
            }

            @Override
            public ApplicationInfo getApplicationInfo() {
                return null;
            }

            @Override
            public String getPackageResourcePath() {
                return null;
            }

            @Override
            public String getPackageCodePath() {
                return null;
            }

            @Override
            public SharedPreferences getSharedPreferences(String s, int i) {
                return null;
            }

            @Override
            public boolean moveSharedPreferencesFrom(Context context, String s) {
                return false;
            }

            @Override
            public boolean deleteSharedPreferences(String s) {
                return false;
            }

            @Override
            public FileInputStream openFileInput(String s) throws FileNotFoundException {
                return null;
            }

            @Override
            public FileOutputStream openFileOutput(String s, int i) throws FileNotFoundException {
                return null;
            }

            @Override
            public boolean deleteFile(String s) {
                return false;
            }

            @Override
            public File getFileStreamPath(String s) {
                return null;
            }

            @Override
            public File getDataDir() {
                return null;
            }

            @Override
            public File getFilesDir() {
                return null;
            }

            @Override
            public File getNoBackupFilesDir() {
                return null;
            }

            @Override
            public File getExternalFilesDir(String s) {
                return null;
            }

            @Override
            public File[] getExternalFilesDirs(String s) {
                return new File[0];
            }

            @Override
            public File getObbDir() {
                return null;
            }

            @Override
            public File[] getObbDirs() {
                return new File[0];
            }

            @Override
            public File getCacheDir() {
                return null;
            }

            @Override
            public File getCodeCacheDir() {
                return null;
            }

            @Override
            public File getExternalCacheDir() {
                return null;
            }

            @Override
            public File[] getExternalCacheDirs() {
                return new File[0];
            }

            @Override
            public File[] getExternalMediaDirs() {
                return new File[0];
            }

            @Override
            public String[] fileList() {
                return new String[0];
            }

            @Override
            public File getDir(String s, int i) {
                return null;
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String s, int i, SQLiteDatabase.CursorFactory cursorFactory) {
                return null;
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String s, int i, SQLiteDatabase.CursorFactory cursorFactory, @androidx.annotation.Nullable DatabaseErrorHandler databaseErrorHandler) {
                return null;
            }

            @Override
            public boolean moveDatabaseFrom(Context context, String s) {
                return false;
            }

            @Override
            public boolean deleteDatabase(String s) {
                return false;
            }

            @Override
            public File getDatabasePath(String s) {
                return null;
            }

            @Override
            public String[] databaseList() {
                return new String[0];
            }

            @Override
            public Drawable getWallpaper() {
                return null;
            }

            @Override
            public Drawable peekWallpaper() {
                return null;
            }

            @Override
            public int getWallpaperDesiredMinimumWidth() {
                return 0;
            }

            @Override
            public int getWallpaperDesiredMinimumHeight() {
                return 0;
            }

            @Override
            public void setWallpaper(Bitmap bitmap) throws IOException {

            }

            @Override
            public void setWallpaper(InputStream inputStream) throws IOException {

            }

            @Override
            public void clearWallpaper() throws IOException {

            }

            @Override
            public void startActivity(Intent intent) {

            }

            @Override
            public void startActivity(Intent intent,  Bundle bundle) {

            }

            @Override
            public void startActivities(Intent[] intents) {

            }

            @Override
            public void startActivities(Intent[] intents, Bundle bundle) {

            }

            @Override
            public void startIntentSender(IntentSender intentSender, @androidx.annotation.Nullable Intent intent, int i, int i1, int i2) throws IntentSender.SendIntentException {

            }

            @Override
            public void startIntentSender(IntentSender intentSender, @androidx.annotation.Nullable Intent intent, int i, int i1, int i2, @androidx.annotation.Nullable Bundle bundle) throws IntentSender.SendIntentException {

            }

            @Override
            public void sendBroadcast(Intent intent) {

            }

            @Override
            public void sendBroadcast(Intent intent, @androidx.annotation.Nullable String s) {

            }

            @Override
            public void sendOrderedBroadcast(Intent intent, @androidx.annotation.Nullable String s) {

            }

            @Override
            public void sendOrderedBroadcast(@androidx.annotation.NonNull Intent intent, @androidx.annotation.Nullable String s, @androidx.annotation.Nullable BroadcastReceiver broadcastReceiver, @androidx.annotation.Nullable Handler handler, int i, @androidx.annotation.Nullable String s1, @androidx.annotation.Nullable Bundle bundle) {

            }

            @Override
            public void sendBroadcastAsUser(Intent intent, UserHandle userHandle) {

            }

            @Override
            public void sendBroadcastAsUser(Intent intent, UserHandle userHandle, @androidx.annotation.Nullable String s) {

            }

            @Override
            public void sendOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, @androidx.annotation.Nullable String s, BroadcastReceiver broadcastReceiver, @androidx.annotation.Nullable Handler handler, int i, @androidx.annotation.Nullable String s1, @androidx.annotation.Nullable Bundle bundle) {

            }

            @Override
            public void sendStickyBroadcast(Intent intent) {

            }

            @Override
            public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver broadcastReceiver, @androidx.annotation.Nullable Handler handler, int i, @androidx.annotation.Nullable String s, @androidx.annotation.Nullable Bundle bundle) {

            }

            @Override
            public void removeStickyBroadcast(Intent intent) {

            }

            @Override
            public void sendStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {

            }

            @Override
            public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, BroadcastReceiver broadcastReceiver, @androidx.annotation.Nullable Handler handler, int i, @androidx.annotation.Nullable String s, @androidx.annotation.Nullable Bundle bundle) {

            }

            @Override
            public void removeStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {

            }

            @androidx.annotation.Nullable
            @Override
            public Intent registerReceiver(@androidx.annotation.Nullable BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
                return null;
            }

            @androidx.annotation.Nullable
            @Override
            public Intent registerReceiver(@androidx.annotation.Nullable BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, int i) {
                return null;
            }

            @androidx.annotation.Nullable
            @Override
            public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, @androidx.annotation.Nullable String s, @androidx.annotation.Nullable Handler handler) {
                return null;
            }

            @androidx.annotation.Nullable
            @Override
            public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, @androidx.annotation.Nullable String s, @androidx.annotation.Nullable Handler handler, int i) {
                return null;
            }

            @Override
            public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {

            }

            @androidx.annotation.Nullable
            @Override
            public ComponentName startService(Intent intent) {
                return null;
            }

            @androidx.annotation.Nullable
            @Override
            public ComponentName startForegroundService(Intent intent) {
                return null;
            }

            @Override
            public boolean stopService(Intent intent) {
                return false;
            }

            @Override
            public boolean bindService(Intent intent, @androidx.annotation.NonNull ServiceConnection serviceConnection, int i) {
                return false;
            }

            @Override
            public void unbindService(@androidx.annotation.NonNull ServiceConnection serviceConnection) {

            }

            @Override
            public boolean startInstrumentation(@androidx.annotation.NonNull ComponentName componentName, @androidx.annotation.Nullable String s, @androidx.annotation.Nullable Bundle bundle) {
                return false;
            }

            @Override
            public Object getSystemService(@androidx.annotation.NonNull String s) {
                return null;
            }

            @androidx.annotation.Nullable
            @Override
            public String getSystemServiceName(@androidx.annotation.NonNull Class<?> aClass) {
                return null;
            }

            @Override
            public int checkPermission(@androidx.annotation.NonNull String s, int i, int i1) {
                return 0;
            }

            @Override
            public int checkCallingPermission(@androidx.annotation.NonNull String s) {
                return 0;
            }

            @Override
            public int checkCallingOrSelfPermission(@androidx.annotation.NonNull String s) {
                return 0;
            }

            @Override
            public int checkSelfPermission(@androidx.annotation.NonNull String s) {
                return 0;
            }

            @Override
            public void enforcePermission(@androidx.annotation.NonNull String s, int i, int i1, @androidx.annotation.Nullable String s1) {

            }

            @Override
            public void enforceCallingPermission(@androidx.annotation.NonNull String s, @androidx.annotation.Nullable String s1) {

            }

            @Override
            public void enforceCallingOrSelfPermission(@androidx.annotation.NonNull String s, @androidx.annotation.Nullable String s1) {

            }

            @Override
            public void grantUriPermission(String s, Uri uri, int i) {

            }

            @Override
            public void revokeUriPermission(Uri uri, int i) {

            }

            @Override
            public void revokeUriPermission(String s, Uri uri, int i) {

            }

            @Override
            public int checkUriPermission(Uri uri, int i, int i1, int i2) {
                return 0;
            }

            @Override
            public int checkCallingUriPermission(Uri uri, int i) {
                return 0;
            }

            @Override
            public int checkCallingOrSelfUriPermission(Uri uri, int i) {
                return 0;
            }

            @Override
            public int checkUriPermission(@androidx.annotation.Nullable Uri uri, @androidx.annotation.Nullable String s, @androidx.annotation.Nullable String s1, int i, int i1, int i2) {
                return 0;
            }

            @Override
            public void enforceUriPermission(Uri uri, int i, int i1, int i2, String s) {

            }

            @Override
            public void enforceCallingUriPermission(Uri uri, int i, String s) {

            }

            @Override
            public void enforceCallingOrSelfUriPermission(Uri uri, int i, String s) {

            }

            @Override
            public void enforceUriPermission(@androidx.annotation.Nullable Uri uri, @androidx.annotation.Nullable String s, @androidx.annotation.Nullable String s1, int i, int i1, int i2, @androidx.annotation.Nullable String s2) {

            }

            @Override
            public Context createPackageContext(String s, int i) throws PackageManager.NameNotFoundException {
                return null;
            }

            @Override
            public Context createContextForSplit(String s) throws PackageManager.NameNotFoundException {
                return null;
            }

            @Override
            public Context createConfigurationContext(@androidx.annotation.NonNull Configuration configuration) {
                return null;
            }

            @Override
            public Context createDisplayContext(@androidx.annotation.NonNull Display display) {
                return null;
            }

            @Override
            public Context createDeviceProtectedStorageContext() {
                return null;
            }

            @Override
            public boolean isDeviceProtectedStorage() {
                return false;
            }
        }, new FragmentActivity());

    }

    @Test
    public void getDeviceLocation() {
        mapUtility.getDeviceLocation(new FragmentActivity());
    }
}