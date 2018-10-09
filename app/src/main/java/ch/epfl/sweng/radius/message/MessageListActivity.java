package ch.epfl.sweng.radius.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import ch.epfl.sweng.radius.R;
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
        User alfred = new User("alfred", 1235);
        User mika = new User(UserInfos.getUserUsername(), UserInfos.getUserId());

        UserMessage m1 = new UserMessage("Hello", new Date().getTime() - 1000000, alfred);
        UserMessage m2 = new UserMessage("Hello alfred", new Date().getTime() - 10000, mika);
        UserMessage m3 = new UserMessage("how are you ?", new Date().getTime(), alfred);


        List messageList = new ArrayList();
        messageList.add(m1);
        messageList.add(m3);
        messageList.add(m2);

        //sort by date
        Collections.sort(messageList, new Comparator<UserMessage>() {
            @Override
            public int compare(UserMessage o1, UserMessage o2) {
                return (int) (o1.getCreatedAt() - o2.getCreatedAt());
            }
        });

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
