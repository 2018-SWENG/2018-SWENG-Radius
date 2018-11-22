package ch.epfl.sweng.radius.database;

public interface DBUserObserver {
    void onUserChange(String id);
}
