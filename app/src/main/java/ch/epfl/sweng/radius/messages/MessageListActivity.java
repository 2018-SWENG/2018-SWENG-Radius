package ch.epfl.sweng.radius.messages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.DBUserObserver;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.MapUtility;


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
    private ValueEventListener listener;

    //these might cause problems when we switch to multiple users and multiple different chats
    //This field will be used to enable chat with FRIENDS not in radius
    private static User myUser, otherUser;
    private MLocation otherLoc;
    private Database database;


    private final CallBackDatabase otherLocationCallback = new CallBackDatabase() {
        @Override
        public void onFinish(Object value) {
            otherLoc = (MLocation) value;

        }

        @Override
        public void onError(DatabaseError error) {

        }
    };

    private String getOtherID() {
        String otherId = this.otherUserId;
        if (chatLogs.getMembersId().size() == 2) {
            String tempID = chatLogs.getMembersId().get(0);
            String tempID2 = chatLogs.getMembersId().get(1);
            otherId = tempID.equals(myID) ?
                    tempID : tempID2;
        }
        return otherId;
    }

    private final CallBackDatabase chatLogCallBack = new CallBackDatabase() {
        @Override
        public void onFinish(Object value) {
            chatLogs = (ChatLogs) value;
            if (chatLogs.getMembersId().size() == 2) {
                String otherId = getOtherID();
                otherLoc = new MLocation(otherId);
                database.readObjOnce(otherLoc, Database.Tables.LOCATIONS, otherLocationCallback);
            }
            if (chatLogs.getMembersId().size() < 2 && otherUserId != null) {
                chatLogs.addMembersId(otherUserId);
            }
            if (!chatLogs.getMembersId().contains(myID))
                chatLogs.addMembersId(myID);

            database.writeInstanceObj(chatLogs, Database.Tables.CHATLOGS);
            Log.e("message", "Calllback Messages size" + Integer.toString(chatLogs.getMessages().size()));


        }

        @Override
        public void onError(DatabaseError error) {
            Log.e("Firebase", "Error reading Database");
        }
    };

    /**
     * Get all infos needed to create the activity
     * We get the chatId and otherUserId from the parent fragment
     */
    private void setInfo() {
        Bundle b = getIntent().getExtras();

        //Get infos from parent fragment
        otherUserId = "";

        if (b != null) {
            chatId = b.getString("chatId");
            Log.w("Message", "ChatId is " + chatId);
            otherUserId = b.getString("otherId");
            chatLogs = new ChatLogs(chatId);
            database.readObjOnce(chatLogs, Database.Tables.CHATLOGS, chatLogCallBack);
            Log.e("message", "Setup Messages size" + Integer.toString(chatLogs.getMessages().size()));
        } else {
            throw new RuntimeException("MessagListActivity Intent created without bundle");
        }

    }

    /**
     * Set up the interface
     */
    private void setUpUI() {
        setContentView(R.layout.activity_message_list);
        messageZone = (EditText) findViewById(R.id.edittext_chatbox);
        myMessageAdapter = new MessageListAdapter(this, chatLogs.getMessages(),chatLogs.getMembersId());
        myMessageRecycler = findViewById(R.id.reyclerview_message_list);
        myMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        myMessageRecycler.setAdapter(myMessageAdapter);

    }


    /**
     * add a message in the chatlogs and notify the adapter
     *
     * @param message the new message
     */
    private void receiveMessage(Message message) {

        if (!chatLogs.getMessages().contains(message))
            chatLogs.addMessage(message);
        Log.e("message", "Messages size" + Integer.toString(chatLogs.getMessages().size()));
        Log.e("message", "Messages size" + Integer.toString(chatLogs.getNumberOfMessages()));
        //  database.writeInstanceObj(chatLogs, Database.Tables.CHATLOGS);
        myMessageAdapter.setMessages(chatLogs.getMessages());
        myMessageRecycler.smoothScrollToPosition(chatLogs.getNumberOfMessages());
        myMessageAdapter.notifyDataSetChanged();
    }


    private void addMembersInfo(String membersId){
        if(!chatLogs.getMembersId().contains(membersId)){
            chatLogs.addMembersId(membersId);
        }
        myMessageAdapter.setMembersIds(chatLogs.getMembersId());
        myMessageRecycler.smoothScrollToPosition(chatLogs.getNumberOfMessages());
        myMessageAdapter.notifyDataSetChanged();
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
            Log.e("message", "NewList size is " + newList.size());
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

    /**
     * If a message is added in the db, add the message in the chat
     */
    private void setUpListener() {

        Pair<String, Class> child = new Pair<String, Class>("messages", Message.class);
        database.listenObjChild(chatLogs, Database.Tables.CHATLOGS, child, new CallBackDatabase() {
            public void onFinish(Object value) {
                Log.e("message", "message received " + ((Message) value).getContentMessage());
                receiveMessage((Message) value);

            }

            @Override
            public void onError(DatabaseError error) {

            }
        });

        Pair<String, Class> child_members = new Pair<String, Class>("membersId", String.class);
        database.listenObjChild(chatLogs, Database.Tables.CHATLOGS, child_members, new CallBackDatabase() {
            public void onFinish(Object value) {
                Log.e("membersId", "members list update");
                addMembersInfo((String) value);

            }

            @Override
            public void onError(DatabaseError error) {

            }
        });

    }

    private void prepareUsers(ArrayList<String> participants) {
        database.readListObjOnce(participants, Database.Tables.USERS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                if (((ArrayList) value).size() == 2) {
                    myUser.setRadius(((User) (((ArrayList) value).get(0))).getRadius());
                    otherUser.setRadius(((User) (((ArrayList) value).get(1))).getRadius());
                }

            }

            @Override
            public void onError(DatabaseError error) {

                Log.e("Firebase Error", error.getMessage());
            }
        });
    }

    private void compareLocataion() {
        /*TODO check if other users radius contains current user.
           The problem here is, if the other user is in my radius I can send messages to them, however, if current user
           is not in the radius of the other user, they can not reply to those messages.*/
        if (chatLogs.getMembersId().size() == 2)
            setEnabled(MapUtility.isInRadius(otherLoc));
        else
            setEnabled(true);
    }

    public void usersInRadius() { //this method needs to go through severe change - currently we are not saving the radius or the locations of users properly.
        ArrayList<String> participants = (ArrayList) chatLogs.getMembersId();
        otherUser = new User(otherUserId);
        otherLoc = new MLocation(otherUser.getID());
        //read the users from the database in order to be able to access their radius in the compareLocation method.
        prepareUsers(participants);

        //compare the locations of the users and whether they are able to talk to each other or not.
        compareLocataion();

    }

    public void setEnabled(boolean enableChat) {
        if (!enableChat) {
            sendButton.setEnabled(false);
            messageZone.setFocusable(false);
            messageZone.setText("You can't text this user.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.w("MessageActivity", "Just got onCreated");
        //ChatInfo.getInstance().addUserObserver(this);

        super.onCreate(savedInstanceState);
        myUser  = UserInfo.getInstance().getCurrentUser();
        myID = UserInfo.getInstance().getCurrentUser().getID();
        database = Database.getInstance();
        setContentView(R.layout.activity_message_list);
        messageZone = findViewById(R.id.edittext_chatbox);

        setInfo();

        setUpUI();
        setUpSendButton();
        setUpListener();
        setEnabled(true);

       usersInRadius();// This part enables or disables the chat

    }

    @Override
    protected void onStop() {

        super.onStop();

        //database.stopListening(chatLogs.getID() + "chatLogListener", Database.Tables.CHATLOGS);


    }
}
