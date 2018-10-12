package ch.epfl.sweng.radius;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;

class FirebaseUtility {

    private FirebaseDatabase fireDB;
    private FirebaseAuth     auth;

    private DatabaseReference   database;

    private Long        uID;
    private User        user;
    private Message     msg;
    private ChatLogs    chatLogs;

    // TODO Check if user is logged in by checking userID is not NULL
    // TODO Include authentication into constructor so all Firebase operations are done within here
    public FirebaseUtility(User user){

        // Instanciate References Object
        this.auth      = FirebaseAuth.getInstance();
        this.fireDB    = FirebaseDatabase.getInstance();
        this.uID       = user.getUserID();

        this.user      = user;
        this.database  = fireDB.getReference("users");
    }

    public FirebaseUtility(Message dataType){

        // Instanciate References Object
        this.auth       = FirebaseAuth.getInstance();
        this.fireDB     = FirebaseDatabase.getInstance();
        this.uID        = dataType.getMessageID();

        this.msg        = dataType;
        this.database   = fireDB.getReference("messages")

    }

    public FirebaseUtility(ChatLogs dataType){

        // Instanciate References Object
        this.auth        = FirebaseAuth.getInstance();
        this.fireDB      = FirebaseDatabase.getInstance();
        this.uID         = dataType.getParticipants().get(0).getUserID();

        this.chatLogs    = dataType;
        this.database    = fireDB.getReference("chatlogs");
    }

    /*
    TODO : When userProfile class ready, add method to create whole userProfile
    entry based on UID
    */

    /**
     * Writes single value in provided table and field
     * @param table Database "table" in which to modify an element
     * @param field Field to modify
     * @param value Value to write -- Must correspond to field type
     */
    public void writeToDB(String table, String field, Object value){

        database.child(table).child(Long.toString(uID)).child(field).setValue(value);
    }


    /**
     * Reads single value in provided table and field
     * @param table Database "table" in which to read an element
     * @param field Field to read
     * @return String representation of the object read in the database
     */
    public void listenUser(){

        ValueEventListener  listener;

        listener = new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                Log.e("Firebase", "User data has been read.");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read user", databaseError.toException());

            }
        };

        database.addListenerForSingleValueEvent(listener);

        return;

    }

    public void writeUser(){

        database.child(Long.toString(user.getUserID())).setValue(user);

        return;

    }

    public void writeUser(User new_user){

        database.child(Long.toString(new_user.getUserID())).setValue(new_user);

        return;

    }

    public void listenMessage(){

        ValueEventListener  listener;

        listener = new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                msg = dataSnapshot.getValue(Message.class);

                Log.e("Firebase", "Message data has been read.");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read message", databaseError.toException());

            }
        };

        database.addListenerForSingleValueEvent(listener);

        return;

    }

    public void writeMessage(){

        database.child(Long.toString(msg.getMessageID())).setValue(msg);

        return;

    }

    public void writeMessage(Message new_msg){

        database.child(Long.toString(new_msg.getMessageID())).setValue(new_msg);

        return;

    }

    public void listenChatLogs(){

        ValueEventListener  listener;

        listener = new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatLogs = dataSnapshot.getValue(ChatLogs.class);

                Log.e("Firebase", "Chatlogs data has been read.");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read Chatlogs", databaseError.toException());

            }
        };

        database.addListenerForSingleValueEvent(listener);

        return;

    }

    public void writeChatLogs(){

        database.child(Long.toString(msg.getMessageID())).setValue(msg);

        return;

    }

    public void writeChatLogs(ChatLogs new_chatlogs){
        database.child(Long.toString(new_chatlogs.getParticipants().get(0).getUserID()))
                .setValue(new_chatlogs);

        return;

    }




}


