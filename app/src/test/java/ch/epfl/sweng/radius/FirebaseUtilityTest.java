package ch.epfl.sweng.radius;

import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FirebaseUtilityTest {

    private FirebaseAuth auth;
    private FirebaseDatabase    firedb;
    private DatabaseReference   database;
    private FirebaseApp         fbApp;
    private String              listenerStr;
    private Integer             listenerInt;

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
}