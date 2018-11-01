package ch.epfl.sweng.radius.database;

import com.google.firebase.database.DatabaseError;

public interface CallBackDatabase<DatabaseObject> {
    void onFinish(DatabaseObject value);
    void onError(DatabaseError error);
}
