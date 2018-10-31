/** Represents an interface to communicate with the Firebase Realtime Database
 * @usage : 1 . Instantiate class by providing the object to be exchanged with the database
 *          2 . Add a listener to this object by calling listen[User, Message, ChatLogs]
 *          3 . Update the value on the database by first updating the object and then call
 *                  write[User, Message, ChatLogs]
 *              Write a new value in the database by calling
 *                  write[User, Message, ChatLogs]([User,Message, ChatLogs] new_value)
 * TODO : Add methods to add more than one listener for each instance
 * TODO : Add method to check if user is new
 */
package ch.epfl.sweng.radius.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.data.DataBuffer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.DatabaseObject;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;

public class FirebaseUtility {

    private FirebaseDatabase    fireDB;
    private FirebaseAuth        auth;
    private DatabaseReference   database;

    private DatabaseObject  obj;

    public FirebaseUtility(DatabaseObject obj, String ref){
        this.auth       = FirebaseAuth.getInstance();
        this.fireDB     = FirebaseDatabase.getInstance();

        this.obj = obj;
        this.database = fireDB.getReference(ref);
    }

    // TODO : #Salezer, must be fixed
    public boolean isNew(){
        final AtomicBoolean done = new AtomicBoolean(false);
        final AtomicInteger message1 = new AtomicInteger(0);
        final boolean[] newUser = new boolean[1];
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(obj.getID())) {
                    System.out.println("is not new");

                    newUser[0] = false;
                }
                else
                    newUser[0] = true;

                done.set(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        while (!done.get());
        return newUser[0];
    }

    public DatabaseObject readObj() throws InterruptedException {
        final AtomicBoolean done = new AtomicBoolean(false);
        final AtomicInteger message1 = new AtomicInteger(0);
        database.child(obj.getID()).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Firebase", "Wait for Read done");

                obj = dataSnapshot.getValue(obj.getClass());
                Log.e("Firebase", "Read done");
                done.set(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read user", databaseError.toException());

            }
        });
  //      semaphore.acquire();
        while (!done.get());
        return obj;
    }

    /*
        Do not use this method to read instantly used values ! Might lead to NullPointerException
                Use readObj() instead
     */
    public void listenInstanceObject() throws InterruptedException {

        database.child(obj.getID()).addValueEventListener( new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                obj = dataSnapshot.getValue(obj.getClass());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read user", databaseError.toException());

            }
        });

    }

    public DatabaseObject readOtherObject(String otherObjID) throws InterruptedException {
        final AtomicBoolean done = new AtomicBoolean(false);
        final AtomicInteger message1 = new AtomicInteger(0);

        final DatabaseObject[] ret = new DatabaseObject[1];
        database.child(otherObjID).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ret[0] = dataSnapshot.getValue(obj.getClass());
                done.set(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Firebase", "Failed to read object", databaseError.toException());

            }
        });
        while (!done.get());

        return ret[0];
    }

    public void writeInstanceObj(){ database.child(obj.getID()).setValue(obj); }

    public void writeOtherObj(DatabaseObject otherObj){ database.child(otherObj.getID()).setValue(otherObj);    }

    public DatabaseObject getInstance(){ return this.obj;    }

    public void setInstance(DatabaseObject new_obj){

        if(new_obj.getClass().equals(this.obj.getClass())) this.obj = new_obj;

    }
}


