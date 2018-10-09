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


class FirebaseUtility {

    private FirebaseDatabase fireDB;
    private FirebaseAuth     auth;
    private FirebaseUser     user;

    private DatabaseReference   database;

    private String           uID;
    private String           retBuffer;

    // TODO Check if user is logged in by checking userID is not NULL
    // TODO Include authentication into constructor so all Firebase operations are done within here
    public FirebaseUtility(){

        // TODO : Either move it into separate method or first auth user
        // Instanciate References Object
        auth        = FirebaseAuth.getInstance();
        fireDB      = FirebaseDatabase.getInstance();
        user        = auth.getCurrentUser();
        database    = fireDB.getReference();
        uID         = user.getUid();

    }

    /**
     * Method to check whether the current authentication is the first one
     *  in order to create a new user profile in the DB if need be
     */
    public boolean checkNewUser(){
        FirebaseUserMetadata metadata = auth.getCurrentUser().getMetadata();
        if (metadata.getCreationTimestamp() == metadata.getLastSignInTimestamp()) {
            // TODO : Push new default profile entry to DB and go to profile Activity
            return true;
        } else {
            // TODO : Goto home activity
            return false;
        }
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

        database.child(table).child(uID).child(field).setValue(value);
    }


    /**
     * Reads single value in provided table and field
     * @param table Database "table" in which to read an element
     * @param field Field to read
     * @return String representation of the object read in the database
     */
    public String readFromDB(String table, String field){

        ValueEventListener  listener;

        listener = new ValueEventListener() {
            @Override
            public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
                retBuffer = dataSnapshot.getValue().toString();

                Log.e("Firebase", "User data has been read : " + retBuffer);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to read user", databaseError.toException());

            }
        };

        database.addListenerForSingleValueEvent(listener);

        return retBuffer;

    }


    // TODO : When table ready, only one method for whole class
    public void addStringListenerToDB(String table, final String field, final TextView view){

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String val = dataSnapshot.getValue(String.class);

                if (val == null){
                    Log.e("Firebase", "User data is null!");
                    return;
                }

                view.setText(val);

                Log.e("Firebase", "User data is changed : " + field);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Unable to read user data.", databaseError.toException());

            }
        };

    }

    public void addIntListenerToDB(String table, final String field, final TextView view){

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Integer val = dataSnapshot.getValue(Integer.class);

                if (val == null){
                    Log.e("Firebase", "User data is null!");
                    return;
                }

                view.setText(val);

                Log.e("Firebase", "User data is changed : " + field);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Unable to read user data.", databaseError.toException());

            }
        };

    }

}


