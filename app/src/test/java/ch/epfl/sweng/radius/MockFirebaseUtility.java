package ch.epfl.sweng.radius;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseDatabase.class})
public class MockFirebaseUtility {

    private FirebaseUtility uT;
    private final static    String mockUserDBPath       = "/home/arthur/Projects/Sweng_Proj/2018-SWENG-Radius/app/src/test/java/ch/epfl/sweng/radius/mock_databases/user.json";
    private final static    String mockMsgDBPath        = "/home/arthur/Projects/Sweng_Proj/2018-SWENG-Radius/app/src/test/java/ch/epfl/sweng/radius/mock_databases/msg.json";
    private final static    String mockChatLogDBPath    = "../../db/chatlog.json";

    private DatabaseReference mockedDatabaseReference;
    private FirebaseDatabase  mockedFirebaseDatabase;
    private DataSnapshot      mockedDataSnapshot;
    private Object obj;
    String  path = "/user/";

    @Before
    public void before() throws IOException {
        mockedDatabaseReference = Mockito.mock(DatabaseReference.class);

        mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);

        PowerMockito.mock(DatabaseReference.class);
        // When the child() method is called, the current path is updated
        //     PATH MUST BE CLEARED BETWEEN OPERATIONS
        when(mockedDatabaseReference.child((String) argThat(new ArgumentMatcher(){

            // Update current and print to console path to console
            @Override
            public boolean matches(Object argument) {
                System.out.println(path);
                User usr = new User();
                path += argument + "/";
                return true;
            }

        }))).thenReturn(mockedDatabaseReference);

