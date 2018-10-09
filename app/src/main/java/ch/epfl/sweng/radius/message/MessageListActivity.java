package ch.epfl.sweng.radius.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.R;

import static ch.epfl.sweng.radius.R.layout.activity_message_list;

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        User alfred = new User("alfred",1235);
        User mika = new User("Mika",1234);

        UserMessage m1 = new UserMessage("Hello",11111,alfred);
        UserMessage m2 = new UserMessage("Hello alfred",11112,mika);
        UserMessage m3 = new UserMessage("how are you ?",11113,alfred);


        List messageList = new ArrayList();
        messageList.add(m1);
        messageList.add(m2);
        messageList.add(m3);

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
    }
}
