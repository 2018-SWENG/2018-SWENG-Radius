package ch.epfl.sweng.radius.database;

import android.util.Log;
import android.util.Pair;

import com.google.firebase.database.DatabaseError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatlogsUtil implements DBLocationObserver {

    private static ChatlogsUtil instance = null;
    private static Map<String, ChatLogs> userChat = new HashMap<>();
    private static Map<String, ChatLogs> topicChat = new HashMap<>();
    private static Map<String, ChatLogs> groupChat = new HashMap<>();

    private static ChatlogsUtil getInstance(){
        if(instance == null)
            instance = new ChatlogsUtil();

        return instance;
    }

    private ChatlogsUtil(){
        // Read and setUp listener on the ChatList field of current user
        fetchUserChats();
        // Read and setUp listeners on the Group and Topics chats
    }

    public void fetchUserConvAndListen(){

        Map<String, String> convIds = UserInfo.getInstance().getCurrentUser().getChatList();
        Database.getInstance();

    }

    private String getOtherID(ChatLogs chat){
        if(chat.getMembersId().size() != 2)
            return null;

        return chat.getMembersId().get(0) == UserInfo.getInstance().getCurrentUser().getID() ?
                chat.getMembersId().get(1) : chat.getMembersId().get(0);
    }

    private void fetchUserChats(){
        Pair<String,Class> chatlistChild = new Pair<String, Class>("chatList", String.class);
        Database.getInstance().listenObjChild(UserInfo.getInstance().getCurrentUser(),
                Database.Tables.USERS,
                chatlistChild,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        ChatLogs newChat = (ChatLogs) value;
                        userChat.put(getOtherID(newChat), newChat);
                        listenToChatMessages(newChat);
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                }
        );
    }

    private void listenToChatMessages(ChatLogs chatLogs){
        Pair<String, Class> child = new Pair<String, Class>("messages", Message.class);
        Database.getInstance().listenObjChild(chatLogs, Database.Tables.CHATLOGS, child, new CallBackDatabase() {
            public void onFinish(Object value) {
                Log.e("message", "message received " + ((Message) value).getContentMessage());
             //   receiveMessage((Message) value);

            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    @Override
    public void onLocationChange(String id) {

    }
}
