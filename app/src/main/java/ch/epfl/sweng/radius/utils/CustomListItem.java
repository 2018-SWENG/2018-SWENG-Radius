package ch.epfl.sweng.radius.utils;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.User;

public class CustomListItem {
    private String nickname;
    private int profilePic;

    public CustomListItem(User user){
        this.nickname = user.getNickname();
        this.profilePic = R.drawable.user_photo_default;
    }

    // getters & setters
    public String getFriendName() {
        return nickname;
    }

    public int getFriendProfilePic() {
        return profilePic;
    }
}
