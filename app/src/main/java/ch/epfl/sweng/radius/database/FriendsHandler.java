package ch.epfl.sweng.radius.database;

import java.util.ArrayList;
import java.util.List;

public class FriendsHandler {

    private List<String> friendsRequests;
    private List<String> friendsInvitations;
    private List<String> friends;
    private List<String> blockedUsers;

    public FriendsHandler() {
        this.friendsRequests = new ArrayList<>();
        this.friendsInvitations = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
    }

    public List<String> getFriendsRequests() {
        return friendsRequests;
    }

    public List<String> getFriendsInvitations() {
        return friendsInvitations;
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<String> getBlockedUsers() {
        return blockedUsers;
    }

    public void addFriendRequest(String friendID) {
        if (!friendsInvitations.contains(friendID))
            friendsRequests.add(friendID);
        else {
            friendsInvitations.remove(friendID);
            friends.add(friendID);
        }
    }

    public void removeFriendRequest(String friendID) {
        if (friendsRequests.contains(friendID))
            friendsRequests.remove(friendID);
    }

    public void addFriend(String friendID) {
        if (!friends.contains(friendID)) {
            friends.add(friendID);
            if (friendsRequests.contains(friendID))
                friendsRequests.remove(friendID);
        }
    }

    public void removeFriend(String friendID) {
        if (friends.contains(friendID))
            friends.remove(friendID);
    }

    public void addFriendInvitation(String friendID) {
        if (!friendsInvitations.contains(friendID)) {
            friendsInvitations.add(friendID);

        }
    }

    public void removeFriendInvitation(String friendID){
            if (friendsInvitations.contains(friendID))
                friendsInvitations.remove(friendID);
        }

    public void addBlockedUser(String friendID) {
        if (!blockedUsers.contains(friendID)) {
            blockedUsers.add(friendID);
        }
    }

    public void removeBlockedUser(String friendID) {
        if (blockedUsers.contains(friendID))
            blockedUsers.remove(friendID);
    }
}
