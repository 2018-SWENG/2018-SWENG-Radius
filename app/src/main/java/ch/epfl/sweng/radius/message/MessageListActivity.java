package ch.epfl.sweng.radius.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.ChatLogDbUtility;
import ch.epfl.sweng.radius.utils.UserInfos;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        messageZone = (EditText) findViewById(R.id.edittext_chatbox);

        final String otherUserId = "2";

        //get chatlogs from db
        //chatLogs = ChatLogDbUtility.getChatLogs(someChatLogsId);


        ArrayList<String> participantsId = new ArrayList<String>(){{
            add(UserInfos.getUserId());
            add(otherUserId);
        }};

        chatLogs = new ChatLogs(participantsId);

        myMessageRecycler = findViewById(R.id.reyclerview_message_list);
        myMessageAdapter = new MessageListAdapter(this, chatLogs.getAllMessages());
        myMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        myMessageRecycler.setAdapter(myMessageAdapter);


        Firebase.setAndroidContext(this);
        //chatReference = new Firebase("https://radius-1538126456577.firebaseio.com/messages/" + UserInfos.getchatList().getChatId(receiver.getUserId()));

        //test :
        chatReference = new Firebase("https://radius-1538126456577.firebaseio.com/messages/myChatID");

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
        chatReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String senderId = map.get("senderId").toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                Date sendingTime = null;
                try {
                    sendingTime = simpleDateFormat.parse(map.get("sendingTime").toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                chatLogs.addMessage(new Message(senderId,message,sendingTime));
               myMessageAdapter.notifyDataSetChanged();
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
}
