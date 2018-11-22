package ch.epfl.sweng.radius.database;

public class ChatInfo extends DBObservable{

    private static ChatInfo chatInfo = null;
    private static final Database database = Database.getInstance();


    public static ChatInfo getInstance(){
        if (chatInfo == null)
            chatInfo = new ChatInfo();
        return chatInfo;
    }

    private ChatInfo(){
    }
}
