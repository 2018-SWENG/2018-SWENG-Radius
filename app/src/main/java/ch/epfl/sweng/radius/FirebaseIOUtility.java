package ch.epfl.sweng.radius;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class FirebaseIOUtility {

    private FirebaseDatabase database;

    private DatabaseReference[] myRefs;

    public FirebaseIOUtility(String [] references){

        // Instanciate References Object
        database    = FirebaseDatabase.getInstance();
        myRefs      = new DatabaseReference[references.length];


        // Fill fields reference array
        for (int i = 0; i < references.length; i++){
            DatabaseReference ref = database.getReference(references[i]);
            myRefs[i] = ref;
        }
    }
}


