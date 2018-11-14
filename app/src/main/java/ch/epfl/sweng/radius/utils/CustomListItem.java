package ch.epfl.sweng.radius.utils;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.User;

public class CustomListItem {
    private User user;
    private int profilePic;
    private String userId;
    private String convId;
    private String nickname;


    public CustomListItem(User user){
        this.user = user;
        this.nickname = user.getNickname();
        this.userId = user.getID();
        ChatLogs emptyConv = new ChatLogs();
        // TODO Verify it is correct
        this.convId = emptyConv.getID();
        this.profilePic = R.drawable.user_photo_default;

      }

    public CustomListItem(User user, String convId){
        this.nickname = user.getNickname();
        this.userId = user.getID();
        this.convId = convId;
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

    public String getUserId() {
        return userId;
    }

    public String getConvId() {
        return convId;
    }

    public String getFriendName() {
        return nickname;
    }
}
