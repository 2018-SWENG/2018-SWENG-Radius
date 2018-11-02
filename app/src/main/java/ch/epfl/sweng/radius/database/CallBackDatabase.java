package ch.epfl.sweng.radius.database;

import com.google.firebase.database.DatabaseError;

public interface CallBackDatabase<T> {
    void onFinish(T value);
    void onError(DatabaseError error);
}
