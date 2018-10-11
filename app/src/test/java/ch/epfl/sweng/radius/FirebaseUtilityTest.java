package ch.epfl.sweng.radius;

import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

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
import java.util.Map;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseDatabase.class})
public class FirebaseUtilityTest {

    private FirebaseUtility uT;
    private final static    String mockUserDBPath       = "../../db/user.json";
    private final static    String mockMsgDBPath        = "../../db/msg.json";
    private final static    String mockChatLogDBPath    = "../../db/chatlog.json";

    private DatabaseReference mockedDatabaseReference;
    private FirebaseDatabase  mockedFirebaseDatabase;
    String  path = "/";
    ValueEventListener listener;
    User mock_user;
    public DatabaseReference updateString(String s){

        return mockedDatabaseReference;
    }

    @Before
    public void before() {
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
                path += argument + "/";
                return true;
            }

        }))).thenReturn(mockedDatabaseReference);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                Object ret_obj;
                String ret;
                String [] parsed_path = path.split("/");

                switch (parsed_path[0]) {
                    case "user" : {
                        ret_obj = getUser(parsed_path[1]);

                        switch(parsed_path[1]) {

                            case()
                        }
                    }
     //               case "chatlogs" : ret = getChatlogs(parsed_path[1]);
     //               case "messages" : ret = getMessages(parsed_path[1]);
                };

                DataSnapshot mockedDataSnapshot = Mockito.mock(DataSnapshot.class);
                when(mockedDataSnapshot.getValue(User.class)).thenReturn(getUser(parsed_path[parsed_path.length -1]));
                // ADD cases here for different data type
                when(mockedDataSnapshot.getValue(String.class)).thenReturn(ret);

                valueEventListener.onDataChange(mockedDataSnapshot);
                //valueEventListener.onCancelled(...);

                return null;
            }
        }).when(mockedDatabaseReference).addListenerForSingleValueEvent(any(ValueEventListener.class));

    }


    @Test
    public void writeToDB() {

        // Try writing to existing user
        mockedDatabaseReference.child("arthur").child("nickname").setValue("Archie");

        mockedDatabaseReference.child("arthurrrrr");

    }


    @Test
    public void getSignedInUserProfileTest() {
      //  when(mockedDatabaseReference.child(anyString())).thenReturn(mockedDatabaseReference);



        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String val = dataSnapshot.getValue(String.class);

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
        mockedDatabaseReference.child("arthur").addListenerForSingleValueEvent(listener);
        // check preferences are updated
    }

    private void writeUser(User user) throws IOException {

        Gson gson = new Gson();
        UserDB userdb;

        try{
            BufferedReader br = new BufferedReader(new FileReader(mockUserDBPath));
            userdb = gson.fromJson(br, UserDB.class);

       //     userdb.addUser(user.uID, user);

            String json = gson.toJson(userdb);

            FileWriter writer = new FileWriter(mockUserDBPath, true);
            writer.write(json);
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

    /*
    private void writeChatlogs(Chatlogs chatlogs){
        Gson gson = new Gson();

        String json = gson.toJson(chatlogs);

        try{
            FileWriter writer = new FileWriter(mockChatLogDBPath);
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeMessage(Message msg){
        Gson gson = new Gson();

        String json = gson.toJson(msg);

        try{
            FileWriter writer = new FileWriter(mockMsgDBPath);
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

}

class UserDB {

    private Map<String, User> database;

    public User getUser(String uID){

        return database.get(uID);
    }

    public void addUser(String uID, User user){

        database.put(uID, user);
    }

    public void removeUser(String uID){

        database.remove(uID);
    }
}
/*
class ChatLogsDB {

    private Map<String, Chatlogs> database;

    public Chatlogs getUser(String uID){

        return database.get(uID);
    }

    public void addUser(String uID, Chatlogs chatlog){

        database.put(uID, chatlog);
    }

    public void removeUser(String uID){

        database.remove(uID);
    }
}

class MessageDB {

    private Map<String, Message> database;

    public Message getMsg(String uID){

        return database.get(uID);
    }

    public void addMsg(String uID, Message msg){

        database.put(uID, msg);
    }

    public void removeMsg(String uID){

        database.remove(uID);
    }
}

*/