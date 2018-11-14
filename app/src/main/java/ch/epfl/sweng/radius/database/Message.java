package ch.epfl.sweng.radius.database;

import java.util.Date;

/**
 * This class represent a unique message sent in a particular chat.
 * It's important pair this class with the ChatLogs class to determine
 * who are the receivers. DO NOT STORE DIRECTLY AN INSTANCE OF MESSAGE
 * IN THE DB.
 */
public class Message {
    public final static Integer NUMBER_ELEMENTS_IN_MESSAGE = 3;

    private String senderId;
    private  String contentMessage;
    private  Date sendingTime;

    public Message(){
        this.senderId = "NULL";
        this.contentMessage = null;
        this.sendingTime = null;
    }

    public Message(String senderId, String contentMessage, Date sendingTime){
        this.senderId = senderId;
        this.contentMessage = contentMessage;
        this.sendingTime = sendingTime;
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

