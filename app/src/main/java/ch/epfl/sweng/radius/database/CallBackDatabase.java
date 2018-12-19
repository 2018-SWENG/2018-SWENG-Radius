/**
 * This file contains an interface to specify the behavior of
 * a database callback.
 * @author RADIUS
 * @version 1.0
 */
package ch.epfl.sweng.radius.database;

import com.google.firebase.database.DatabaseError;

/**
 * An interface to specify the functionality that a database
 * callback provides.
 */
public interface CallBackDatabase<T> {

    /**
     * The specific functionality that a callback should
     * implement after a value is successfully obtained from
     * the database.
     * @param value The value obtained from the database
     */
    void onFinish(T value);

    /**
     * The specific functionality that a callback should
     * implement in the case that a problem occurs during
     * the database access
     * @param error The particular kind of error happened
     *              during the database access.
     */
    void onError(DatabaseError error);

}


