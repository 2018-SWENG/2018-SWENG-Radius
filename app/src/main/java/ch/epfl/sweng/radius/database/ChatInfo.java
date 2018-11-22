package ch.epfl.sweng.radius.database;

public class ChatInfo extends DBObservable{
    private final Database database = Database.getInstance();
    private ChatLogs chatLogs = new ChatLogs();

    private ChatInfo(String chatLogID){

    }
}
