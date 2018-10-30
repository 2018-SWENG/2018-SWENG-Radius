package ch.epfl.sweng.radius.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.*;

/**
 * This class represent a Chat conversation with a list of participants and a List of Messages
 */

public class ChatLogs implements DatabaseObject{
    private static long idGenerator = 0;
    private List<String> membersId;
    private List<Message> messages; // List LIFO of all the message in the chat
    private final String chatLogsId;


    public ChatLogs(ArrayList<String> membersId){
        //if(membersId.size() != 2) { throw new IllegalArgumentException("Chat must be between 2 users");
        this.membersId = new ArrayList<>(membersId);
        this.messages = new LinkedList<>();
        this.chatLogsId = Long.toString(idGenerator++);

    }

    public ChatLogs(String chatLogsId){
        //if(membersId.size() != 2) { throw new IllegalArgumentException("Chat must be between 2 users");
        this.membersId = new ArrayList<>();
        this.messages = new LinkedList<>();
        this.chatLogsId = chatLogsId;

    }

    public ChatLogs(){
        this.membersId = new ArrayList<>();
        this.messages = new LinkedList<>();
        this.chatLogsId = Long.toString(idGenerator++);
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
        int maxIndex = max(n, messages.size());
        for (int i = 0; i<maxIndex; i++)
            ret.addLast(messages.get(i));
        return ret;
    }

    // Setters
    public void addMembersId(String userID){
        if(!membersId.contains(userID))
            membersId.add(userID);
    }

    public void setMessages(List<Message> msgs){
        this.messages = msgs;
    }

    public void addMessage(Message message){
        messages.add(message);
    }

    public void deleteMessage(int msgIndex){
        this.messages.remove(msgIndex);
    }

    public String getChatLogsId() {
        return chatLogsId;
    }

    public String getID() {
        return chatLogsId;
    }
}
