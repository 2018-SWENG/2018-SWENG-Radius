package ch.epfl.sweng.radius.mock_databases;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseDatabase.class})
public class MockFirebaseUtility{

    public final static    String mockUserDBPath       = "./src/test/java/ch/epfl/sweng/radius/mock_databases/user.json";
    public final static    String mockMsgDBPath        = "./src/test/java/ch/epfl/sweng/radius/mock_databases/msg.json";
    public final static    String mockChatLogDBPath    = "./src/test/java/ch/epfl/sweng/radius/mock_databases/chatlog.json";

    private DatabaseReference mockedDatabaseReference;
    private FirebaseDatabase  mockedFirebaseDatabase;
    private DataSnapshot      mockedDataSnapshot;
    String  path = "";

    public MockFirebaseUtility() throws IOException {
        mockedDatabaseReference = Mockito.mock(DatabaseReference.class);

        mockedFirebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        when(mockedFirebaseDatabase.getReference()).thenReturn(mockedDatabaseReference);

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFirebaseDatabase);

        PowerMockito.mock(DatabaseReference.class);
        // When the child() method is called, the current path is updated
        //     PATH MUST BE CLEARED BETWEEN OPERATIONS
        when(mockedDatabaseReference.child((String) Matchers.argThat(new ArgumentMatcher(){

            // Update current and print to console path to console
            @Override
            public boolean matches(Object argument) {
                System.out.println(path);
                User usr = new User();
                path += argument + "/";
                return true;
            }

        }))).thenReturn(mockedDatabaseReference);

        when(mockedDatabaseReference.setValue(Matchers.any(User.class))).thenAnswer(new Answer<Void>() {
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
                return ret_obj;
            }
        }).when(mockedDatabaseReference).addListenerForSingleValueEvent(Matchers.any(ValueEventListener.class));

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
        generateJSONChatFile();
    }

    public void clearPath() { path = "";}

    public void generateJSONUserFile() throws IOException {
        UserDB database;
        Gson gson = new Gson();
        BufferedWriter writer = new BufferedWriter(new FileWriter(mockUserDBPath));
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
        BufferedWriter writer = new BufferedWriter(new FileWriter(mockMsgDBPath));

        ArrayList<Message> list = new ArrayList<>();

        for(int i = 0; i < 20; i++){
            User u = new User();
            list.add(new Message(i, u, "Coucou" + Integer.toString(i), date));
        }
        database = new MessageDB(list);

        writer.write(gson.toJson(database));
        writer.close();


    }

    public void generateJSONChatFile() throws IOException {
        ChatLogsDB database;
        Gson gson = new Gson();

        BufferedWriter writer = new BufferedWriter(new FileWriter(mockChatLogDBPath));

        ArrayList<ChatLogs> list = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            users.add(new User());
        }
        for(int i = 0; i < 20; i++){
            ChatLogs chat = new ChatLogs(users);
            users.add(new User());
            list.add(chat);
        }
        database = new ChatLogsDB(list);

        writer.write(gson.toJson(database));
        writer.close();


    }

    public void writeUser(User user) throws IOException {

        Gson gson = new Gson();
        UserDB userdb;
        System.out.println("Printing user to File : " + user.getNickname());
        try{
            BufferedReader br = new BufferedReader(new FileReader(mockUserDBPath));

            userdb = gson.fromJson(br, UserDB.class);

            userdb.addUser(user.getUserID(), user);


            FileWriter writer = new FileWriter(mockUserDBPath, false);
            writer.write(gson.toJson(userdb));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser(String key){

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

    public void removeUser(String key){

        Gson gson = new Gson();
        UserDB userdb;

        try{
            BufferedReader br = new BufferedReader(new FileReader(mockUserDBPath));
            userdb = gson.fromJson(br, UserDB.class);

            userdb.removeUser(key);

            String json = gson.toJson(userdb);
            // Overwrites existing file
            FileWriter writer = new FileWriter(mockUserDBPath, false);
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeChatLogs(ChatLogs chatlogs){
        Gson gson = new Gson();

        ChatLogsDB chatLogsDB;

        try{
            BufferedReader br = new BufferedReader((new FileReader(mockChatLogDBPath)));
            chatLogsDB = gson.fromJson(br, ChatLogsDB.class);

            chatLogsDB.addChatLogs(chatlogs.getParticipants().get(0).getUserID(),
                    chatlogs);

            FileWriter writer = new FileWriter(mockChatLogDBPath, false);
            writer.write(gson.toJson(chatLogsDB));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Message getMessage(String key){

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

    public void removeMessage(String key){

        Gson gson = new Gson();
        MessageDB msgDB;

        try{
            BufferedReader br = new BufferedReader(new FileReader(mockChatLogDBPath));
            msgDB = gson.fromJson(br, MessageDB.class);

            msgDB.removeMsg(key);

            String json = gson.toJson(msgDB);
            // Overwrites existing file
            FileWriter writer = new FileWriter(mockChatLogDBPath, false);
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeMessage(Message msg){
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

    public ChatLogs getChatLogs(String key){

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

    public void removeChatLogs(String key){

        Gson gson = new Gson();
        ChatLogsDB chatLogsDB;

        try{
            BufferedReader br = new BufferedReader(new FileReader(mockChatLogDBPath));
            chatLogsDB = gson.fromJson(br, ChatLogsDB.class);

            chatLogsDB.removeChatLogs(key);

            String json = gson.toJson(chatLogsDB);
            // Overwrites existing file
            FileWriter writer = new FileWriter(mockChatLogDBPath, false);
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
            if (database.get(i).getUserID().equals(uID))
                return  database.get(i);
        }
        return null;
    }

    public void addUser(String uID, User user){

        for(int i = 0; i <database.size(); i++){
            if (database.get(i).getUserID().equals(uID))
                database.remove(i);
        }
            database.add(user);
    }

    public void removeUser(String uID){

        for(int i = 0; i <database.size(); i++){
            if (database.get(i).getUserID().equals(uID))
                database.remove(i);
        }
    }

    public List<User> getUsers(){
        return database;
    }

    public void setUsers(List<User> db){
        this.database = db;
    }
}

class ChatLogsDB {

    private List<ChatLogs> database;

    ChatLogsDB(List<ChatLogs> data){
        this.database = data;
    }
    public ChatLogs getChatLogs(String uID){
        System.out.print("Size of chat :" + database.size());
        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getParticipants().get(0).getUserID().equals(uID))
                return database.get(i);
        }
        return null;
    }

    public void addChatLogs(String uID, ChatLogs chatlog){
        for(int i = 0; i <database.size(); i++){
            if (database.get(i).getParticipants().get(0).getUserID().equals(uID))
                database.remove(i);
        }
        database.add(chatlog);
    }

    public void removeChatLogs(String uID){
        for(int i = 0; i <database.size(); i++){
            if (database.get(i).getParticipants().get(0).getUserID().equals(uID))
                database.remove(i);
        }
    }
    public List<ChatLogs> getChatLogs(){
        return database;
    }

    public void setChatlogs(List<ChatLogs> db){
        this.database = db;
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
        for(int i = 0; i < database.size(); i++){
            if(Long.toString(database.get(i).getMessageID()).equals(msg.getMessageID()))
                database.remove(i);
        }
        database.add(msg);
    }

    public void removeMsg(String uID){

        for(int i = 0; i < database.size(); i++){
            if(Long.toString(database.get(i).getMessageID()).equals(uID))
                database.remove(i);
        }
    }

    public List<Message> getMsgs(){
        return database;
    }

    public void setMsgs(List<Message> db){
        this.database = db;
    }
}

