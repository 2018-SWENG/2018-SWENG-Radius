package ch.epfl.sweng.radius.database;

import android.app.PendingIntent;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.support.v4.util.Pair;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.radius.messages.ChatState;
import ch.epfl.sweng.radius.messages.MessageListActivity;
import ch.epfl.sweng.radius.utils.NotificationUtility;

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
    public static ChatlogsUtil getInstance(Context ... context){
        if(instance == null)
            instance = context == null ? new ChatlogsUtil(null) : new ChatlogsUtil(context[0]);

        return instance;
    }

    private ChatlogsUtil(Context context){
        this.context = context;
        OthersInfo.getInstance().addLocationObserver(this);
        UserInfo.getInstance().addUserObserver(this);
        // Read and setUp listener on the ChatList field of current user
        fetchUserChats();
        // Read and setUp listeners on the Group and Topics chats
        fetchChatsAndListen();

    }

    public void fetchChatsAndListen(){
        final List<String> topicChatsID = new ArrayList<>(OthersInfo.getInstance().getTopicsPos().keySet());
        List<String> groupChatsID = new ArrayList<>(OthersInfo.getInstance().getGroupsPos().keySet());
        groupChatsID.addAll(topicChatsID);
        Database.getInstance().readListObjOnce(groupChatsID,
                Database.Tables.CHATLOGS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        for(ChatLogs newChat : (ArrayList<ChatLogs>) value){
                            if(topicChatsID.contains(newChat.getID())){
                                topicChat.put(newChat.getID(), newChat);
                                listenToChatMessages(newChat, 2);
                            }
                            else{
                                groupChat.put(newChat.getID(), newChat);
                                listenToChatMessages(newChat, 1);
                            }
                            listenToChatMembers(newChat);
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });

    }



    public void fetchUserChats(){
        Pair<String,Class> chatlistChild = new Pair<String, Class>("chatList", String.class);
        System.out.print(chatlistChild.first);

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

//        if(Database.DEBUG_MODE)
//            return new ChatLogs("10");
        ChatLogs ret = null;
        // TODO UTILISER RET ET SI NULL ON CHANGE ET BOU
        switch (chatType){
            case 0:
                ret = userChat.get(chatID);break;
            case 1:
                ret = groupChat.get(chatID);break;
            case 2:
                ret = topicChat.get(chatID);break;
        }

        if(ret == null && Database.DEBUG_MODE)
            return ((FakeFirebaseUtility) Database.getInstance()).getChat();
        
        return ret;
    }

    public void listenToChatMessages(final ChatLogs chatLogs, final int chatType){
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

    public void receiveMessage(ChatLogs chatLogs, Message message, int chatType){
        MessageListActivity messageActivity = MessageListActivity.getChatInstance(chatLogs.getID());
        if(chatLogs.getMessages().contains(message)) return;
        // Add message to local chatlog
        chatLogs.addMessage(message);

        // Setup Sender name to display
        String senderData = NotificationUtility.getNickname(chatLogs, message, chatType);

        // Get ChatActivity instance if it exists
        // If return Activity is null, Chat was never opened in the past
        if(messageActivity == null) messageActivity = new MessageListActivity(chatLogs, context, chatType);
        if(!messageActivity.getIsChatRunning().isRunning() && upToDate >= 0){
            // Show notification as chat is not running
            messageActivity.showNotification(message.getContentMessage(), senderData, chatLogs.getID());
        }
        messageActivity.receiveMessage(message);

        // If Chat is running, there's nothing to do here
    }

    public String getNewChat(String otherUserId){
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

    public void listenToChatMembers(final ChatLogs chatLogs){
        Pair<String, Class> child = new Pair<String, Class>("membersId", String.class);
        Log.e("ChatlogsDebug", "Chat Members ID is "+ chatLogs.getID());
        Database.getInstance().listenObjChild(chatLogs, Database.Tables.CHATLOGS, child, new CallBackDatabase() {
            public void onFinish(Object value) {
                String newMemberId = (String) value;
                Log.e("ChatlogsDebug", "Chat Members ID is "+ chatLogs.getID());

                chatLogs.addMembersId(newMemberId);
                if(MessageListActivity.getChatInstance(chatLogs.getID()) != null)
                    MessageListActivity.getChatInstance(chatLogs.getID()).addMembersInfo(newMemberId);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    public void fetchSingleChatAndListen(final String chatID, final int chatType){
        Database.getInstance().readObjOnce(new ChatLogs(chatID),
                Database.Tables.CHATLOGS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        ChatLogs newChat = (ChatLogs) value;
                        switch (chatType){
                            case 0:
                                if(userChat.containsKey(newChat.getID())) return;
                                userChat.put(chatID, newChat); break;
                            case 1:
                                if(groupChat.containsKey(newChat.getID())) return;
                                groupChat.put(newChat.getID(), newChat); break;
                            case 2:
                                if(topicChat.containsKey(newChat.getID())) return;
                                topicChat.put(newChat.getID(), newChat);
                        }
                        listenToChatMessages(newChat, chatType);
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
    }
/*
    public void fetchListChatAndListen(final List<String> chatID, final int chatType){
        Database.getInstance().readListObjOnce(chatID, Database.Tables.CHATLOGS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
               //         Log.e("ChatlogsDebug", "Size of user is" + Integer.toString(userChat.size()));
                        for(ChatLogs newChat : (List<ChatLogs>) value){
                            switch (chatType){
                                case 0:
                                    if(userChat.containsKey(newChat.getID())) continue;
                                    userChat.put(newChat.getID(), newChat); break;
                                case 1:
                                    if(groupChat.containsKey(newChat.getID())) continue;
                                    groupChat.put(newChat.getID(), newChat); break;
                                case 2:
                                    if(topicChat.containsKey(newChat.getID())) continue;
                                    topicChat.put(newChat.getID(), newChat); break;
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
*/
    @Override
    public void onLocationChange(String id) {
        Log.e("ChatlogsDebug", "Update tables " + groupChat.size() + " " + topicChat.size());

        for(String s : new ArrayList<>(OthersInfo.getInstance().getGroupsPos().keySet()))
            fetchSingleChatAndListen(s, 1);
        for(String s : new ArrayList<>(OthersInfo.getInstance().getTopicsPos().keySet()))
            fetchSingleChatAndListen(s, 2);

        // Nothing to do for User as we keep the conversation in local list
        // TODO Remove chats when topic/group not in radius anymore
    }

    @Override
    public void onUserChange(String id) {
        for(Map.Entry<String, String> s : UserInfo.getInstance().getCurrentUser().getChatList().entrySet())
            if(!userChat.keySet().contains(s.getValue()))
                fetchSingleChatAndListen(s.getValue(), 0);
    }
}

