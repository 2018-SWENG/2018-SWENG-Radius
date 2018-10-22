package ch.epfl.sweng.radius.configuration;

import android.net.Uri;

/**
 *  AppData is an enum that obeys the Singleton design pattern,
 *  it holds the essential application data that should be unique.
 */
public enum AppData {

    INSTANCE;

    // Properties
    String username;
    String device;
    String profilePhotoUri;
    boolean isIncognitoModeOn;
    boolean isNightModeOn;
    boolean isNotificationsOn;
    // ... And other fields that will be used in the initial configuration

    // Accessor and modifiers for the properties

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getProfilePhotoUri() {
        return profilePhotoUri;
    }

    public void setProfilePhotoUri(String profilePhotoUri) {
        this.profilePhotoUri = profilePhotoUri;
    }

    public Boolean isIncognitoModeOn() {
        return isIncognitoModeOn;
    }

    public void setIncognitoMode(boolean isIncognitoModeOn) {
        this.isIncognitoModeOn = isIncognitoModeOn;
    }

    public Boolean isNightModeOn() {
        return isNightModeOn;
    }

    public void setNightMode(boolean isNightModeOn) {
        this.isNightModeOn = isNightModeOn;
    }

    public Boolean isNotificationsOn() {
        return isNotificationsOn;
    }

    public void setNotifications(boolean isNotificationsOn) {
        this.isNotificationsOn = isNotificationsOn;
    }

    //... And other methods that will be needed to access/modify application data

}
