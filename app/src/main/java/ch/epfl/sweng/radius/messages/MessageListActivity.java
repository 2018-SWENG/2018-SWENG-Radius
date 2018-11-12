package ch.epfl.sweng.radius.messages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
import ch.epfl.sweng.radius.utils.UserInfos;


/**
 * Activity that hosts messages between two users
 * MessageListActivity and MessageListAdapter and some layout files are inspired from https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
 */
public class MessageListActivity extends AppCompatActivity {
    private RecyclerView myMessageRecycler;
    private MessageListAdapter myMessageAdapter;
    private EditText messageZone;
    private ChatLogs chatLogs;
    private String chatId;
    private ValueEventListener listener;

    /**
     * Get all infos needed to create the activity
     * We get the chatId and otherUserId from the parent fragment
     *
     */
    private void setInfo() {
        Bundle b = getIntent().getExtras();

        //Get infos from parent fragment
        String otherUserId = "";

        if (b != null) {
            chatId      = b.getString("chatId");
            Log.w("Message", "ChatId is " + chatId);
            otherUserId = b.getString("otherUserId");
        }
        ArrayList<String> participantsId = new ArrayList<String>();
        participantsId.add(UserInfos.getUserId());

        participantsId.add(otherUserId);
        if(chatLogs == null)
            chatLogs = new ChatLogs(chatId);
        //get chatlogs from db
        //chatLogDbUtility = new ChatLogDbUtility(chatId);
        Database.getInstance().readObjOnce(chatLogs, Database.Tables.CHATLOGS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        chatLogs = (ChatLogs) value;
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });

    //    chatLogs = new ChatLogs(participantsId);

    }

    /**
     * Set up the interface
     */
    private void setUpUI() {

        setContentView(R.layout.activity_message_list);
        messageZone = (EditText) findViewById(R.id.edittext_chatbox);
        myMessageAdapter = new MessageListAdapter(this, chatLogs.getMessages());
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
        if (!message.isEmpty()) {
            Message msg = new Message(senderId, message, date);
            Log.w("MessageActivity" , "Chatlogs ID is " + chatLogs.getID());

            chatLogs.addMessage(msg);
            Database.getInstance().writeInstanceObj(chatLogs, Database.Tables.CHATLOGS);

            messageZone.setText("");
            receiveMessage(msg);
        }

    }

    /**
     * If the button is clicked, add the message to the db
     */
    private void setUpSendButton() {
        findViewById(R.id.button_chatbox_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageZone.getText().toString();
                sendMessage( Database.getInstance().getCurrent_user_id(), message, new Date());
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
        Database.getInstance().readObj(chatLogs, Database.Tables.CHATLOGS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        String pattern = "EEE MMM dd HH:mm:ss Z yyyy";
                        ArrayList<Message> messages = new ArrayList<>();
                        ChatLogs  ret = (ChatLogs) value;
                        messages.addAll(ret.getMessages());

                        for(Message msg : messages)
                     //       if(!chatLogs.getMessages().contains(msg))
                                receiveMessage(msg);
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                },
            chatLogs.getID());

            }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w("MessageActivity" , "Just got onCreated");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_list);
        messageZone = findViewById(R.id.edittext_chatbox);

        setInfo();

        setUpUI();
        setUpSendButton();
        setUpListener();
    }

    @Override
    protected void onStop() {

        super.onStop();

        Database.getInstance().stopListening(chatLogs.getID(), Database.Tables.CHATLOGS);


    }
}
