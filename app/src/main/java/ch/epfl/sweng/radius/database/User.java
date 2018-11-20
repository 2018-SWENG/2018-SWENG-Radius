package ch.epfl.sweng.radius.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is design to store all the element we need about a user in the app
 * We can then store/access the states of each user in the database
 */
public class User implements DatabaseObject {
    private static long idGenerator = 0;// Debugging purpose only

    private String userID;
    private String nickname;
    private String urlProfilePhoto;
    private int radius; // meters
    private String status;
    private List<String> friendsRequests;
    private List<String> friendsInvitations;
    private List<String> friends;
    private List<String> blockedUsers;
    // Map is uID --> convID
    private Map<String, String> chatList = new HashMap<>();
    private Map<String, String> reportList;

    private String spokenLanguages;
    private String interests;


    public User(String userID) {
        this.userID = userID;
        this.nickname = "New User " + userID;
        this.urlProfilePhoto = "";
        this.radius = 50;
        this.status = "Hi, I'm new to radius !";
        this.friendsRequests = new ArrayList<>();
        this.friendsInvitations = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
        this.spokenLanguages = "";
        this.chatList = new HashMap<>();
        this.interests = "";
        this.reportList = new HashMap<>();
    }

    // Debugging purpose only
    public User(){
        this(Long.toString(idGenerator++));
    }

    // Getter
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setUrlProfilePhoto(String urlProfilePhoto) {
        this.urlProfilePhoto = urlProfilePhoto;
    }

    public String getUrlProfilePhoto() {
        return urlProfilePhoto;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) throws IllegalArgumentException {
        if (status.length() > 50) // TODO : config file with all the constants
            throw new IllegalArgumentException("The status is limited to 50 characters");
        this.status = status;
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
    // Setter

    public List<String> getBlockedUsers() {
        return blockedUsers;
    }

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

    public String getSpokenLanguages() {
        return this.spokenLanguages;
    }

    public void setSpokenLanguages(String spokenLanguages) {
        if (spokenLanguages != null) this.spokenLanguages = spokenLanguages;
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
    public String getInterests() {
        return interests;
    }
    public void setInterests(String interests) {
        if (interests.length() > 100)
            throw new IllegalArgumentException("Interests input is limited to 100 characters");
        this.interests = interests;
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

    public String getReportFromUser(String userID) {
        return reportList.get(userID);
    }


}
