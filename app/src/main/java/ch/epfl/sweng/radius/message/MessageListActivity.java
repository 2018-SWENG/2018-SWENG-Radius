package ch.epfl.sweng.radius.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.UserInfos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Activity that hosts messages between two users
 * MessageListActivity and many layout file comes in part from https://blog.sendbird.com/android-chat-tutorial-building-a-messaging-ui
 */
public class MessageListActivity extends AppCompatActivity {
    private RecyclerView myMessageRecycler;
    private MessageListAdapter myMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        // Test the chat view
        User alfred = new User(1);
        User mika = new User(UserInfos.getUserId());
        ArrayList<User> participants = new ArrayList<>();
        participants.add(alfred);
        participants.add(mika);

        Message m1 = new Message(1, alfred, "Hello", new Date());
        Message m2 = new Message(2, mika,"Hello alfred", new Date());
        Message m3 = new Message(3, alfred,"how are you ?", new Date());

        ChatLogs messageList= new ChatLogs(participants);
        messageList.addMessage(m1);
        messageList.addMessage(m2);
        messageList.addMessage(m3);

        /*sort by date
        Collections.sort(messageList, new Comparator<UserMessage>() {
            @Override
            public int compare(UserMessage o1, UserMessage o2) {
                return (int) (o1.getCreatedAt() - o2.getCreatedAt());
            }
        });
        */

        // End Test

        myMessageRecycler = findViewById(R.id.reyclerview_message_list);
        myMessageAdapter = new MessageListAdapter(this, messageList);
        myMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        myMessageRecycler.setAdapter(myMessageAdapter);

        findViewById(R.id.button_chatbox_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send
                Log.println(Log.INFO, "info", "In the future, a message will be sent");
            }
        });
    }
}
