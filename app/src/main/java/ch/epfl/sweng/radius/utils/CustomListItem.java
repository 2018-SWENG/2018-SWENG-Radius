package ch.epfl.sweng.radius.utils;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.User;

public class CustomListItem {
    private User user;
    private int profilePic;

    public CustomListItem(User user){
        this.user = user;
        this.profilePic = R.drawable.user_photo_default;
    }

    // getters & setters
    public User getItemUser() {
        return user;
    }

    public int getFriendProfilePic() {
        return profilePic;
    }
}
