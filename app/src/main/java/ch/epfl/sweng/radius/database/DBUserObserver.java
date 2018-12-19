/**
 * This file contains an interface that specifies the
 * behavior of a DBUserObserver, which is a class
 * used to observe, track the user related changes in
 * a database.
 * @author RADIUS
 * @version 1.0
 */

package ch.epfl.sweng.radius.database;

/**
 * An interface for DBUserObserver, a useful class to track
 * user updates in a database.
 */
public interface DBUserObserver {

    /**
     * The method to execute in case of a user related change.
     * @param id The id of DatabaseObject
     */
    void onUserChange(String id);

}
