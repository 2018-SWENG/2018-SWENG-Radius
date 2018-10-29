package ch.epfl.sweng.radius.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Message;

public class ChatLogDbUtility {

    private FirebaseUtility fbUtil;
    private ChatLogs localInstance;

    public ChatLogDbUtility(ChatLogs localInstance){
        DatabaseReference db = null;
        FirebaseAttributes attr = new FirebaseAttributes(FirebaseDatabase.getInstance(),
                                                         FirebaseAuth.getInstance(),
                                                         db);

        this.localInstance = localInstance;
        this.fbUtil = new FirebaseUtility(attr, localInstance, "chatlogs");
    }

    public void addMessage(Message newMsg){
        localInstance.addMessage(newMsg);
        fbUtil.setInstance(localInstance);
        writeChatLogs();
    }


    public void deleteMessage(int msgIndex){
        localInstance.getMessages().remove(msgIndex);
        fbUtil.setInstance(localInstance);
        writeChatLogs(localInstance);
    }

    public Message getMessage(int index) throws InterruptedException { return readChatLogs().getMessages().get(index);}

    public ChatLogs getChatLogs(String chatLogsID) throws InterruptedException {

        ChatLogs ret = new ChatLogs(chatLogsID);

        ret = (ChatLogs) fbUtil.readOtherObject(chatLogsID);

        return ret;
    }

    public ChatLogs readChatLogs() throws InterruptedException {

        return (ChatLogs) fbUtil.readObj();
    }

    public void writeChatLogs() {

        fbUtil.writeInstanceObj();
    }

    public void writeChatLogs(ChatLogs otherChatlogs) {
        fbUtil.writeOtherObj(otherChatlogs);
    }

}
