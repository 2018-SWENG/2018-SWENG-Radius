package ch.epfl.sweng.radius.messages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.ChatLogDbUtility;

import ch.epfl.sweng.radius.utils.UserInfos;


/**
 * Activity that hosts messages between two users
 * MessageListActivity and MessageListAdapter and some layout files are inspired from https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
 */
public class MessageListActivity extends AppCompatActivity {
    private RecyclerView myMessageRecycler;
    private MessageListAdapter myMessageAdapter;
    private EditText messageZone;
    private Button sendButton;
    private Firebase chatReference;
    private ChatLogs chatLogs;

    /**
     * Get all infos needed to create the activity
     * We get the chatId and otherUserId from the parent fragment
     *
     * @param databaseUrl the url from the messages table of the database
     */
    private void setInfo(String databaseUrl) {
        Bundle b = getIntent().getExtras();

        //Get infos from parent fragment
        String otherUserId = "";
        String chatId = "";
        if (b != null) {
            otherUserId = b.getString("otherUserId");
            chatId = b.getString("chatId");
        }
        ArrayList<String> participantsId = new ArrayList<String>();
        participantsId.add(UserInfos.getUserId());
        participantsId.add(otherUserId);

        //get chatlogs from db
        //chatLogDbUtility = new ChatLogDbUtility(chatId);
        // chatLogs = ChatLogDbUtility.getChatLogs(chatId);

        chatLogs = new ChatLogs(participantsId);

        Firebase.setAndroidContext(this);
        chatReference = new Firebase(databaseUrl + chatId);

    }

    /**
     * Set up the interface
     */
    private void setUpUI() {

        setContentView(R.layout.activity_message_list);
        messageZone = (EditText) findViewById(R.id.edittext_chatbox);
        myMessageAdapter = new MessageListAdapter(this, chatLogs.getAllMessages());
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
        chatLogs.addMessage(message);
        myMessageRecycler.smoothScrollToPosition(chatLogs.getNumberOfMessages());
        myMessageAdapter.notifyDataSetChanged();
    }


    /**
     * push a message in the table
     * @param senderId the senderId
     * @param message the message
     * @param date the date
     */
    private void sendMessage(String senderId,String message,Date date) {
        if (!message.equals("")) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("senderId", senderId);
            map.put("message", message);
            map.put("sendingTime", date.toString());
            chatReference.push().setValue(map);
            messageZone.setText("");
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
                String message = messageZone.getText().toString();
                sendMessage( UserInfos.getUserId(), message, new Date());
            }
        });

        /*
        findViewById(R.id.button_chatbox_send).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    String message = messageZone.getText().toString();
                    sendMessage( UserInfos.getUserId(), message, new Date());
                    return true;
                }
                return false;
            }

        });
        */
    }


    /**
     * If a message is added in the db, add the message in the chat
     */
    private void setUpListener() {
        chatReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String pattern = "EEE MMM dd HH:mm:ss Z yyyy";
                Map map = dataSnapshot.getValue(Map.class);
                if (!map.isEmpty() && map.size() == Message.NUMBER_ELEMENTS_IN_MESSAGE) {
                    if (map.containsKey("message") && map.containsKey("senderId")) {
                        String message = map.get("message").toString();
                        String senderId = map.get("senderId").toString();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        Date sendingTime = null;
                        try {
                            sendingTime = simpleDateFormat.parse(map.get("sendingTime").toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        receiveMessage(new Message(senderId, message, sendingTime));
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_list);
        messageZone = findViewById(R.id.edittext_chatbox);

        String databaseMessagesUrl = "https://radius-1538126456577.firebaseio.com/messages/";
        setInfo(databaseMessagesUrl);

        setUpUI();
        setUpSendButton();
        setUpListener();

        final Database database = Database.getInstance();
        System.out.println("---------------------------------------" + database.getCurrent_user_id() + "------------------------------------------");
        database.readObjOnce(new User(database.getCurrent_user_id()),
                Database.Tables.USERS, new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        User current_user = (User) value;
                        //current_user.setNickname("God Emperor");
                        System.out.println("------------------------------------------" + current_user.getNickname() + "----------------------------------");
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase Error", error.getMessage());
                    }
                });
        /*messageZone.setFocusable(false);
        messageZone.setText("User is out of radius.");
        sendButton.setEnabled(false);*/
    }
}
