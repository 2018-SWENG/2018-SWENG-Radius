package ch.epfl.sweng.radius;

import android.net.Uri;

import org.junit.Test;

import ch.epfl.sweng.radius.configuration.AppData;

public class AppDataTest {

    private AppData appData = AppData.INSTANCE;

    @Test
    public void testUsername() {
        appData.setUsername("testUsername");
        assert appData.getUsername().equals("testUsername") :
                "AppData methods for username access/modification are buggy.";
    }

    @Test
    public void testDevice() {
        appData.setDevice("testDevice");
        assert appData.getDevice().equals("testDevice") :
                "AppData methods for device access/modification are buggy.";
    }

    @Test
    public void testProfilePhotoUri() {
        appData.setProfilePhotoUri("testProfilePhotoUri");
        assert appData.getProfilePhotoUri().equals("testProfilePhotoUri") :
                "AppData methods for profilePhotoUri access/modification are buggy.";
    }

    @Test
    public void testIsIncognitoModeOn() {
        appData.setIncognitoMode(false);
        assert !appData.isIncognitoModeOn() :
                "AppData methods for isIncognitoModeOn access/modification are buggy.";
    }

    @Test
    public void testIsNightModeOn() {
        appData.setNightMode(true);
        assert appData.isNightModeOn() :
                "AppData methods for isNightModeOn access/modification are buggy.";
    }

    @Test
    public void testIsNotificationsOn() {
        appData.setNotifications(false);
        assert !appData.isNotificationsOn() :
                "AppData methods for isNotificationsOn access/modification are buggy.";
    }

}
