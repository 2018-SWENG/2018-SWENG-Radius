package ch.epfl.sweng.radius.database;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Test;
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

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static ch.epfl.sweng.radius.database.Database.Tables;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doAnswer;


@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseAuth.class, FirebaseDatabase.class })
public class FirebaseUtilityTest {

    FirebaseUtility     fbUtil;
    FirebaseAuth        mockedAuth = Mockito.mock(FirebaseAuth.class);
    FirebaseUser        mockedUser = Mockito.mock(FirebaseUser.class);
    FirebaseDatabase    mockedFb   = Mockito.mock(FirebaseDatabase.class);
    DatabaseReference   mockedDb   = Mockito.mock(DatabaseReference.class);
    DataSnapshot        mockedSnap = Mockito.mock(DataSnapshot.class);

    private Map<String, User> mockedData;
    private Database                    database;
    private CallBackDatabase            callback = Mockito.mock(CallBackDatabase.class);
    private String                      curRef = "1";
    private User                        otherUser;
    private DatabaseError               dberror;

    @Before
    public void setUp() throws Exception {
        fbUtil = new FirebaseUtility();
        mockedData = new HashMap<>();
        for(int i = 0; i < 10; i++){
            String key = Integer.toString(i);
            User val = new User(Integer.toString(i));
            val.setStatus("HeyHeyHey");
            val.addChat("1", "2");
            val.setNickname("Arthy");
            val.setRadius(22);

            mockedData.put(key, val);
        }
    }

    @Test
    public void getCurrent_user_id() {
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockedAuth);
        when(mockedAuth.getCurrentUser()).thenReturn(mockedUser);
        when(mockedUser.getUid()).thenReturn("Coucou");

