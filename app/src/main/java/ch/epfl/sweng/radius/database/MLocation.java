/**
 * This file contains the MLocation class, which is one of the
 * most important database objects in the database.
 * @author RADIUS
 * @version 1.0
 */

package ch.epfl.sweng.radius.database;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * One of the most important database objects in the database,
 * it can be explained as an extented location object with a lot
 * of properties that are required because user specifications.
 */
public class MLocation implements DatabaseObject, Serializable {

    // Constants
    private final static double DEFAULT_LOCATION_RADIUS = 2000;
    private final static double DEFAULT_LONGITUDE = 6.5681216000000004;
    private final static double DEFAULT_lATITUDE = 46.5160698;
    private final static String DEFAULT_URL_PROFIL_PIC = "https://firebasestorage.googleapis.com/v0/b/radius-1538126456577.appspot.com/o/profilePictures%2Fdefault.png?alt=media&token=ccd39de0-9921-487b-90e7-3501262d7835";

    // Properties
    private String userID = "";
    private String title;
    private String message;
    private double longitude;
    private double latitude;
    private boolean visible; // added for invisibility feature
    private boolean deleted;
    private String urlProfilePhoto;
    private int locationType; // 0: user location, 1: group location, 2: topic location
    private double radius; // Use it only if the mLocation is a group.
    private String spokenLanguages;
    private String interests;
    private ArrayList<String> languageList = new ArrayList<>();
    private String ownerId; // for topics, no significance for locations and groups (so default is "")

    /**
     * The default constructor of MLocation.
     */
    public MLocation() { this(""); }

    /**
     * Constructor for MLocation, which takes into consideration the specified userID
     * required to know which user the MLocation belongs to.
     * @param userID The specified userID required to know which user the MLocation belongs to
     */
    public MLocation(String userID){
        this(userID,DEFAULT_LONGITUDE,DEFAULT_lATITUDE);
    }

    /**
     * Another constructor for MLocation, where the coordinates can be specified.
     * @param userID The specified userID required to know which user the MLocation belongs to
     * @param longitude Longitude of the geographic location
     * @param latitude Latitude of the geographic location
     */
    public MLocation(String userID, double longitude, double latitude){
        this.userID = userID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = "New MLocation";
        this.message = "Here I am";
        this.locationType = 0;
        this.radius = DEFAULT_LOCATION_RADIUS;
        this.visible = true;
        this.urlProfilePhoto = DEFAULT_URL_PROFIL_PIC;
        this.spokenLanguages = "";
        this.interests = "";
    }

    /**
     * Accessor method for the latitude of the geographic location.
     * @return Latitude of the geographic location
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Accessor method for the longitude of the geographic location.
     * @return Longitude of the geographic location
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Getter for the message field, MLocation contains to hold additional information.
     * @return The message MLocation contains
     */
    public String getMessage() {
        return message;
    }

    /**
     * Getter for the title of the MLocation
     * @return The title of the MLocation
     */
    public String getTitle() {
        return title;
    }

    @Override
    public String getID() {
        return userID;
    }

    /**
     * Getter for the URL of the profile picture of the user associated with the MLocation.
     * @return The  URL of the profile picture of the user associated with the MLocation
     */
    public String getUrlProfilePhoto() {
        return urlProfilePhoto;
    }

    /**
     * Setter for the URL of the profile picture of the user associated with the MLocation.
     * @param  urlProfilePhoto The  URL of the profile picture of the user associated with
     *                        the MLocation
     */
    public void setUrlProfilePhoto(String urlProfilePhoto) {
        this.urlProfilePhoto = urlProfilePhoto;
    }

    /**
     * Getter for the spoken languages field of MLocation that contains the languages the user
     * associated with the MLocation speaks.
     * @return The languages the user associated to the MLocation speaks.
     */
    public String getSpokenLanguages() {
        return this.spokenLanguages;
    }

    /**
     * Setter for the spoken languages field of MLocation that contains the languages the user
     * associated with the MLocation speaks.
     * @param spokenLanguages The languages the user associated to the MLocation speaks.
     */
    public void setSpokenLanguages(String spokenLanguages) {
        if (spokenLanguages != null) this.spokenLanguages = spokenLanguages;
    }

    /**
     * Getter for the interests field explaining the interests of the user associated with the
     * MLocation instance.
     * @return The interests of the user associated with the MLocation
     */
    public String getInterests() {
        return interests;
    }

    /**
     * Setter for the interests field explaining the interests of the user associated with the
     * MLocation instance.
     * @param interests interests of the user associated with the MLocation
     */
    public void setInterests(String interests) {
        this.interests = interests;
    }

    /**
     * Setter for the latitude of the geographic location represented by the MLocation
     * @param latitude The new latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Setter for the longitude of the geographic location represented by the MLocation
     * @param longitude The new latitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setID(String userID) {
        this.userID = userID;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double newRadius) {
            radius = newRadius;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean getDeleted() { return deleted; }

    public void setDeleted(boolean deleted) { this.deleted = deleted; }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Determines whether the MLocation represents a topic and it is removable or not. Returns
     * a boolean indicating the result.
     * @return True if the MLocation is a topic and it is removable
     */
    public boolean isRemovableTopic() { // Is Topic created by the current user?
        return this.locationType == 2 &&
                this.getOwnerId().equals(UserInfo.getInstance().getCurrentUser().getID());
    }

    public ArrayList<String> getLanguageList(){
        return languageList;
    }

    public void addLanguage(String language){
        languageList.add(language);
    }

}
