package ch.epfl.sweng.radius.database;

import android.app.PendingIntent;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.radius.messages.ChatState;
import ch.epfl.sweng.radius.messages.MessageListActivity;

public class ChatlogsUtil implements DBLocationObserver, DBUserObserver{

    /**
     * TODO Implement new OnChildEventListener to enable removing conversations
     */
    private static ChatlogsUtil instance = null;
    private static Map<String, ChatLogs> userChat = new HashMap<>();
    private static Map<String, ChatLogs> topicChat = new HashMap<>();
    private static Map<String, ChatLogs> groupChat = new HashMap<>();
    public int upToDate = -2;

    private Context context;
    public static ChatlogsUtil getInstance(){
        if(instance == null)
            instance = new ChatlogsUtil(null);

        return instance;
    }
    public static ChatlogsUtil getInstance(Context context){
        if(instance == null)
            instance = new ChatlogsUtil(context);

        return instance;
    }

    private ChatlogsUtil(Context context){
        this.context = context;
        OthersInfo.getInstance().addLocationObserver(this);
        UserInfo.getInstance().addUserObserver(this);
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
                        for(ChatLogs newChat : (ArrayList<ChatLogs>) value){
                            topicChat.put(newChat.getID(), newChat);
                            listenToChatMessages(newChat, 2);
                            listenToChatMembers(newChat);
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });

    }

    private void fetchGroupChatsAndListen(){

        List<String> groupChatsID = new ArrayList<>(OthersInfo.getInstance().getGroupsPos().keySet());
        /**
         *  WARNING This suppose that the Topic/Group ID and their respective ChatID are the same !
         */
        Database.getInstance().readListObjOnce(groupChatsID,
                Database.Tables.CHATLOGS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        for(ChatLogs newChat : (ArrayList<ChatLogs>) value){
                            groupChat.put(newChat.getID(), newChat);
                            listenToChatMessages(newChat, 1);
                            listenToChatMembers(newChat);
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
    }

    public static String getOtherID(ChatLogs chat){
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
                        fetchSingleChatAndListen((String) value, 0);

                    }

                    public void onError(DatabaseError error) {

                    }
                }
        );
    }

    public ChatLogs getChat(String chatID, int chatType){

        switch (chatType){
            case 0:
                return userChat.get(chatID);
            case 1:
                return groupChat.get(chatID);
            case 2:
                return topicChat.get(chatID);
        }
        return null;
    }

    private void listenToChatMessages(final ChatLogs chatLogs, final int chatType){
        Pair<String, Class> child = new Pair<String, Class>("messages", Message.class);
        Database.getInstance().listenObjChild(chatLogs, Database.Tables.CHATLOGS, child, new CallBackDatabase() {
            public void onFinish(Object value) {
                Log.e("ChatlogsDebug", "message received " + ((Message) value).getContentMessage());
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
        else
            return;

        if(upToDate < 0) return;
        String senderNickname;
        // Setup Sender name to display
        if(OthersInfo.getInstance().getUsersInRadius()
                .containsKey(message.getSenderId()))
            senderNickname = OthersInfo.getInstance().getUsersInRadius()
                .get(message.getSenderId()).getTitle();
        else senderNickname = "Anonymous";
        // If chat is Group or Topic, add its name to Notification title
        if(chatType != 0) senderNickname = chatLogs.getID() + " : " + senderNickname;

        // Get ChatActivity instance if it exists
        MessageListActivity messageActivity = MessageListActivity.getChatInstance(chatLogs.getID());

        // If return Activity is null, Chat was never opened in the past
        if(messageActivity == null){
            messageActivity = new MessageListActivity(chatLogs, context, chatType);}


        ChatState chatState = messageActivity.getIsChatRunning();
        Log.e("ChatlogsDebug", "Notification Util Chat " + chatLogs.getID());

        if(!chatState.isRunning()){
            // Show notification as chat is not running
            messageActivity.showNotification(message.getContentMessage(), senderNickname, chatLogs.getID());
            return;
        }
        // If Chat is running, there's nothing to do here
    }

    public String getNewChat(String otherUserId){
        Log.e("ChatlogsDebug" , "Asked for new Chat ");
        ChatLogs newChat = new ChatLogs();
        newChat.addMembersId(UserInfo.getInstance().getCurrentUser().getID());
        newChat.addMembersId(otherUserId);

        // Update local data
        userChat.put(newChat.getID(), newChat);
        UserInfo.getInstance().getCurrentUser().addChat(otherUserId, newChat.getID());
        OthersInfo.getInstance().getUsers().get(otherUserId).addChat(UserInfo.getInstance().getCurrentUser().getID(),
                                                                    newChat.getID()); // Should always be there

        // Update database
        Database.getInstance().writeInstanceObj(newChat, Database.Tables.CHATLOGS);
        UserInfo.getInstance().updateUserInDB();
        Database.getInstance().writeInstanceObj(OthersInfo.getInstance().getUsers().get(otherUserId), Database.Tables.USERS);

        return newChat.getID();
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

    private void fetchSingleChatAndListen(final String chatID, final int chatType){
        Database.getInstance().readObjOnce(new ChatLogs(chatID),
                Database.Tables.CHATLOGS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        ChatLogs newChat = (ChatLogs) value;
                        switch (chatType){
                            case 0:
                                if(userChat.containsKey(newChat.getID())) return;
                                userChat.put(chatID, newChat);
                                break;
                            case 1:
                                if(groupChat.containsKey(newChat.getID())) return;
                                groupChat.put(newChat.getID(), newChat);
                                break;
                            case 2:
                                if(topicChat.containsKey(newChat.getID())) return;
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

    private void fetchListChatAndListen(final List<String> chatID, final int chatType){
        Database.getInstance().readListObjOnce(chatID,
                Database.Tables.CHATLOGS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        Log.e("ChatlogsDebug", "Size of user is" + Integer.toString(userChat.size()));
                        for(ChatLogs newChat : (List<ChatLogs>) value){
                            switch (chatType){
                                case 0:
                                    if(!userChat.containsKey(newChat.getID()));
                                        userChat.put(newChat.getID(), newChat);
                                    break;
                                case 1:
                                    if(!groupChat.containsKey(newChat.getID()));
                                        groupChat.put(newChat.getID(), newChat);
                                    break;
                                case 2:
                                    if(!topicChat.containsKey(newChat.getID()));
                                        topicChat.put(newChat.getID(), newChat);
                                    break;
                                default:
                                    break;
                            }
                            listenToChatMessages(newChat, chatType);
                        }
                    upToDate++;
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
    }

    @Override
    public void onLocationChange(String id) {
        Log.e("ChatlogsDebug", "Update tables " + groupChat.size() + " " + topicChat.size());

        updateGroups();
        updateTopic();
        // Nothing to do for User as we keep the conversation in local list
        // TODO Remove chats when topic/group not in radius anymore
    }

    private void updateGroups(){
        fetchListChatAndListen(new ArrayList<>(OthersInfo.getInstance().getGroupsPos().keySet()),
            1);
    }

    private void updateTopic(){
        fetchListChatAndListen(new ArrayList<>(OthersInfo.getInstance().getTopicsPos().keySet()),
                2);
    }

    @Override
    public void onUserChange(String id) {
        for(Map.Entry<String, String> s : UserInfo.getInstance().getCurrentUser().getChatList().entrySet())
            if(!userChat.keySet().contains(s.getValue()))
                fetchSingleChatAndListen(s.getValue(), 0);
    }
}

