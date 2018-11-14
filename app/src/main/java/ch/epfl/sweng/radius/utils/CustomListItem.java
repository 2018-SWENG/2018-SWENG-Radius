package ch.epfl.sweng.radius.utils;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.User;

public class CustomListItem {
    private User user;
    private String convId;
    private int profilePic;

    public CustomListItem(User user, String convId){
        this.user = user;
        this.convId = convId;
        this.profilePic = R.drawable.user_photo_default;
    }

    // getters & setters
    public User getItemUser() {
        return user;
    }


    public int getFriendProfilePic() {
        return profilePic;
    }

    public String getUserId() {
        return user.getID();
    }

    public String getConvId() {
        return convId;
    }
}