        assertEquals("Coucou", fbUtil.getCurrent_user_id());
    }

    @Test
    public void readObjOnce() {
        final User user = new User(Integer.toString(1));

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFb);
        when(mockedFb.getReference(any(String.class))).thenReturn(mockedDb);

        when(mockedDb.child((String) Matchers.argThat(new ArgumentMatcher(){

            // Update current and print to console path to console
            @Override
            public boolean matches(Object argument) {
                curRef = (String) argument;
                return true;
            }

        }))).thenReturn(mockedDb);
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                Object ret_obj = mockedData.get(curRef);

                when(mockedSnap.getValue(User.class)).thenReturn(mockedData.get(curRef));

                valueEventListener.onDataChange(mockedSnap);
                return ret_obj;
            }

        }).when(mockedDb).addListenerForSingleValueEvent(any(ValueEventListener.class));

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                User ret_obj = null;
                ret_obj =  mockedData.get(curRef);
                otherUser = ret_obj;
                return ret_obj;
            }
        }).when(mockedSnap).getValue(User.class);
        System.out.print(user.getID());

        fbUtil.readObjOnce(user, Tables.USERS, callback);

        assertEquals(otherUser.getStatus(), "HeyHeyHey");
    }

    @Test
    public void readObjOnce1() {
        final User user = new User(Integer.toString(1));

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFb);
        when(mockedFb.getReference(any(String.class))).thenReturn(mockedDb);

        when(mockedDb.child((String) Matchers.argThat(new ArgumentMatcher() {

            // Update current and print to console path to console
            @Override
            public boolean matches(Object argument) {
                curRef = (String) argument;
                return true;
            }

        }))).thenReturn(mockedDb);
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                Object ret_obj = mockedData.get(curRef);

                when(mockedSnap.getValue()).thenReturn(user);

                valueEventListener.onDataChange(mockedSnap);
                return ret_obj;
            }

        }).when(mockedDb).addListenerForSingleValueEvent(any(ValueEventListener.class));

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                User ret_obj = null;
                ret_obj = mockedData.get(curRef);
                otherUser = ret_obj;
                return ret_obj;
            }
        }).when(mockedSnap).getValue(User.class);
        System.out.print(user.getID());

        fbUtil.readObjOnce(user, Tables.USERS, callback);

    }

    @Test
    public void readObj() {

        final User user = new User(Integer.toString(1));

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFb);
        when(mockedFb.getReference(any(String.class))).thenReturn(mockedDb);

        when(mockedDb.child((String) Matchers.argThat(new ArgumentMatcher(){

            // Update current and print to console path to console
            @Override
            public boolean matches(Object argument) {
                curRef = (String) argument;
                return true;
            }

        }))).thenReturn(mockedDb);
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                Object ret_obj = mockedData.get(curRef);

                when(mockedSnap.getValue(User.class)).thenReturn(mockedData.get(curRef));
                when(mockedSnap.hasChild(anyString())).thenReturn(true);

                valueEventListener.onDataChange(mockedSnap);
                return valueEventListener;
            }

        }).when(mockedDb).addValueEventListener(any(ValueEventListener.class));

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                User ret_obj = null;
                ret_obj =  mockedData.get(curRef);
                otherUser = ret_obj;
                return ret_obj;
            }
        }).when(mockedSnap).getValue(User.class);

        when(mockedSnap.exists()).thenReturn(true);
        System.out.print(user.getID());
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        fbUtil.readObj(user, Tables.USERS, callback, "listener");

        assertEquals(otherUser.getStatus(), "HeyHeyHey");
    }
    @Test
    public void readObj2() {

        final User user = new User(Integer.toString(1));

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFb);
        when(mockedFb.getReference(any(String.class))).thenReturn(mockedDb);

        when(mockedDb.child((String) Matchers.argThat(new ArgumentMatcher(){

            @Override
            public boolean matches(Object argument) {
                curRef = (String) argument;
                return true;
            }

        }))).thenReturn(mockedDb);
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                Object ret_obj = mockedData.get(curRef);

                when(mockedSnap.getValue(User.class)).thenReturn(mockedData.get(curRef));
                when(mockedSnap.hasChild(anyString())).thenReturn(false);

                valueEventListener.onDataChange(mockedSnap);
                return valueEventListener;
            }

        }).when(mockedDb).addValueEventListener(any(ValueEventListener.class));

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                User ret_obj = null;
                ret_obj =  mockedData.get(curRef);
                otherUser = ret_obj;
                return ret_obj;
            }
        }).when(mockedSnap).getValue(User.class);

        System.out.print(user.getID());

        fbUtil.readObj(user, Tables.USERS, callback, "listener");

        assertEquals(otherUser.getStatus(), "HeyHeyHey");
    }

    @Test
    public void readListObjOnce() {

        final User user = new User(Integer.toString(1));

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFb);
        when(mockedFb.getReference(any(String.class))).thenReturn(mockedDb);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                Object ret_obj = mockedData.get(curRef);
                Iterable<DataSnapshot> mockedIteDataSnap = new Iterable<DataSnapshot>() {
                    int max = 5;
                    int cur = 0;

                    @Override
                    public Iterator<DataSnapshot> iterator() {
                        return new Iterator<DataSnapshot>() {
                            @Override
                            public boolean hasNext() {
                               return cur < max;
                            }

                            @Override
                            public DataSnapshot next() {
                                cur++;
                                return mockedSnap;
                            }
                        };
                    }
                };
                when(mockedSnap.hasChild(anyString())).thenReturn(true);
                when(mockedSnap.getChildren()).thenReturn(mockedIteDataSnap);

                valueEventListener.onDataChange(mockedSnap);
                return valueEventListener;
            }

        }).when(mockedDb).addListenerForSingleValueEvent(any(ValueEventListener.class));
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                User ret_obj = null;
                ret_obj =  mockedData.get(curRef);
                otherUser = ret_obj;
                return mockedData.get(curRef);
            }
        }).when(mockedSnap).getValue(User.class);


        List<String> ids = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            ids.add(Integer.toString(i));
        }

        fbUtil.readListObjOnce(ids, Tables.USERS, callback);

    }

    @Test
    public void readAllTableOne() {

        final User user = new User(Integer.toString(1));

        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFb);
        when(mockedFb.getReference(any(String.class))).thenReturn(mockedDb);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ValueEventListener valueEventListener = (ValueEventListener) invocation.getArguments()[0];
                Object ret_obj = mockedData.get(curRef);
                Iterable<DataSnapshot> mockedIteDataSnap = new Iterable<DataSnapshot>() {
                    int max = 5;
                    int cur = 0;

                    @Override
                    public Iterator<DataSnapshot> iterator() {
                        return new Iterator<DataSnapshot>() {
                            @Override
                            public boolean hasNext() {
                                return cur < max;
                            }

                            @Override
                            public DataSnapshot next() {
                                cur++;
                                return mockedSnap;
                            }
                        };
                    }
                };
                when(mockedSnap.hasChild(anyString())).thenReturn(true);
                when(mockedSnap.getChildren()).thenReturn(mockedIteDataSnap);

                valueEventListener.onDataChange(mockedSnap);
                return valueEventListener;
            }

        }).when(mockedDb).addListenerForSingleValueEvent(any(ValueEventListener.class));
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                User ret_obj = null;
                ret_obj =  mockedData.get(curRef);
                otherUser = ret_obj;
                return mockedData.get(curRef);
            }
        }).when(mockedSnap).getValue(User.class);

        fbUtil.readAllTableOnce(Tables.USERS, callback);

    }

    @Test
    public void listenObjChild(){
        ChatLogs chat = new ChatLogs();
        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFb);
        when(mockedFb.getReference(any(String.class))).thenReturn(mockedDb);

        when(mockedDb.child((String) Matchers.argThat(new ArgumentMatcher(){

            // Update current and print to console path to console
            @Override
            public boolean matches(Object argument) {
                curRef = (String) argument;
                return true;
            }

        }))).thenReturn(mockedDb);
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ChildEventListener valueEventListener = (ChildEventListener) invocation.getArguments()[0];
                Object ret_obj = mockedData.get(curRef);

                when(mockedSnap.getValue(Message.class)).thenReturn(new Message());
                when(mockedSnap.hasChild(anyString())).thenReturn(true);

                valueEventListener.onChildAdded(mockedSnap, "test");
                valueEventListener.onChildChanged(mockedSnap, "test");
                valueEventListener.onChildMoved(mockedSnap, "test");
                valueEventListener.onChildRemoved(mockedSnap);
                return valueEventListener;
            }

        }).when(mockedDb).addChildEventListener(any(ChildEventListener.class));



        when(mockedSnap.exists()).thenReturn(true);

        fbUtil.listenObjChild(chat, Tables.CHATLOGS, "message", Message.class, callback);

    }

    @Test
    public void stopListening(){
        ChatLogs chat = new ChatLogs();
        PowerMockito.mockStatic(FirebaseDatabase.class);
        when(FirebaseDatabase.getInstance()).thenReturn(mockedFb);
        when(mockedFb.getReference(any(String.class))).thenReturn(mockedDb);

        when(mockedDb.child((String) Matchers.argThat(new ArgumentMatcher(){

            // Update current and print to console path to console
            @Override
            public boolean matches(Object argument) {
                curRef = (String) argument;
                return true;
            }

        }))).thenReturn(mockedDb);

       fbUtil.stopListening("test", Tables.CHATLOGS);

    }

}
