package ch.epfl.sweng.radius.database;

import java.util.Date;

/**
 * This class represent a unique message sent in a particular chat.
 * It's important pair this class with the ChatLogs class to determine
 * who are the receivers. DO NOT STORE DIRECTLY AN INSTANCE OF MESSAGE
 * IN THE DB.
 */
public class Message {

    private User sender;
    private final String contentMessage;
    private final Date sendingTime;

    public Message(User sender, String contentMessage, Date sendingTime){
        this.sender = sender;
        this.contentMessage = contentMessage;
        this.sendingTime = sendingTime;
    }

    public User getSender() {
        return sender;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public Date getSendingTime() {
        return sendingTime;
    }
}
