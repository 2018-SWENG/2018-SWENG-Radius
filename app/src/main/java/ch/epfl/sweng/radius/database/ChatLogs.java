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
    private String chatLogsId = Long.toString(idGenerator++);
    private int numberOfMessages;


    public ChatLogs(ArrayList<String> membersId){
        if(membersId.size() != 2) { throw new IllegalArgumentException("Chat must be between 2 users"); }
        this.membersId = new ArrayList<>(membersId);
        this.messages = new LinkedList<>();
        numberOfMessages=0;
        //this.chatLogsId = Long.toString(idGenerator++);
    }


    public ChatLogs(String chatLogsId) {
        //if(membersId.size() != 2) { throw new IllegalArgumentException("Chat must be between 2 users");
        this.membersId = new ArrayList<>();
        this.messages = new LinkedList<>();
        this.chatLogsId = chatLogsId;
        numberOfMessages=0;
    }

    // Getters
    public List<String> getMembersId() {
        return membersId;
    }

    public List<Message> getAllMessages() {
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
    }

    public void addMessage(Message message){
        messages.add(message);
        numberOfMessages++;
    }

    public String getID() {
        return chatLogsId;
    }

    public int getNumberOfMessages() {
        return numberOfMessages;
    }
}
