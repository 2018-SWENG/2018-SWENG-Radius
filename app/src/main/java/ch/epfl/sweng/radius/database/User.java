package ch.epfl.sweng.radius.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is design to store all the element we need about a user in the app
 * We can then store/access the states of each user in the database
 */
public class User implements DatabaseObject, Serializable {
    private static long idGenerator = 0;// Debugging purpose only

    private String userID;
    private List<String> friendsRequests;
    private List<String> friendsInvitations;
    private List<String> friends;
    private List<String> blockedUsers;
    // Map is uID --> convID
    private Map<String, String> chatList = new HashMap<>();
    private Map<String, String> reportList;



    public User(String userID) {
        this.userID = userID;
        this.friendsRequests = new ArrayList<>();
        this.friendsInvitations = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
        this.chatList = new HashMap<>();
        this.reportList = new HashMap<>();
    }

    // Debugging purpose only
    public User(){
        this(Long.toString(idGenerator++));
    }

    // Debugging purpose only
    public User(String userID, String nickname, String status){
        this(userID);
    }

    // Getter

    public List<String> getFriendsRequests() {
        return friendsRequests;
    }

    public List<String> getFriendsInvitations() {
        return friendsInvitations;
    }

    public List<String> getFriends() {
        return friends;
    }
    // Setter

    public List<String> getBlockedUsers() {
        return blockedUsers;
    }

    public void setBlockedUsers(List<String> blockedUsers) { this.blockedUsers = blockedUsers; }

    public Map<String, String> getChatList() {
        return chatList;
    }

    public void setChatList(Map<String, String> chatList) {
        this.chatList = chatList;
    }

    public String getConvFromUser(String userID) {
        String convId = chatList.get(userID);
        if (convId == null) {
            return "";
        } else {
            return convId;
        }
    }

    public void addFriendRequest(User friend) {
        if (friendsInvitations.contains(friend.getID())) {
            friend.friendsRequests.remove(this.userID);
            friendsInvitations.remove(friend.getID());
            friends.add(friend.getID());
            friend.friends.add(this.userID);
        } else if (!friendsRequests.contains(friend.getID())) {
            friendsRequests.add(friend.getID());
            friend.friendsInvitations.add(this.userID);
        }
    }

    public void removeFriend(User friend) {
        friends.remove(friend.getID());
        friend.friends.remove(getID());
        Database.getInstance().writeInstanceObj(this, Database.Tables.USERS);
        Database.getInstance().writeInstanceObj(friend, Database.Tables.USERS);
    }

    /**
     * add a chat to a user
     * @param otherUserId the other user ID
     * @param chatID the chat ID
     * @return the chat ID
     */
    public String addChat(String otherUserId, String chatID) {
        if (!chatList.containsKey(otherUserId)) {
            this.chatList.put(otherUserId, chatID);
        }
        return chatID;
    }

    public String newChat(String otherUserId) {
        ArrayList<String> ids = new ArrayList();
        ids.add(otherUserId);
        ids.add(getID());
        ChatLogs chatLogs = new ChatLogs(ids);

        return addChat(otherUserId, chatLogs.getID());
    }

    @Override
    public String getID() {
        return userID;
    }

    public void setID(String userID) {
        this.userID = userID;
    }

    public void addReport(String reportingUserID, String reportingReason) {
        reportList.put(reportingUserID, reportingReason);
    }

    public Map<String, String> getReportList() {
        return reportList;
    }


}
