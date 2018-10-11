package ch.epfl.sweng.radius.database;

import java.util.Date;

/**
 * This class represent a unique message sent in a particular chat.
 * It's important pair this class with the ChatLogs class to determine
 * who are the receivers. DO NOT STORE DIRECTLY AN INSTANCE OF MESSAGE
 * IN THE DB.
 */
public class Message {
    private final long messageID;
    private final  User owner;
    private final String contentMessage;
    private final Date sendingTime;

    public Message(long messageID, User owner, String contentMessage, Date sendingTime){
        this.messageID = messageID;
        this.owner = owner;
        this.contentMessage = contentMessage;
        this.sendingTime = sendingTime;
    }

    // Getters
    public long getMessageID() {
        return messageID;
    }

    public User getOwner() {
        return owner;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public Date getSendingTime() {
        return sendingTime;
    }
}
