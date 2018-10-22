package ch.epfl.sweng.radius.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.*;

/**
 * This class represent a Chat conversation with a list of participants and a List of Messages
 */
public class ChatLogs {
    private static long idGenerator = 0;
    private List<String> membersId;
    private LinkedList<Message> messages; // List LIFO of all the message in the chat
    private final long chatLogsId;


    public ChatLogs(ArrayList<String> membersId){
        //if(membersId.size() != 2) { throw new IllegalArgumentException("Chat must be between 2 users");
        this.membersId = new ArrayList<>(membersId);
        this.messages = new LinkedList<>();
        this.chatLogsId = idGenerator++;

    }

    // Getters
    public List<String> getMembersId() {
        return membersId;
    }

    public LinkedList<Message> getAllConversations() {
        return messages;
    }

    public LinkedList<Message> getLastNMessages(int n) {
        LinkedList<Message> ret = new LinkedList<>();
        int maxIndex = max(n, messages.size());
        for (int i = 0; i<maxIndex; i++)
            ret.addLast(messages.get(i));
        return ret;
    }

    public void addMessage(Message message){
        messages.addFirst(message);
    }

    public long getChatLogsId() {
        return chatLogsId;
    }
}
