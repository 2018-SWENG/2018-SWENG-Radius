package ch.epfl.sweng.radius.database;

import com.google.firebase.database.DatabaseError;

public interface CallBackDatabase2<T> extends CallBackDatabase<T>{
    void onFinish(T value);
    void onError(DatabaseError error);

    void onFinish(T value, String s);
}
