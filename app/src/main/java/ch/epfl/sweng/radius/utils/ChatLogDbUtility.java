package ch.epfl.sweng.radius.utils;

import java.util.List;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Message;

public class ChatLogDbUtility {

    private FirebaseUtility fbUtil;
    private ChatLogs localInstance;

    public ChatLogDbUtility(ChatLogs localInstance){

        this.localInstance = localInstance;
        this.fbUtil = new FirebaseUtility(localInstance, "chatlogs");
    }

    public void addMessage(Message newMsg){}


    public void deleteMessage(Message msg){}

    public Message getMessage(int index){ return null;}

    public ChatLogs getChatLogs(String chatLogsID) throws InterruptedException {

        //ChatLogs ret = new ChatLogs(chatLogsID);

        ChatLogs ret = (ChatLogs) fbUtil.readOtherObject(chatLogsID);

        return ret;
    }

}
