package ch.epfl.sweng.radius.utils;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.User;

public class CustomListItem {
    private String userId;
    private String convId;
    private String nickname;
    private int profilePic;

    public CustomListItem(User user, String convId){
        this.nickname = user.getNickname();
        this.userId = user.getID();
        this.convId = convId;
        this.profilePic = R.drawable.user_photo_default;
    }

    // getters & setters
    public String getFriendName() {
        return nickname;
    }

    public int getFriendProfilePic() {
        return profilePic;
    }

    public String getUserId() {
        return userId;
    }

    public String getConvId() {
        return convId;
    }
}
