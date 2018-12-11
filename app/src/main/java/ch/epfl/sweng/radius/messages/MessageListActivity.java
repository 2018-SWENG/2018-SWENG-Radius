package ch.epfl.sweng.radius.messages;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.ChatlogsUtil;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.NotificationUtility;

/**
 * Activity that hosts messages between two users
 * MessageListActivity and MessageListAdapter and some layout files are inspired from https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
 */
public class MessageListActivity extends AppCompatActivity {

    private RecyclerView myMessageRecycler;
    private MessageListAdapter myMessageAdapter;
    private EditText messageZone;
    private Button sendButton;
    private ChatLogs chatLogs;
    private String chatId, otherUserId, myID;
    private int locType;
    private static HashMap<String, MessageListActivity> chatInstance = new HashMap<>();
    private static int UNIQUE_INT_PER_CALL = 0;
    private MLocation otherLoc;
    private Database database;
    private ChatState isChatRunning = null;
    private Context context;
    public boolean uiReady = false;

    public MessageListActivity(){}
    public MessageListActivity(ChatLogs chatLogs, Context context, int locType){
        // Just create entry to avoid duplicate activities

        if(MessageListActivity.getChatInstance(chatLogs.getID()) == null){
            Log.e("message", "Construcor called with " + chatLogs.getID() + " " + locType);

            this.otherUserId = ChatLogs.getOtherID(chatLogs);
            Log.e("RealTimeDebug", "MLA : Param of otherUsedId " + otherUserId);

            this.chatId = chatLogs.getID();
            this.chatLogs = chatLogs;
            this.locType =locType;
            chatInstance.put(chatId, this);
            isChatRunning = new ChatState();
            isChatRunning.leaveActivity();
            this.context = context;
        }

    }

    public static MessageListActivity getChatInstance(String chatID){
        return chatInstance.get(chatID);
    }

    public void showNotification(String content, String senderId, String chatId) {
        // Setup Intent to end here in case of click
        Intent notifIntent = new Intent(context, MessageListActivity.class);
        Bundle b = new Bundle();
        b.putString("chatId", chatId);
        b.putString("otherId", otherUserId);
        b.putInt("locType", locType);
        notifIntent.putExtras(b);
        notifIntent.setAction(chatId);
        Log.e("RealTimeDebug", "MLA : Param of item "+ senderId + " " + otherUserId);

        PendingIntent pi = PendingIntent.getActivity(context, 0,notifIntent, 0);
        // Build and show notification
        NotificationUtility.getInstance(null, null, null, null)
                .notifyNewMessage(senderId, content, pi);
    }
    /**
     * Get all infos needed to create the activity
     * We get the chatId and otherUserId from the parent fragment
     */
    private void setInfo() {
        Bundle b = getIntent().getExtras();

        //Get infos from parent fragme

        if (b != null) {
            chatId = b.getString("chatId");
            locType = b.getInt("locType");
            Log.e("RealTimeDebug", "MLA : Param of item setInfo " + chatId + "   " + locType);

            chatInstance.put(chatId, this);

            chatLogs = ChatlogsUtil.getInstance().getChat(chatId, locType);
        //    database.readObjOnce(chatLogs, Database.Tables.CHATLOGS, chatLogCallBack);
     //       Log.e("message", "Setup Messages size" + Integer.toString(chatLogs.getMessages().size()));

        } else {
            throw new RuntimeException("MessagListActivity Intent created without bundle");
        }

    }

    public ChatState getIsChatRunning() {
        return isChatRunning;
    }

    /**
     * Set up the interface
     */
    private void setUpUI() {
        if(chatLogs == null)
            chatLogs = ChatlogsUtil.getInstance().getChat(chatId, locType);

        Log.e("ChatlogsDebug", chatId);
        setContentView(R.layout.activity_message_list);
        messageZone = (EditText) findViewById(R.id.edittext_chatbox);
        myMessageAdapter = new MessageListAdapter(this, chatLogs.getMessages(),chatLogs.getMembersId());
        myMessageRecycler = findViewById(R.id.reyclerview_message_list);
        myMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        myMessageRecycler.setAdapter(myMessageAdapter);
        uiReady = true;

    }


    /**
     * add a message in the chatlogs and notify the adapter
     *
     * @param message the new message
     */
    public void receiveMessage(Message message) {
        myMessageAdapter.setMessages(chatLogs.getMessages());
        myMessageRecycler.smoothScrollToPosition(chatLogs.getNumberOfMessages());
        myMessageAdapter.notifyDataSetChanged();
        Log.e("RealTimeDebug", "MLA Message received !");

    }
    public void addMembersInfo(String membersId){
        if(!chatLogs.getMembersId().contains(membersId)){
            chatLogs.addMembersId(membersId);
        }
        myMessageAdapter.setMembersIds(chatLogs.getMembersId());
        myMessageRecycler.smoothScrollToPosition(chatLogs.getNumberOfMessages());
        myMessageAdapter.notifyDataSetChanged();
        Log.e("RealTimeDebug", "MLA Member received !");
    }



