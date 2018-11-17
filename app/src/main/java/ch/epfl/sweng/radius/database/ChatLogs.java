package ch.epfl.sweng.radius.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.min;

/**
 * This class represent a Chat conversation with a list of participants and a List of Messages
 */

public class ChatLogs implements DatabaseObject{
    private static long idGenerator = 0;

    private List<String> membersId;
    private List<Message> messages; // List LIFO of all the message in the chat
    private String chatLogsId;
    private int numberOfMessages;


    public ChatLogs(List<String> membersId){
    //    if(membersId.size() < 2) { throw new IllegalArgumentException("Chat must be between 2 users"); }
        this.membersId = new ArrayList<>(membersId);
        this.messages = new LinkedList<>();
        this.numberOfMessages=0;
        this.chatLogsId = Long.toString(idGenerator++);
    }


    public ChatLogs(String chatLogsId) {
        this.membersId = new ArrayList<>();
        this.messages = new LinkedList<>();
        this.chatLogsId = chatLogsId;
        numberOfMessages=0;
    }

    public ChatLogs() {
        this.membersId = new ArrayList<>();
        this.messages = new LinkedList<>();
        this.chatLogsId = Long.toString(idGenerator++);
        this.chatLogsId = "NULL";
    }

    // Getters
    public List<String> getMembersId() {
        return membersId;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public LinkedList<Message> getLastNMessages(int n) {
        LinkedList<Message> ret = new LinkedList<>();
        int maxIndex = min(n, messages.size());
        for (int i = 0; i<maxIndex; i++)
            ret.addLast(messages.get(i));
        return ret;
    }

    // Setters
    public void addMembersId(String userID){
        if(!membersId.contains(userID))
            membersId.add(userID);
        Database.getInstance().writeToInstanceChild(this, Database.Tables.CHATLOGS, "membersId",membersId);

    }

    public void addMessage(Message message){
        messages.add(message);
        Database.getInstance().writeToInstanceChild(this, Database.Tables.CHATLOGS, "numberOfMessages",++numberOfMessages);
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void removeMessage(Message msg){ messages.remove(msg);}

    public void removeMessage(int index){ messages.remove(index);}

    public String getID() {
        return this.chatLogsId;
    }

    public String getChatLogsId() {
        return chatLogsId;
    }

    public void setChatLogsId(String chatLogsId) {
        this.chatLogsId = chatLogsId;
    }

    public int getNumberOfMessages() {
        return numberOfMessages;
    }

}
