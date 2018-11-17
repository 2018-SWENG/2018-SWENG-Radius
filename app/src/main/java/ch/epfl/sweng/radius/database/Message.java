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
        this.contentMessage = "";
        this.sendingTime = new Date();
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

    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }
        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Message)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Message m = (Message) o;

        boolean time =  m.sendingTime.toString().equals(this.sendingTime.toString());
        boolean content =  m.contentMessage.equals(this.contentMessage);
        boolean sender =  m.senderId.equals(this.senderId);

        return time && content && sender;
    }
}

