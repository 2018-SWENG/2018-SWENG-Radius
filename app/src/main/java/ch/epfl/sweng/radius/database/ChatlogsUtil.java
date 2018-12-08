package ch.epfl.sweng.radius.database;

import android.app.PendingIntent;
import android.util.Log;
import android.util.Pair;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.radius.messages.ChatState;
import ch.epfl.sweng.radius.messages.MessageListActivity;

public class ChatlogsUtil implements DBLocationObserver {

    /**
     * TODO Implement new OnChildEventListener to enable removing conversations
     */
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
        OthersInfo.getInstance().addLocationObserver(this);
        // Read and setUp listener on the ChatList field of current user
        fetchUserChats();
        // Read and setUp listeners on the Group and Topics chats
        fetchGroupChatsAndListen();
        fetchTopicChatsAndListen();
    }

    private void fetchTopicChatsAndListen(){
        List<String> topicChatsID = new ArrayList<>(OthersInfo.getInstance().getTopicsPos().keySet());

        Database.getInstance().readListObjOnce(topicChatsID,
                Database.Tables.CHATLOGS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        ChatLogs newChat = (ChatLogs) value;
                        topicChat.put(newChat.getID(), newChat);
                        listenToChatMessages(newChat, 2);
                        listenToChatMembers(newChat);
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });

    }

    public void fetchGroupChatsAndListen(){

        List<String> groupChatsID = new ArrayList<>(OthersInfo.getInstance().getGroupsPos().keySet());

        /**
         *  WARNING This suppose that the Topic/Group ID and their respective ChatID are the same !
         */
        Database.getInstance().readListObjOnce(groupChatsID,
                Database.Tables.CHATLOGS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        ChatLogs newChat = (ChatLogs) value;
                        groupChat.put(newChat.getID(), newChat);
                        listenToChatMessages(newChat, 1);
                        listenToChatMembers(newChat);
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
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
                        listenToChatMessages(newChat, 0);
                    }

                    public void onError(DatabaseError error) {

                    }
                }
        );
    }

    private void listenToChatMessages(final ChatLogs chatLogs, final int chatType){
        Pair<String, Class> child = new Pair<String, Class>("messages", Message.class);
        Database.getInstance().listenObjChild(chatLogs, Database.Tables.CHATLOGS, child, new CallBackDatabase() {
            public void onFinish(Object value) {
                Log.e("message", "message received " + ((Message) value).getContentMessage());
                receiveMessage(chatLogs, (Message) value, chatType);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    private void receiveMessage(ChatLogs chatLogs, Message message, int chatType){
        if (!chatLogs.getMessages().contains(message))
            chatLogs.addMessage(message);

        // Setup Sender name to display
        String senderNickname = OthersInfo.getInstance().getUsersInRadius()
                .get(message.getSenderId()).getTitle();
        if(senderNickname == null) senderNickname = "Anonymous";
        // If chat is Group or Topic, add its name to Notification title
        if(chatType != 0) senderNickname = chatLogs.getID() + " : " + senderNickname;

        // Get ChatActivity instance if it exists
        MessageListActivity messageActivity = MessageListActivity.getChatInstance(chatLogs.getID());

        // If return Activity is null, Chat was never opened in the past
        if(messageActivity == null){ messageActivity = new MessageListActivity(chatLogs);}
        ChatState chatState = messageActivity.getIsChatRunning();

        if(!chatState.isRunning()){
            // Show notification as chat is not running
            messageActivity.showNotification(message.getContentMessage(), senderNickname);
            return;
        }
        // If Chat is running, there's nothing to do here
    }


    private void listenToChatMembers(final ChatLogs chatLogs){
        Pair<String, Class> child = new Pair<String, Class>("membersId", String.class);
        Database.getInstance().listenObjChild(chatLogs, Database.Tables.CHATLOGS, child, new CallBackDatabase() {
            public void onFinish(Object value) {
                String newMemberId = (String) value;
                Log.e("MembersId", "New Member :" + newMemberId);
                chatLogs.addMembersId(newMemberId);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    private void fetchSingleChatAndListen(String chatID, final int chatType){
        Database.getInstance().readObjOnce(new ChatLogs(chatID),
                Database.Tables.CHATLOGS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        ChatLogs newChat = (ChatLogs) value;
                        switch (chatType){
                            case 0:
                                userChat.put(getOtherID(newChat), newChat);
                                break;
                            case 1:
                                groupChat.put(newChat.getID(), newChat);
                                break;
                            case 2:
                                topicChat.put(newChat.getID(), newChat);
                                break;
                            default:
                                break;
                        }
                        listenToChatMessages(newChat, chatType);
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
    }

    @Override
    public void onLocationChange(String id) {
        updateGroups();
        updateTopic();
        // Nothing to do for User as we keep the conversation in local list
        // TODO Remove chats when topic/group not in radius anymore
    }
    
    private void updateGroups(){
        for(String s : OthersInfo.getInstance().getGroupsPos().keySet())
            if(!groupChat.keySet().contains(s))
                fetchSingleChatAndListen(s, 1);
    }

    private void updateTopic(){
        for(String s : OthersInfo.getInstance().getTopicsPos().keySet())
            if(!topicChat.keySet().contains(s))
                fetchSingleChatAndListen(s, 2);
    }

}

