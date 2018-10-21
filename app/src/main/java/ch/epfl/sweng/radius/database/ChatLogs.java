package ch.epfl.sweng.radius.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.*;

/**
 * This class represent a Chat conversation with a list of participants and a List of Messages
 */
public class ChatLogs {
    private static int idGenerator = 0;

    private String convID;
    private List<String> userIDs;
    private LinkedList<Message> conversations; // List LIFO of all the message in the chat
    public ChatLogs(ArrayList<String> participants){
        this.convID = Integer.toString(idGenerator++);
        this.userIDs = participants;
        this.conversations = new LinkedList<>();
    }

    // Getters
    public String getConvID(){return convID; }

    public List<String> getParticipants() {
        return userIDs;
    }

    public LinkedList<Message> getAllConversations() {
        return conversations;
    }

    public LinkedList<Message> getLastNMessages(int n) {
        LinkedList<Message> ret = new LinkedList<>();
        int maxIndex = max(n, conversations.size());
        for (int i = 0; i<maxIndex; i++)
            ret.addLast(conversations.get(i));
        return ret;
    }

    // Setters
    public void addParticipant(String userID){
        if(!userIDs.contains(userID))
            userIDs.add(userID);
    }
    public void addMessage(Message message){
        conversations.addFirst(message);
    }
}
