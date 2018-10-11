package ch.epfl.sweng.radius.database;

import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.*;

/**
 * This class represent a Chat conversation with a list of participants and a List of Messages
 */
public class ChatLogs {
    private List<Integer> participants;
    private LinkedList<Message> conversations; // List LIFO of all the message in the chat
    public ChatLogs(List<Integer> participants){
        this.participants = participants;
        this.conversations = new LinkedList<>();
    }

    // Getters
    public List<Integer> getParticipants() {
        return participants;
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
    public void addParticipant(Integer userID){
        if(!participants.contains(userID))
            participants.add(userID);
    }
    public void addMessage(Message message){
        conversations.addFirst(message);
    }
}
