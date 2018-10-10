package ch.epfl.sweng.radius;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FirebaseUtilityTest  {

    private FirebaseAuth auth;
    private FirebaseDatabase    firedb;
    private DatabaseReference   database;
    private FirebaseApp         fbApp;
    private String              listenerStr;
    private Integer             listenerInt;

    private CountDownLatch authSignal = null;

    @Before
    public void setUp() throws InterruptedException {
        authSignal = new CountDownLatch(1);
        Context cont = InstrumentationRegistry.getContext();

        fbApp = FirebaseApp.initializeApp(cont);

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null) {
            auth.signInWithEmailAndPassword("passuello.arthur@gmail.com", "3S-n1035*70").addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {

                            final AuthResult result = task.getResult();
                            final FirebaseUser user = result.getUser();
                            authSignal.countDown();
                        }
                    });
        } else {
            authSignal.countDown();
        }
        authSignal.await(10, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() throws Exception {
        if(auth != null) {
            auth.signOut();
            auth = null;
        }
    }

    @Test
    public void testWrite() throws InterruptedException {
        final CountDownLatch writeSignal = new CountDownLatch(1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Do you have data? You'll love Firebase. - 3")
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull final Task<Void> task) {
                        writeSignal.countDown();
                    }
                });

        writeSignal.await(10, TimeUnit.SECONDS);
    }
}
/*
    @Test
    public void checkNewUser() {
        // Try with other account
    }


    @Test
    public void writeToDB() {

        // Try writing to existing user
        database.child("arthur").child("nickname").setValue("Archie");

        firedb.getReference("users").child("arthur").child("nickname").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("FirebaseTest", "Data updated");

                String res = dataSnapshot.getValue(String.class);

                // update toolbar title
                assertEquals("Archie", res);
                }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("FirebaseTest", "Failed to read data.", error.toException());
            }
        });

        // Try writing to non-existing user
        database.child("arthur").child("status").setValue("Asleep");

        firedb.getReference("users").child("arthur").child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("FirebaseTest", "Data updated");

                String res = dataSnapshot.getValue(String.class);

                // update toolbar title
                assertEquals("Asleep", res);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("FirebaseTest", "Failed to read data.", error.toException());
            }
        });

    }

    @Test
    public void readFromDB() {
        // Try reading existing data
        firedb.getReference("users").child("arthur").child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("FirebaseTest", "Data updated");

                String res = dataSnapshot.getValue(String.class);

                // update toolbar title
                assertEquals("arthur", res);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("FirebaseTest", "Failed to read data.", error.toException());
            }
        });
        // Try reading non-existing data
        firedb.getReference("users").child("arthur").child("picture").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("FirebaseTest", "Data updated");

                String res = dataSnapshot.getValue(String.class);

                // update toolbar title
                assertNull(res);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("FirebaseTest", "Failed to read data.", error.toException());
            }
        });
    }

    @Test
    public void addStringListenerToDB() {
        // Initiate listener
        firedb.getReference("users").child("arthur").child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("FirebaseTest", "Data updated");

                listenerStr = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("FirebaseTest", "Failed to read data.", error.toException());
            }
        });

        // Modifiy field
        database.child("arthur").child("username").setValue("Archie");

        // Verify field was updated
        assertEquals("Archie", listenerStr);
    }

    @Test
    public void addIntListenerToDB() {
        // Initiate listener
        firedb.getReference("sweng-radius").child("arthur").child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("FirebaseTest", "Data updated");

                listenerInt = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("FirebaseTest", "Failed to read data.", error.toException());
            }
        });

        // Modifiy field
        database.child("arthur").child("age").setValue("98");

        // Verify field was updatedi
        assertEquals(98, listenerInt.intValue());
    }

    @Before
    public void setUp() throws Exception {

        firedb = FirebaseDatabase.getInstance("https://sweng-radius.firebaseio.com");
        database = firedb.getReference("sweng-radius");

        // Will be added when testing auth features
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            return;
        } else {
            // TODO Authenticate with hard-coded credentials

            return;
        }
    }

    @After
    public void tearDown() throws Exception {
        // Will be added when testing auth features
        //auth.getInstance().signOut()
    }

    */