    /**
     * push a message in the table
     *
     * @param senderId the senderId
     * @param message  the message
     * @param date     the date
     */
    private void sendMessage(String senderId, String message, Date date) {
        if (!message.isEmpty()) {
            Message msg = new Message(senderId, message, date);

            chatLogs.addMessage(msg);
            //  database.writeInstanceObj(chatLogs, Database.Tables.CHATLOGS);
            List<Message> newList = chatLogs.getMessages();
       //     Log.e("message", "NewList size is " + newList.size());
            database.writeToInstanceChild(chatLogs, Database.Tables.CHATLOGS, "messages",
                    chatLogs.getMessages());

            messageZone.setText("");
            //receiveMessage(msg);
        }

    }

    /**
     * If the button is clicked, add the message to the db
     */
    private void setUpSendButton() {
        sendButton = findViewById(R.id.button_chatbox_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("message", "Message Sent ");
                String message = messageZone.getText().toString();
                sendMessage(myID, message, new Date());
            }
        });

    }

    private void compareLocation() {
        //TODO check if other users radius contains current user.
        Log.e("ChatlogsDebug", "CompareLocation is : " + String.valueOf(OthersInfo.getInstance().getUsersInRadius().containsKey(otherUserId)) + otherUserId);

        if (locType == 0) {
            Log.e("RealTimeDebug", "Compare location :  " + String.valueOf(OthersInfo.getInstance().getUsersInRadius().containsKey(otherUserId)));
                    Log.e("RealTimeDebug", "Compare location :  " + " blocked " + OthersInfo.getInstance().getUsers().get(otherUserId).getBlockedUsers().
                    contains(UserInfo.getInstance().getCurrentUser().getID()));

            setEnabled(OthersInfo.getInstance().getUsersInRadius().containsKey(otherUserId) &&
                    !OthersInfo.getInstance().getUsers().get(otherUserId).getBlockedUsers().
                            contains(UserInfo.getInstance().getCurrentUser().getID()));
        }
        else {
            setEnabled(true);
        }

            }

    public void setEnabled(boolean enableChat) {
        if (!enableChat) {
            sendButton.setEnabled(false);
            messageZone.setFocusable(false);
            messageZone.setText("You can't text this user.");
        }
        else{
            sendButton.setEnabled(true);
            messageZone.setFocusable(true);
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        String chatId = getIntent().getExtras().getString("chatId");
        getIntent().putExtra("chatId", chatId);
        if(chatId == null) return;
        isChatRunning = new ChatState();

        if(MessageListActivity.getChatInstance(chatId) == null){
            Log.e("RealTimeDebug", "Instance was null ! ");

            chatInstance.put(chatId, this);
            isChatRunning = new ChatState();
            return;
        }
        //
        NotificationUtility.clearSeenMsg(isChatRunning.getUnreadMsg());
        Log.e("RealTimeDebug", "Construcor oNStart with " +chatId + " " + locType +" " + otherUserId);

        isChatRunning.clear();
    }

    @Override
    public void onPause(){
        super.onPause();
        isChatRunning.leaveActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean nightMode = settings.getBoolean("nightModeSwitch", false);
        String temp = getIntent().getExtras().getString("chatId");
            setTheme(R.style.LightTheme);
        if(MessageListActivity.getChatInstance(temp) == null){
            Log.e("RealTimeDebug", "Instance was null ! ");}
        else{
            this.locType = MessageListActivity.getChatInstance(temp).locType;
            this.chatId = MessageListActivity.getChatInstance(temp).chatId;
            this.otherUserId = MessageListActivity.getChatInstance(temp).otherUserId;
        }
        Log.e("NIGHT", nightMode + "");
        Log.e("message", "Construcor oNStart with " +temp + " " + locType);
        if(isChatRunning == null) isChatRunning = new ChatState();
        //ChatInfo.getInstance().addUserObserver(this)
        super.onCreate(savedInstanceState);
        myID = UserInfo.getInstance().getCurrentUser().getID();
        database = Database.getInstance();
        setContentView(R.layout.activity_message_list);
        messageZone = findViewById(R.id.edittext_chatbox);
        this.context = this;

        setInfo();setUpUI();setUpSendButton();
        //setUpListener();
        setEnabled(true);
        compareLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
