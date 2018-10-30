package ch.epfl.sweng.radius.utils;

import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.DatabaseObject;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class MockFirebaseAttr {

    public final static String mockUserDBPath       = "./src/test/java/ch/epfl/sweng/radius/mock_databases/user.json";
    public final static String mockMsgDBPath        = "./src/test/java/ch/epfl/sweng/radius/mock_databases/msg.json";
    public final static String mockChatLogDBPath    = "./src/test/java/ch/epfl/sweng/radius/mock_databases/chatlog.json";

    private DatabaseReference reference;
    private DataSnapshot dataSnapshot;
    private DatabaseObject obj;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;

    private BufferedReader br;
    private Gson gson;
    private FileWriter writer;
    MockedDB mockedDb;

    String path = "";
    String otherPath = "";

    public FirebaseAttributes genMockFirebaseAttribute(DatabaseObject obj) throws IOException {
        reference = Mockito.mock(DatabaseReference.class);
        dataSnapshot = Mockito.mock(DataSnapshot.class);
        firebaseDatabase = Mockito.mock(FirebaseDatabase.class);
        auth = Mockito.mock(FirebaseAuth.class);
        this.obj = obj;
        gson = new Gson();

        if (obj.getClass().equals(User.class))
            path = mockUserDBPath;
        else if (obj.getClass().equals(ChatLogs.class))
            path = mockChatLogDBPath;
        else if (obj.getClass().equals(Message.class))
            path = mockMsgDBPath;

        return null;
    }

    public void setAndWriteMockDb(ArrayList<DatabaseObject> dB) throws IOException {
        // TODO Gen exception if unmatched type
        mockedDb    = new MockedDB<>(dB);
        writer      = new FileWriter(path, false);
        writer.write(gson.toJson(mockedDb));
        writer.close();

    }

    public void setElement(DatabaseObject el) throws IOException {

        br = new BufferedReader(new FileReader(path));
        writer = new FileWriter(path, false);

        // TODO Secure
        mockedDb = gson.fromJson(br, mockedDb.getClass());

        mockedDb.addElement(el);

        writer.write(gson.toJson(mockedDb));
        writer.close();

    }

    public void mockAuth(){

    }

    public void mockDataSnapshot(){

    }

    public void mockDbReference(){

        when(reference.child((String) Matchers.argThat(new ArgumentMatcher(){

            // Update current and print to console path to console
            @Override
            public boolean matches(Object argument) {
                System.out.println(path);
                path += argument + "/";
                return true;
            }

        }))).thenReturn(reference);

        when(reference.setValue(Matchers.any(DatabaseObject.class))).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();

                return null;
            }
        });

        doAnswer(new Answer<DatabaseObject>() {
            @Override
            public DatabaseObject answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];

                valueEventListener.onDataChange(dataSnapshot);
                return obj;
            }
        }).when(reference).addListenerForSingleValueEvent(Matchers.any(ValueEventListener.class));

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String [] parsed_path = path.split("/");
                DatabaseObject ret_obj = null;

                ret_obj = mockedDb.getElement(obj.getID());

                return ret_obj;
            }
        }).when(dataSnapshot).getValue(obj.getClass());
    }
}

class MockedDB<E extends DatabaseObject> {

    private List<E> database;

    public MockedDB(ArrayList<E> db){
        this.database = db;
    }

    public E getElement(String ID){

        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getID().equals(ID))
                return database.get(i);
        }
        return null;
    }

    public void addElement(E el){

        for(int i = 0; i < database.size(); i++){
            if(database.get(i).getID().equals(el.getID()))
                database.remove(i);
        }
        database.add(el);
    }

    public void removeUser(String uID){

        for(int i = 0; i <database.size(); i++){
            if (database.get(i).getID().equals(uID))
                database.remove(i);
        }
    }

    public List<E> getDb(){
        return database;
    }

    public void setDB(List<E> db){
        this.database = db;
    }
}
