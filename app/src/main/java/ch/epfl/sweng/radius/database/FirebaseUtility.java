/** Represents an interface to communicate with the Firebase Realtime Database
 * @usage : 1 . Instantiate class by providing the table we need to access in the DB
 *              ("user", "message", ...)
 *          2 . Read objects of this table by providing an object with the id we want to match.
 *          3 . Write/Update by providing the object we need to store
 */
package ch.epfl.sweng.radius.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import com.google.common.collect.Table;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseUtility extends Database{

    private static HashMap<String, ValueEventListener> listeners = new HashMap<>();

    public FirebaseUtility(){}

    @Override
    public String getCurrent_user_id() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    @Override
    public void writeToInstanceChild(final DatabaseObject obj, Tables tablename,
                                              final String childName, final Object child){
        FirebaseDatabase.getInstance()
                .getReference(tablename.toString())
                .child(obj.getID())
                .child(childName)
                .setValue(child);
    }

    @Override
    public void listenObjChild(final DatabaseObject obj,
                               final Tables tableName,
                               final Pair<String, Class> child,
                               final CallBackDatabase callback) {

        FirebaseDatabase.getInstance()
                .getReference(tableName.toString())
                .child(obj.getID())
                .child(child.first)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Log.e("message", "New child !");
                            callback.onFinish(dataSnapshot.getValue(child.second));
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Log.e("Firebase", "Child Changed !");
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onError(databaseError);

                    }
                });
    }



    @Override
    public void readObjOnce(final DatabaseObject obj,
                            final Tables tableName,
                            final CallBackDatabase callback) {
        FirebaseDatabase.getInstance()
                .getReference(tableName.toString())
                .child(obj.getID())
                .addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void  onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {
                    writeInstanceObj(obj, tableName);
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

    @Override
    public void readObj(final DatabaseObject obj,
                        final Tables tableName,
                        final CallBackDatabase callback,
                        String listenerID) {

  //      Log.w("Firebase Message", "Read " + obj.getClass() + "Called by "+ getLogTagWithMethod());

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    writeInstanceObj(obj, tableName);
                    callback.onFinish(obj);
                }
                else
                    callback.onFinish(dataSnapshot.getValue(obj.getClass()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        };

        listeners.put(listenerID, listener);

        FirebaseDatabase.getInstance()
                .getReference(tableName.toString())
                .child(obj.getID())
                .addValueEventListener(listener);
    }

    @Override
    public void readListObjOnce(final List<String> ids,
                            final Tables tableName,
                            final CallBackDatabase callback) {
        FirebaseDatabase.getInstance()
                .getReference(tableName.toString())
                .addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<DatabaseObject> allItems = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    DatabaseObject snap = (DatabaseObject)postSnapshot
                            .getValue(tableName.getTableClass());
                    if (ids.contains(snap.getID())) {
                        allItems.add((DatabaseObject)postSnapshot
                                .getValue(tableName.getTableClass()));
                    }
                    Log.e("PeopleTab", "allItems users size :" + allItems.size());

                }
                callback.onFinish(allItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError);

            }
        });
    }

    @Override
    public void readAllTableOnce(final Tables tableName, final CallBackDatabase callback) {
        FirebaseDatabase.getInstance()
                .getReference(tableName.toString())
                .addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<DatabaseObject> allItems = new ArrayList<>();
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            DatabaseObject snap = (DatabaseObject)postSnapshot
                                    .getValue(tableName.getTableClass());
                                allItems.add((DatabaseObject)postSnapshot
                                        .getValue(tableName.getTableClass()));
                        }
                        callback.onFinish(allItems);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onError(databaseError);

                    }
                });
    }
/*
    public void listenToWholeTable(final DatabaseObject obj,
                        final Tables tableName,
                        final CallBackDatabase callback) {
        FirebaseDatabase.getInstance()
                .getReference(tableName.toString())
                .child(obj.getID())
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChild(obj.getID())) {
                            writeInstanceObj(obj, tableName);
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
    */

    @Override
    public void writeInstanceObj(final DatabaseObject obj, final Tables tableName){

   //     Log.w("Firebase Message", "Called for " + obj.getID());
        if(obj.getClass() == ChatLogs.class) {
            ChatLogs test = (ChatLogs) obj;
  //          Log.w("Firebase Message", "Called for " + ((ChatLogs) obj).getMessages().size() +getLogTagWithMethod());
        }
        Log.e( "writeInstance", "moveCamerafetchh: ");
        FirebaseDatabase.getInstance()
                .getReference(tableName.toString())
                .child(obj.getID()).setValue(obj);
    }


    @Override
    public void stopListening(String listenerID, final Tables tableName){
            FirebaseDatabase.getInstance()
                    .getReference(tableName.toString())
                    .removeEventListener(listeners.get(listenerID));

            listeners.remove(listenerID);
    }

    /*
    private String getLogTagWithMethod() {

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        return trace[3].getClassName() + "." + trace[3].getMethodName() + ":" + trace[3].getLineNumber() + "\n" +
                trace[4].getClassName() + "." + trace[4].getMethodName() + ":" + trace[4].getLineNumber() + "\n" +
                trace[5].getClassName() + "." + trace[5].getMethodName() + ":" + trace[5].getLineNumber() + "\n" ;
    }
*/
}
