/** Represents an interface to communicate with the Firebase Realtime Database
 * @usage : 1 . Instantiate class by providing the table we need to access in the DB
 *              ("user", "message", ...)
 *          2 . Read objects of this table by providing an object with the id we want to match.
 *          3 . Write/Update by providing the object we need to store
 */
package ch.epfl.sweng.radius.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import ch.epfl.sweng.radius.database.DatabaseObject;

public class FirebaseUtility {
    private final DatabaseReference   database;
    private final String current_user_id;

    public FirebaseUtility(String tableName){
        this.current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.database = FirebaseDatabase.getInstance().getReference(tableName);
    }


    public String getCurrent_user_id() {
        return current_user_id;
    }

    /**
     * Read the DatabaseObject with which we instantiate FirebaseUtility
     * and call the OnFinish function of the callback.
     * @param callback
     */
    public void readObjOnce(final DatabaseObject obj, final CallBackDatabase callback) {
        database.child(obj.getID()).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void  onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    writeInstanceObj(obj);
                    callback.onFinish(obj);
                }
                else
                    callback.onFinish(dataSnapshot.getValue(obj.getClass()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    /**
     * Read the DatabaseObject obj in the table with which we instantiate FirebaseUtility
     * each time it's value change and call the OnFinish function of the callback.
     * @param callback
     */
    public void readObj(final DatabaseObject obj, final CallBackDatabase callback) {
        database.child(obj.getID()).addValueEventListener( new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(obj.getID())) {
                    writeInstanceObj(obj);
                    callback.onFinish(obj);
                }
                else
                    callback.onFinish(dataSnapshot.getValue(obj.getClass()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    public void readListObj(final List<String> ids,
                            final Class c,
                            final CallBackDatabase callback) {
        database.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DatabaseObject> allItems = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    DatabaseObject snap = (DatabaseObject)postSnapshot.getValue(c);
                    if (ids.contains(snap.getID())) {
                        allItems.add((DatabaseObject)postSnapshot.getValue(c));
                    }
                }
                callback.onFinish(allItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);

            }
        });
    }

    /**
     * Write/Update obj in the DB
     * @param obj
     */
    public void writeInstanceObj(final DatabaseObject obj){
        database.child(obj.getID()).setValue(obj);
    }
}


