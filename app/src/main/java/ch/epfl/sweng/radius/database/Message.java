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
    private final  int ownerID;
    private final String contentMessage;
    private final Date sendingTime;

    public Message(long messageID, int ownerID, String contentMessage, Date sendingTime){
        this.messageID = messageID;
        this.ownerID = ownerID;
        this.contentMessage = contentMessage;
        this.sendingTime = sendingTime;
    }

    // Getters
    public long getMessageID() {
        return messageID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public Date getSendingTime() {
        return sendingTime;
    }
}
