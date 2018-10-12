package ch.epfl.sweng.radius.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.*;

/**
 * This class represent a Chat conversation with a list of participants and a List of Messages
 */
public class ChatLogs {
    private List<User> participants;
    private LinkedList<Message> conversations; // List LIFO of all the message in the chat
    public ChatLogs(ArrayList<User> participants){
        this.participants = participants;
        this.conversations = new LinkedList<>();
    }

    // Getters
    public List<User> getParticipants() {
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
    public void addParticipant(User user){
        if(!participants.contains(user))
            participants.add(user);
    }
    public void addMessage(Message message){
        conversations.addFirst(message);
    }
}
