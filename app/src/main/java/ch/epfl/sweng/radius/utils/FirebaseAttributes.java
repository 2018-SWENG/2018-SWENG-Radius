package ch.epfl.sweng.radius.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseAttributes {

    private FirebaseDatabase fireDB;
    private FirebaseAuth        auth;
    private DatabaseReference database;

    public FirebaseAttributes(FirebaseDatabase fireDB, FirebaseAuth auth, DatabaseReference database){
        this.fireDB     = fireDB;
        this.auth       = auth;
        this.database   = database;
    }

    public DatabaseReference getDatabase() {
        return database;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseDatabase getFireDB() {
        return fireDB;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }

    public void setDatabase(DatabaseReference database) {
        this.database = database;
    }

    public void setFireDB(FirebaseDatabase fireDB) {
        this.fireDB = fireDB;
    }
}

