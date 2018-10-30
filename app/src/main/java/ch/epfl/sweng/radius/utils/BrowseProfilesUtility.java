package ch.epfl.sweng.radius.utils;

import ch.epfl.sweng.radius.database.User;

public class BrowseProfilesUtility {

    private String profileOwner;
    private User user;

    public BrowseProfilesUtility(String profileOwner) {
        this.profileOwner = profileOwner;
        user = new User(profileOwner);
    }

    public void reportUser(String reportReason) {
        user.addReport("", reportReason);//we have to put our users ID in the first parameter field.
        //update the user in the firebase db using utility methods
    }

    public String getProfileOwner() {
        return profileOwner;
    }
}
