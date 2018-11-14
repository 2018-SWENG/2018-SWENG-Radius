package ch.epfl.sweng.radius.browseProfiles;

import ch.epfl.sweng.radius.database.User;

public class ChatListItem {
    private int image;
    private User user;

    public ChatListItem(int image, User user){
        this.image = image;
        this.user = user;
    }

    public int getImage() {
        return image;
    }

    public User getUser() {
        return user;
    }
}