        when(mockedDatabaseReference.setValue(any(User.class))).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                writeUser((User) args[0]);
                return null;
            }
        });

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                Object ret_obj = "Arthur";
                String ret = null;
                String [] parsed_path = path.split("/");

                switch (parsed_path[0]) {
                    case "user"     : ret_obj = getUser(parsed_path[1]); break;
                    case "chatlogs" : ret_obj = getChatLogs(parsed_path[1]); break;
                    case "messages" : ret_obj = getMessage(parsed_path[1]); break;
                };

                if(parsed_path.length > 1){
                    // TODO : Implement class specific, attribute-wise methods
                }

                /*
                  TODO : Implement class specific, attribute-wise methods
                when(mockedDataSnapshot.getValue(User.class)).thenReturn(getUser(parsed_path[parsed_path.length -1]));
                when(mockedDataSnapshot.getValue(ChatLogs.class)).thenReturn(ret);
                when(mockedDataSnapshot.getValue(Message.class)).thenReturn(ret)
                */
                valueEventListener.onDataChange(mockedDataSnapshot);
                obj = ret_obj;
                return ret_obj;
            }
        }).when(mockedDatabaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));

        mockedDataSnapshot = Mockito.mock(DataSnapshot.class);
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String [] parsed_path = path.split("/");
                User ret_obj;

                System.out.println("Parsed 1 : " +parsed_path[1]);

                ret_obj = getUser(parsed_path[1]);

                return ret_obj;
            }
        }).when(mockedDataSnapshot).getValue(User.class);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String [] parsed_path = path.split("/");
                Message ret_obj;

                System.out.println("Parsed 1 : " +parsed_path[1]);

                ret_obj = getMessage(parsed_path[1]);

                return ret_obj;
            }
        }).when(mockedDataSnapshot).getValue(Message.class);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String [] parsed_path = path.split("/");
                ChatLogs ret_obj;

                ret_obj = getChatLogs(parsed_path[1]);

                return ret_obj;
            }
        }).when(mockedDataSnapshot).getValue(ChatLogs.class);

        generateJSONUserFile();
        generateJSONMsgFile();
    }


    @Test
    public void writeToDB() {

        path = "";
        User user = new User();
        user.setNickname("Archie");
        // Try writing to existing user
        mockedDatabaseReference.child("user").setValue(user);


        ValueEventListener  listener;

        listener = new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user2 = dataSnapshot.getValue(User.class);

                System.out.println("User : " + user2.getNickname());
                Log.e("Firebase", "User data has been read.");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read user", databaseError.toException());

            }
        };

        mockedDatabaseReference.child("0").addListenerForSingleValueEvent(listener);

        path = "";
        Date date = new Date();

        Message msg = new Message(10, new User(), "Coucou" + Integer.toString(0), date);
        // Try writing to existing user
        mockedDatabaseReference.child("user").setValue(user);


        listener = new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Message msg2 = dataSnapshot.getValue(Message.class);

                System.out.println("User : " + msg2.getContentMessage());
                Log.e("Firebase", "Message data has been read.");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read user", databaseError.toException());

            }
        };

        mockedDatabaseReference.child("0").addListenerForSingleValueEvent(listener);


        return;

    }

    public void generateJSONUserFile() throws IOException {
        UserDB database;
        Gson gson = new Gson();
        FileWriter writer = new FileWriter(mockUserDBPath, false);

        ArrayList<User> list = new ArrayList<>();

        for(int i = 0; i < 20; i++){

            list.add(new User());
        }
        database = new UserDB(list);

        writer.write(gson.toJson(database));
        writer.close();


    }

    public void generateJSONMsgFile() throws IOException {
        MessageDB database;
        Gson gson = new Gson();
        Date date = new Date();
        FileWriter writer = new FileWriter(mockMsgDBPath, false);

        ArrayList<Message> list = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            User u = new User();
            list.add(new Message(i, u, "Coucou" + Integer.toString(i), date));
        }
        database = new MessageDB(list);

        writer.write(gson.toJson(database));
        writer.close();


    }

    /*
    @Test
    public void getSignedInUserProfileTest() {
      //  when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);



        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object val = mockedDataSnapshot.getValue();

                if (val == null){
                    System.out.println("User data is null!");
                    return;
                }


                System.out.println("User data is changed : " + val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        System.out.println("Path : " + path);
        mockedDatabaseReference.child("arthur").addListenerForSingleValueEvent(listener);
        // check preferences are updated
    }
    */

    private void writeUser(User user) throws IOException {

        Gson gson = new Gson();
        UserDB userdb;
        System.out.println("Printing user to File " + user.getNickname());
        try{
            BufferedReader br = new BufferedReader(new FileReader(mockUserDBPath));

            userdb = gson.fromJson(br, UserDB.class);

            System.out.println("Size :" + Integer.toString(userdb.database.size()));
            userdb.addUser(Long.toString(user.getUserID()), user);
            System.out.println("Size :" + Integer.toString(userdb.database.size()));


            FileWriter writer = new FileWriter(mockUserDBPath, false);
            writer.write(gson.toJson(userdb));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User getUser(String key){

        Gson gson = new Gson();
        UserDB userdb;
        User ret;

        try{
            BufferedReader br = new BufferedReader(new FileReader(mockUserDBPath));
            userdb = gson.fromJson(br, UserDB.class);

            ret = userdb.getUser(key);

            return ret;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void removeUser(String key){

        Gson gson = new Gson();
        UserDB userdb;

        try{
            BufferedReader br = new BufferedReader(new FileReader(mockUserDBPath));
            userdb = gson.fromJson(br, UserDB.class);

            userdb.removeUser(key);

            String json = gson.toJson(userdb);
            // Overwrites existing file
            FileWriter writer = new FileWriter(mockUserDBPath, true);
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeChatLogs(ChatLogs chatlogs){
        Gson gson = new Gson();

        ChatLogsDB chatLogsDB;

        try{
            BufferedReader br = new BufferedReader((new FileReader(mockChatLogDBPath)));
            chatLogsDB = gson.fromJson(br, ChatLogsDB.class);

            chatLogsDB.addChatLogs(Long.toString(chatlogs.getParticipants().get(0).getUserID()),
                    chatlogs);

            FileWriter writer = new FileWriter(mockChatLogDBPath, false);
            writer.write(gson.toJson(chatLogsDB));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Message getMessage(String key){

        Gson gson = new Gson();
        MessageDB msgDB;
        Message ret;

        try{
            BufferedReader br = new BufferedReader(new FileReader(mockMsgDBPath));
            msgDB = gson.fromJson(br, MessageDB.class);

            ret = msgDB.getMsg(key);

            return ret;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void removeMessage(String key){

        Gson gson = new Gson();
        MessageDB msgDB;

        try{
            BufferedReader br = new BufferedReader(new FileReader(mockChatLogDBPath));
            msgDB = gson.fromJson(br, MessageDB.class);

            msgDB.removeMsg(key);

            String json = gson.toJson(msgDB);
            // Overwrites existing file
            FileWriter writer = new FileWriter(mockChatLogDBPath, true);
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeMessage(Message msg){
        Gson gson = new Gson();

        MessageDB msgDB;

        try{
            BufferedReader br = new BufferedReader(new FileReader(mockMsgDBPath));
            msgDB = gson.fromJson(br, MessageDB.class);

            msgDB.addMsg(msg);

            FileWriter writer = new FileWriter(mockMsgDBPath, false);
            writer.write(gson.toJson(msgDB));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ChatLogs getChatLogs(String key){

        Gson gson = new Gson();
        ChatLogsDB chatLogsDB;
        ChatLogs ret;

        try{
            BufferedReader br = new BufferedReader(new FileReader(mockChatLogDBPath));
            chatLogsDB = gson.fromJson(br, ChatLogsDB.class);

            ret = chatLogsDB.getChatLogs(key);

            return ret;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void removeChatLogs(String key){

        Gson gson = new Gson();
        ChatLogsDB chatLogsDB;

        try{
            BufferedReader br = new BufferedReader(new FileReader(mockChatLogDBPath));
            chatLogsDB = gson.fromJson(br, ChatLogsDB.class);

            chatLogsDB.removeChatLogs(key);

            String json = gson.toJson(chatLogsDB);
            // Overwrites existing file
            FileWriter writer = new FileWriter(mockChatLogDBPath, true);
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

class UserDB {
    @Expose
    public List<User> database;

    public  UserDB(ArrayList<User> db){
        database = db;
    }

    public User getUser(String uID){

        User user = new User();
        for(int i = 0; i <database.size(); i++){
            if (Long.toString(database.get(i).getUserID()).equals(uID))
                return  database.get(i);
        }
      //  System.out.println("GET  " + database);
        return null;
    }

    public void addUser(String uID, User user){

    ///    if(database.containsKey(uID))
    //        database.remove(uID);
            database.add(user);
    }

    public void removeUser(String uID){

        database.remove(uID);
    }

    public List<User> getUsers(){
        return database;
    }

    public void setUsers(List<User> db){
        this.database = db;
    }
}

class ChatLogsDB {

    private Map<String, ChatLogs> database;

    public ChatLogs getChatLogs(String uID){

        return database.get(uID);
    }

    public void addChatLogs(String uID, ChatLogs chatlog){

        if(!database.containsKey(uID))
            database.remove(uID, chatlog);

        database.put(uID, chatlog);
    }

    public void removeChatLogs(String uID){

        database.remove(uID);
    }
}

class MessageDB {

    public List<Message>  database;

    MessageDB(List<Message> data){
        database = data;
    }

    public Message getMsg(String uID){

        for(int i = 0; i < database.size(); i++){
            if(Long.toString(database.get(i).getMessageID()).equals(uID))
                return database.get(i);
        }
        return null;
    }

    public void addMsg(Message msg){
        database.add(msg);
    }

    public void removeMsg(String uID){

        database.remove(uID);
    }
}

