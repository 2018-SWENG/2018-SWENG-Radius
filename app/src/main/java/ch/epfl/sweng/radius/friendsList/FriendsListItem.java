package ch.epfl.sweng.radius.friendsList;

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

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public int getFriendProfilePic() {
        return friendProfilePic;
    }

    public void setFriendProfilePic(int friendProfilePic) {
        this.friendProfilePic = friendProfilePic;
    }
}
