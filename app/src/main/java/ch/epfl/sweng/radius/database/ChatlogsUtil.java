package ch.epfl.sweng.radius.database;

import android.content.Context;
import android.support.v4.util.Pair;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.radius.messages.MessageListActivity;
import ch.epfl.sweng.radius.utils.NotificationUtility;

/**
 * This class is an utility to handle conversations
 * Set up the listeners to get Notified when a message is sent
 */
public class ChatlogsUtil implements DBLocationObserver, DBUserObserver{
    private static ChatlogsUtil instance = null;
    private static Map<String, ChatLogs> userChat = new HashMap<>();
    private static Map<String, ChatLogs> topicChat = new HashMap<>();
    private static Map<String, ChatLogs> groupChat = new HashMap<>();
    public int upToDate = -1;

    private Context context;

    /**
     * The singleton constructor
     * @return the only instance of ChatLogsUtil
     */
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

    /**
     * Private contructor, add the class as an observer of OthersInfo and UserInfo
     * @param context
     */
    private ChatlogsUtil(Context context){
        this.context = context;
        OthersInfo.getInstance().addLocationObserver(this);
        UserInfo.getInstance().addUserObserver(this);
        // Read and setUp listener on the ChatList field of current user

        fetchUserChats();
        // Read and setUp listeners on the Group and Topics chats
        fetchChatsAndListen();


    }

    /**
     * Fetch all chats and add listeners for Topics and Groups
     */
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
                            listenToChatMembers(newChat); }upToDate++;
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });

    }

    /**
     * Fetch all chats and add listeners private messages
     */
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

    /**
     * Getter for the ChatLogs
     * @param chatID the id of the chatlog we want
     * @param chatType the type of chatlog, GROUP, TOPICS or PRIVATE
     * @return the corresponding chatlog or null if it doesn't exist
     */
    public ChatLogs getChat(String chatID, int chatType){
        ChatLogs ret = null;
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

    /**
     * Listen to a particular chat session
     * @param chatLogs the chatlogs in witch we want to be notified when a message is sent
     * @param chatType the type of chatlog, GROUP, TOPICS or PRIVATE
     */
    public void listenToChatMessages(final ChatLogs chatLogs, final int chatType){
        Pair<String, Class> child = new Pair<String, Class>("messages", Message.class);
        Database.getInstance().listenObjChild(chatLogs, Database.Tables.CHATLOGS, child, new CallBackDatabase() {
            public void onFinish(Object value) {
                receiveMessage(chatLogs, (Message) value, chatType);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    /**
     * Handle Notifications when a message is receive
     * @param chatLogs the chatlogs in witch a message is sent
     * @param message the message itself
     * @param chatType the type of chatlog, GROUP, TOPICS or PRIVATE
     */
    public void receiveMessage(ChatLogs chatLogs, Message message, int chatType){
        MessageListActivity messageActivity = MessageListActivity.getChatInstance(chatLogs.getID());
        if(chatLogs.getMessages().contains(message)) return;
        // Add message to local chatlog
        chatLogs.addMessage(message);

        // Setup Sender name to display
        String senderData = NotificationUtility.getChatTitleNotification(chatLogs, message, chatType);

        // Get ChatActivity instance if it exists
        // If return Activity is null, Chat was never opened in the past
        if(messageActivity == null) {
            messageActivity = new MessageListActivity(chatLogs, context, chatType);
        }

        handleActvity(messageActivity, message, senderData, chatLogs);
        // If Chat is running, there's nothing to do here
    }

    private void handleActvity(MessageListActivity mla, Message message, String senderData, ChatLogs chatLogs) {
        if(mla.uiReady) mla.receiveMessage(message);

        if(!mla.getIsChatRunning().isRunning() && upToDate >= 0){
            // Show notification as chat is not running
            mla.showNotification(message.getContentMessage(), senderData, chatLogs.getID());
        }
    }

    /**
     * Create a new ChatLog between 2 users
     * @param otherUserId the id of the other user
     * @return the is of the chatlog that we just created
     */
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

    /**
     * Listen every member of a chat to make sure there are still in radius
     * @param chatLogs the chatlog id we want to listen
     */
    public void listenToChatMembers(final ChatLogs chatLogs){
        Pair<String, Class> child = new Pair<String, Class>("membersId", String.class);
        Database.getInstance().listenObjChild(chatLogs, Database.Tables.CHATLOGS, child, new CallBackDatabase() {
            public void onFinish(Object value) {
                String newMemberId = (String) value;
                chatLogs.addMembersId(newMemberId);
                if(MessageListActivity.getChatInstance(chatLogs.getID()) != null){
                    MessageListActivity.getChatInstance(chatLogs.getID()).addMembersInfo(newMemberId);
                }

            }
            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    /**
     * Fetch a chat and listen for new messages and users modifications
     * @param chatID the chatlog id we want to listen
     * @param chatType chatType the type of chatlog, GROUP, TOPICS or PRIVATE
     */
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
                        listenToChatMembers(newChat);
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
    }

    @Override
    public void onLocationChange(String id) {
        if(groupChat.size() == 0){
            fetchChatsAndListen();
            return;
        }
        for(String s : new ArrayList<>(OthersInfo.getInstance().getGroupsPos().keySet()))
            fetchSingleChatAndListen(s, 1);
        for(String s : new ArrayList<>(OthersInfo.getInstance().getTopicsPos().keySet()))
            fetchSingleChatAndListen(s, 2);
    }

    @Override
    public void onUserChange(String id) {
        for(Map.Entry<String, String> s : UserInfo.getInstance().getCurrentUser().getChatList().entrySet())
            if(!userChat.keySet().contains(s.getValue()))
                fetchSingleChatAndListen(s.getValue(), 0);
    }
}

