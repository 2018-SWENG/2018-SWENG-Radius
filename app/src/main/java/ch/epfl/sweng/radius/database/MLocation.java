package ch.epfl.sweng.radius.database;

import java.io.Serializable;
import java.util.ArrayList;

public class MLocation implements DatabaseObject, Serializable {

    private final static double DEFAULT_LOCATION_RADIUS = 2000;
    private final static double DEFAULT_LONGITUDE = 6.5681216000000004;
    private final static double DEFAULT_lATITUDE = 46.5160698;
    private final static String DEFAULT_URL_PROFIL_PIC = "https://firebasestorage.googleapis.com/v0/b/radius-1538126456577.appspot.com/o/profilePictures%2Fdefault.png?alt=media&token=ccd39de0-9921-487b-90e7-3501262d7835";


    private String userID;
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
    private ArrayList<String> languageList;

    private String ownerId = ""; // for topics, no significance for locations and groups (so default is "")

    public MLocation(){
        this(Database.getInstance().getCurrent_user_id());
    }

    public MLocation(String userID){
        this(userID,DEFAULT_LONGITUDE,DEFAULT_lATITUDE);
    }

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
        this.languageList = new ArrayList<>();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getID() {
        return userID;
    }

    public String getUrlProfilePhoto() {
        return urlProfilePhoto;
    }

    public String getSpokenLanguages() {
        return this.spokenLanguages;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public void setSpokenLanguages(String spokenLanguages) {
        if (spokenLanguages != null) this.spokenLanguages = spokenLanguages;
    }

    public void setUrlProfilePhoto(String urlProfilePhoto) {
        this.urlProfilePhoto = urlProfilePhoto;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

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
