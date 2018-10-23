package ch.epfl.sweng.radius.database;

import java.util.Date;

/**
 * This class represent a unique message sent in a particular chat.
 * It's important pair this class with the ChatLogs class to determine
 * who are the receivers. DO NOT STORE DIRECTLY AN INSTANCE OF MESSAGE
 * IN THE DB.
 */
public class Message {
    private String senderID;
    private final String contentMessage;
    private final Date sendingTime;

    public Message(String senderID, User owner, String contentMessage, Date sendingTime){
        this.senderID = senderID;
        this.contentMessage = contentMessage;
        this.sendingTime = sendingTime;
    }

    // Getters
    public String getMessageID() {
        return senderID;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public Date getSendingTime() {
        return sendingTime;
    }
}
