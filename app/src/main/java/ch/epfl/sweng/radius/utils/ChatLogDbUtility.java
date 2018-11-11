package ch.epfl.sweng.radius.utils;

public class ChatLogDbUtility {
    /*
    private FirebaseUtility fbUtil;
    private ChatLogs localInstance;

    public ChatLogDbUtility(ChatLogs localInstance){
        DatabaseReference db = null;
        FirebaseAttributes attr = new FirebaseAttributes(FirebaseDatabase.getInstance(),
                                                         FirebaseAuth.getInstance(),
                                                         db);

        this.localInstance = localInstance;
        this.fbUtil = new FirebaseUtility("chatlogs");
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

        //ChatLogs ret = new ChatLogs(chatLogsID);

        ChatLogs ret = (ChatLogs) fbUtil.readObjOnce(chatLogsID);

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
    */
}
