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
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.ChatLogDbUtility;

import ch.epfl.sweng.radius.utils.MapUtility;
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

    //these might cause problems when we switch to multiple users and multiple different chats
    private static User us, otherUser;
    private static MLocation usLoc, otherUserLoc;
    private final Database database = Database.getInstance();

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
        participantsId.add(database.getCurrent_user_id());//UserInfos.getUserId()
        participantsId.add(otherUserId);

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

    private void prepareUsers(ArrayList<String> participants) {
        database.readListObjOnce(participants, Database.Tables.USERS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                if (((ArrayList) value).size() == 2) {
                    us.setRadius(((User) (((ArrayList) value).get(0))).getRadius());
                    otherUser.setRadius(((User) (((ArrayList) value).get(1))).getRadius());
                }
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase Error", error.getMessage());
            }
        });
    }

    private void compareLocataion(ArrayList<String> participants) {

        database.readListObjOnce(participants, Database.Tables.LOCATIONS, new CallBackDatabase() {
            @Override
            public void onFinish(Object value) {
                if (((ArrayList) value).size() == 2) {
                    // set the locations of the users based on the values obtained from the database.
                    usLoc.setLatitude(((MLocation) (((ArrayList) value).get(0))).getLatitude());
                    usLoc.setLongitude(((MLocation) (((ArrayList) value).get(0))).getLongitude());
                    otherUserLoc.setLatitude(((MLocation) (((ArrayList) value).get(1))).getLatitude());
                    otherUserLoc.setLongitude(((MLocation) (((ArrayList) value).get(1))).getLongitude());

                    // create map listeners so we can use the isInRadius function to compare locations of both of the users
                    MapUtility mapListenerUs = new MapUtility(us.getRadius());
                    mapListenerUs.setMyPos(usLoc);
                    MapUtility mapListenerOtherUser = new MapUtility(otherUser.getRadius());
                    mapListenerOtherUser.setMyPos(otherUserLoc);

                    // if both users are in each others' radius chat is enabled.
                    setEnabled(mapListenerOtherUser.isInRadius(usLoc, otherUser.getRadius()) && mapListenerUs.isInRadius(otherUserLoc, us.getRadius()));
                }
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Firebase Error", error.getMessage());
            }
        });
    }

    public void usersInRadius() { //this method needs to go through severe change - currently we are not saving the radius or the locations of users properly.
        ArrayList<String> participants = (ArrayList) chatLogs.getMembersId();
        us = new User(participants.get(0));
        otherUser = new User(participants.get(1));
        usLoc = new MLocation();
        otherUserLoc = new MLocation();

        usLoc.setID(us.getID());
        otherUserLoc.setID(otherUser.getID());

        //read the users from the database in order to be able to access their radius in the compareLocation method.
        prepareUsers(participants);

        //compare the locations of the users and whether they are able to talk to each other or not.
        compareLocataion(participants);
    }

    public void setEnabled(boolean enableChat) {
        if (!enableChat) {
            sendButton.setEnabled(false);
            messageZone.setFocusable(false);
            messageZone.setText("You can't text this user.");
        }
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

        usersInRadius();// This part enables or disables the chat
    }
}
