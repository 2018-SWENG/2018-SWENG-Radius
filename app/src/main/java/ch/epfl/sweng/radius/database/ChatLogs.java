/**
 * This file contains a class that models a chat conversation.
 * @author RADIUS
 * @version 1.0
 */

package ch.epfl.sweng.radius.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.min;

/**
 * This class models a chat conversation with a list of participants and a
 * list of messages.
 */
public class ChatLogs implements DatabaseObject {

    //Properties
    private static long idGenerator = 0;
    private List<String> membersId;
    private List<Message> messages; // List LIFO of all the message in the chat
    private String chatLogsId;
    private int numberOfMessages;
    private String id;

    /**
     * Setter for the id of the particular conversation
     * @param id The specific id to set to the particular conversation
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Constructor for the ChatLogs class, given a list of membersId,
     * initializes those members as participants.
     * @param membersId The specified list of members with their ids.
     */
    public ChatLogs(List<String> membersId){
        this.membersId = new ArrayList<>(membersId);
        this.messages = new LinkedList<>();
        this.numberOfMessages=0;
        this.chatLogsId = UUID.randomUUID().toString();
        this.id = this.chatLogsId;
    }

    /**
     * Constructor to create a new ChatLog with a particular id.
     * @param chatLogsId The particular id of the created ChatLog
     */
    public ChatLogs(String chatLogsId) {
        this.membersId = new ArrayList<>();
        this.messages = new LinkedList<>();
        this.chatLogsId = chatLogsId;
        numberOfMessages=0;
        this.id = chatLogsId;
    }

    /**
     * Constructor to generate a fresh new ChatLog.
     */
    public ChatLogs() {
        this(UUID.randomUUID().toString());
    }

    /**
     * Getter for the list of participants.
     * @return membersId: The list of participant ids.
     */
    public List<String> getMembersId() {
        return membersId;
    }

    /**
     * Getter for the list of messages.
     * @return membersId: The list of containing the message
     * objects
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Returns the last n messages of the ChatLag.
     * @param n The number of messages that we want to
     *          retrieve from the ChatLog, the messages
     *          are retrieved in the order from the newest
     *          message to the oldest one.
     * @return The last n messages in the ChatLog
     */
    public LinkedList<Message> getLastNMessages(int n) {
        LinkedList<Message> ret = new LinkedList<>();
        int maxIndex = min(n, messages.size());
        for (int i = 0; i<maxIndex; i++)
            ret.addLast(messages.get(i));
        return ret;
    }

    /**
     * Adds a new participant with a specific user id to the chat.
     * @param userID The specific user id of the new participant
     */
    public void addMembersId(String userID){
        if(!membersId.contains(userID))
            membersId.add(userID);
    }

    /**
     * Adds a new message represented as a Message object to the chat.
     * @param message The Message object for the particular message to add.
     */
    public void addMessage(Message message){
        messages.add(message);
        if(!Database.DEBUG_MODE)
            Database.getInstance().writeToInstanceChild(this,
                    Database.Tables.CHATLOGS, "numberOfMessages",++numberOfMessages);
    }

    /**
     * Setter for the list of messages.
     * @param messages A list of messages represented as Message objects,
     *                 that will be the new message list of the ChatLog
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    /**
     * Removes a specified Message object from the list of messages.
     * @param msg The specified Message object to remove from the list
     *            of messages
     */
    public void removeMessage(Message msg) { messages.remove(msg);}

    /**
     * Removes a specified indexed element of the list of messages.
     * @param index The index of the Message to remove
     */
    public void removeMessage(int index) { messages.remove(index);}

    /**
     * Getter for the ChatLog id.
     * @return The id of this ChatLog
     */
    public String getID() {
        return this.id;
    }

    // a deprecated version of the above method
    public String getChatLogsId() {
        return chatLogsId;
    }

    /**
     * Setter for the ChatLog id.
     * @param chatLogsId The new value of chatLogsId to set.
     */
    public void setChatLogsId(String chatLogsId) {
        this.chatLogsId = chatLogsId;
        this.id = chatLogsId;
    }

    /**
     * Returns the number of messages this chat contains.
     * @return The number of messages this chat contains
     */
    public int getNumberOfMessages() {
        return numberOfMessages;
    }

    /**
     * Returns the id of another ChatLog if there are at least 2 participants in the
     * ChatLog, otherwise returns null. The method also adds the current user to the ChatLog
     * if he/she is not participated yet.
     * @param chat The specified ChatLog whose id will be retrieved
     * @return The id of the specified ChatLog if there are at least 2 participants in it,
     *         null otherwise.
     */
    public static String getOtherID(ChatLogs chat){
       if (!chat.getMembersId().contains(UserInfo.getInstance().getCurrentUser().getID())) {
           chat.membersId.add(UserInfo.getInstance().getCurrentUser().getID());
       }
       if(chat.getMembersId().size() != 2) {
            return null;
       }
       return chat.getMembersId().get(0).equals(UserInfo.getInstance().getCurrentUser().getID()) ?
               chat.getMembersId().get(1) : chat.getMembersId().get(0);
    }

}
