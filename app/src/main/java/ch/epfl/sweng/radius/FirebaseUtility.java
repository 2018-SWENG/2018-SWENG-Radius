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
package ch.epfl.sweng.radius;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

    /**
     *
     * @param user User to listen/update
     */
    public FirebaseUtility(User user){

        // Instanciate References Object
        this.auth      = FirebaseAuth.getInstance();
        this.fireDB    = FirebaseDatabase.getInstance();
        this.uID       = user.getUserID();

        this.user      = user;
        this.database  = fireDB.getReference("users");
    }

    /**
     *
     * @param dataType Message listen/update
     */
    public FirebaseUtility(Message dataType){

        // Instanciate References Object
        this.auth       = FirebaseAuth.getInstance();
        this.fireDB     = FirebaseDatabase.getInstance();
        this.uID        = dataType.getMessageID();

        this.msg        = dataType;
        this.database   = fireDB.getReference("messages");

    }

    /**
     *
     * @param dataType ChatLogs to listen/update
     */
    public FirebaseUtility(ChatLogs dataType){

        // Instanciate References Object
        this.auth        = FirebaseAuth.getInstance();
        this.fireDB      = FirebaseDatabase.getInstance();
        this.uID         = dataType.getParticipants().get(0).getUserID();

        this.chatLogs    = dataType;
        this.database    = fireDB.getReference("chatlogs");
    }

    // TODO : #Salezer, must be fixed

    /**
     * Checks whether the current user is a new user. If that's the case, it creates a new user
     *      and pushes it to the database
     * @return Whether the user is new or not
     */
    public boolean isNew(){

        final boolean[] newb = new boolean[1];
        final boolean[] listenDone = {false};
        OnCompleteListener<AuthResult> completeListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                    Log.d("MyTAG", "onComplete: " + (isNew ? "new user" : "old user"));
                    newb[0] = isNew;
                    listenDone[0] = true;
                }
            }
        };
        while(!listenDone[0]);

        if(newb[0]){
            User user = new User(Long.parseLong(auth.getCurrentUser().getUid()));
                user.setNickname(auth.getCurrentUser().getDisplayName());
                user.setRadius(10);
                user.setStatus("Hey, I just arrived on Radius!");

            database.child(Long.toString(user.getUserID())).setValue(user);

        }


        return newb[0];
    }



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

        database.child(Long.toString(user.getUserID())).addListenerForSingleValueEvent(listener);

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


