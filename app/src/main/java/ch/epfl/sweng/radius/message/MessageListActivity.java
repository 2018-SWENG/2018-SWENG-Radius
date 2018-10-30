package ch.epfl.sweng.radius.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.utils.UserInfos;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Activity that hosts messages between two users
 * MessageListActivity and many layout file comes in part from https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
 */
public class MessageListActivity extends AppCompatActivity {
    private RecyclerView myMessageRecycler;
    private MessageListAdapter myMessageAdapter;
    private EditText messageZone;
    private Firebase chatReference;
    private ChatLogs chatLogs;


    private void setUpSendButton(){
        findViewById(R.id.button_chatbox_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageZone.getText().toString();
                if (!message.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("senderId", UserInfos.getUserId());
                    map.put("message", message);
                    map.put("sendingTime", new Date().toString());
                    chatReference.push().setValue(map);
                    messageZone.setText("");
                }
            }
        });
    }

    private void setUpListener(){
        chatReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String pattern = "EEE MMM dd HH:mm:ss Z yyyy";
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String senderId = map.get("senderId").toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                Date sendingTime = null;
                try {
                    sendingTime = simpleDateFormat.parse(map.get("sendingTime").toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                addMessage(new Message(senderId, message, sendingTime));

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

    private void addMessage(Message message){
        chatLogs.addMessage(message);
        myMessageRecycler.smoothScrollToPosition(chatLogs.getAllMessages().size());
        myMessageAdapter.notifyDataSetChanged();
    }

    private void setInfo(String databaseUrl){
        Bundle b = getIntent().getExtras();

        String otherUserId = "";
        String chatId = "";
        if(b != null) {
            otherUserId = b.getString("otherUserId");
            chatId = b.getString("chatId");
        }
        ArrayList<String> participantsId = new ArrayList<String>();
        participantsId.add(UserInfos.getUserId());
        participantsId.add(otherUserId);

        //get chatlogs from db
        //chatLogs = ChatLogDbUtility.getChatLogs(chatId);

        chatLogs = new ChatLogs(participantsId);
        Firebase.setAndroidContext(this);
        //chatReference = new Firebase("https://radius-1538126456577.firebaseio.com/messages/" + UserInfos.getchatList().getChatId(receiver.getUserId()));
        //Hardcoded for now but supposed to be the table reference
        chatReference = new Firebase(databaseUrl+chatId);

    }
    private void setUpUI(){

        setContentView(R.layout.activity_message_list);
        messageZone = (EditText) findViewById(R.id.edittext_chatbox);
        myMessageRecycler = findViewById(R.id.reyclerview_message_list);
        myMessageAdapter = new MessageListAdapter(this, chatLogs.getAllMessages());
        myMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        myMessageRecycler.setAdapter(myMessageAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String databaseMessagesUrl = "https://radius-1538126456577.firebaseio.com/messages/";

        setInfo(databaseMessagesUrl);
        setUpUI();
        setUpSendButton();
        setUpListener();
    }
}
