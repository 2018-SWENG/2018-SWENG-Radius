package ch.epfl.sweng.radius.home;

public class PeopleListItem {
    private String friendName;
    private int friendProfilePic;

    public PeopleListItem(String friendName, int friendProfilePic){

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
