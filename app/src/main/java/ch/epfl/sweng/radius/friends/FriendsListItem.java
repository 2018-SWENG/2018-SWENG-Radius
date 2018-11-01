package ch.epfl.sweng.radius.friends;

public class FriendsListItem {
    private String friendName;
    private int friendProfilePic;

    public FriendsListItem(String friendName, int friendProfilePic){

        this.friendName = friendName;
        this.friendProfilePic = friendProfilePic;
    }

    // getters & setters

    public String getFriendName() {
        return friendName;
    }

    public int getFriendProfilePic() {
        return friendProfilePic;
    }
}
