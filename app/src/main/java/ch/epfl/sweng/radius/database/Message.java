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
    private final  String senderId;
    private final String contentMessage;
    private final Date sendingTime;

    public Message(long messageID, String senderId, String contentMessage, Date sendingTime){
        this.messageID = messageID;
        this.senderId = senderId;
        this.contentMessage = contentMessage;
        this.sendingTime = sendingTime;
    }

    // Getters
    public long getMessageID() {
        return messageID;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public Date getSendingTime() {
        return sendingTime;
    }
}
