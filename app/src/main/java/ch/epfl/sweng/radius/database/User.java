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
    private static long idGenerator = 0; // Debugging purpose only

    private String userID;
    private Map<String, String> friendsRequests;
    private Map<String, String> friendsInvitations;
    private Map<String, String> friends;
    private List<String> blockedUsers;
    // Map is uID --> convID
    private Map<String, String> chatList = new HashMap<>();
    private Map<String, String> reportList;

    /**
     * Constructor to create new User object with the given user ID
     * @param userID: String to set the userID as
     */
    public User(String userID) {
        this.userID = userID;
        this.friendsRequests = new HashMap<>();
        this.friendsInvitations = new HashMap<>();
        this.friends = new HashMap<>();
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

    /**
     * Getter for friends requests
     * @return friendsRequests: HashMap that stores the friends requests of the user
     */
    public Map<String, String> getFriendsRequests() {
        return friendsRequests;
    }

    /**
     * Getter for friends invitations
     * @return friendsInvitations: HashMap that stores the friends invitations of the user
     */
    public Map<String, String> getFriendsInvitations() {
        return friendsInvitations;
    }

    /**
     * Getter for friends
     * @return friends: HashMap that stores the friends of the user
     */
    public Map<String, String> getFriends() {
        return friends;
    }

    /**
     * Getter for blocked users
     * @return blockedUsers: ArrayList that stores the users blocked by this user
     */
    public List<String> getBlockedUsers() {
        return blockedUsers;
    }

    /**
     * Setter for blocked users
     * @param blockedUsers: ArrayList that stores the blocked users
     */
    public void setBlockedUsers(List<String> blockedUsers) { this.blockedUsers = blockedUsers; }

    /**
     * Getter for chat list
     * @return chatList: Map that stores the chats for this user
     */
    public Map<String, String> getChatList() {
        return chatList;
    }

    /**
     * Setter for chat list
     * @param chatList: Map that stores the chats
     */
    public void setChatList(Map<String, String> chatList) {
        this.chatList = chatList;
    }

    /**
     * Returns the conversation id for a given userID
     * @param userID: ID of the user to get the convID
     * @return convID: ID of the conversation for the given userID
     */
    public String getConvFromUser(String userID) {
        String convId = chatList.get(userID);
        if (convId == null) {
            return "";
        } else {
            return convId;
        }
    }

    /**
     * Adds a friend request for the current user, if the user has sent a request to the friend as well,
     * the friend is added to the friends of this user
     * @param friend: the User to add to the friends requests
     */
    public void addFriendRequest(User friend) {
        if (friendsInvitations.containsKey(friend.getID())) {
            friend.friendsRequests.remove(this.userID);
            friendsInvitations.remove(friend.getID());
            friends.put(friend.getID(), friend.getID());
            friend.friends.put(this.userID, this.userID);
        } else if (!friendsRequests.containsKey(friend.getID())) {
            friendsRequests.put(friend.getID(), friend.getID());
            friend.friendsInvitations.put(this.userID, this.userID);
        }
    }

    /**
     * Removes the given user from the friends of this user
     * @param friend: The user to remove as friend
     */
    public void removeFriend(User friend) {
        friends.remove(friend.getID());
        friend.friends.remove(getID());
        Database.getInstance().writeInstanceObj(this, Database.Tables.USERS);
        Database.getInstance().writeInstanceObj(friend, Database.Tables.USERS);
    }

    /**
     * Adds a chat to a user
     * @param otherUserId: the other user ID
     * @param chatID: the chat ID
     * @return the chat ID
     */
    public String addChat(String otherUserId, String chatID) {
        if (!chatList.containsKey(otherUserId)) {
            this.chatList.put(otherUserId, chatID);
        }
        return chatID;
    }

    /**
     * Create a chatlog with the given id and the id of this user
     * @param otherUserId: The other user id to create a chat between
     * @return: The newly created chat id
     */
    public String newChat(String otherUserId) {
        ArrayList<String> ids = new ArrayList();
        ids.add(otherUserId);
        ids.add(getID());
        ChatLogs chatLogs = new ChatLogs(ids);

        return addChat(otherUserId, chatLogs.getID());
    }

    /**
     * Getter for this user's id
     * @return userID: id of this user
     */
    @Override
    public String getID() {
        return userID;
    }

    /**
     * Setter for user's id
     * @param userID: String to set the user id as
     */
    public void setID(String userID) {
        this.userID = userID;
    }

    /**
     * Adds new report to the reports list of this user
     * @param reportingUserID: The id of the user that reported
     * @param reportingReason: The reason for reporting
     */
    public void addReport(String reportingUserID, String reportingReason) {
        reportList.put(reportingUserID, reportingReason);
    }

    /**
     * Getter for reports list of this user
     * @return reportList: reports list of this user
     */
    public Map<String, String> getReportList() {
        return reportList;
    }
}
