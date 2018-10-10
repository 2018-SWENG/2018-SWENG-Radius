package ch.epfl.sweng.radius;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


import static org.junit.Assert.*;

public class FirebaseUtilityTest {

  //  private FirebaseAuth auth;
    private FirebaseDatabase firedb;
    private DatabaseReference database;
    /*
    @Test
    public void checkNewUser() {
        // Try with other account
    }
    */

    @Test
    public void writeToDB() {
        // Try writing to existing user
        database.child("arthur").child("nickname").setValue("Archie");

        firedb.getReference("users").child("arthur").child("nickname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("FirebaseTest", "Data updated");

                String res = dataSnapshot.getValue(String.class);

                // update toolbar title
                assertEquals(res, "Archie");
                }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("FirebaseTest", "Failed to read data.", error.toException());
            }
        });

        // Try writing to non-existing user
        database.child("arthur").child("status").setValue("Asleep");

        firedb.getReference("users").child("arthur").child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("FirebaseTest", "Data updated");

                String res = dataSnapshot.getValue(String.class);

                // update toolbar title
                assertEquals(res, "Asleep");
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
        // Try reading to existing data
        database.child("arthur")
        // Try reading to non-existing data
    }

    @Test
    public void addStringListenerToDB() {
        // Try writing to String field

        // Try writing to non-String field
    }

    @Test
    public void addIntListenerToDB() {
        // Try writing to int field

        // Try writing to non-int field
    }

    @Before
    public void setUp() throws Exception {

        firedb = FirebaseDatabase.getInstance();
        database = firedb.getReference("users");

        // Will be added when testing auth features
     /*   auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            return;
        } else {
            // TODO Authenticate with hard-coded credentials

            return;
        }*/
    }

    @After
    public void tearDown() throws Exception {
        // Will be added when testing auth features
        //auth.getInstance().signOut()
    }


}