/**
 * This file contains an interface that specifies the
 * behavior of a DBLocationObserver, which is a class
 * used to observe, track the location related changes in
 * a database.
 * @author RADIUS
 * @version 1.0
 */
package ch.epfl.sweng.radius.database;

/**
 * An interface for DBLocationObserver, a useful class to track
 * location updates in a database.
 */
public interface DBLocationObserver {

    /**
     * The method to execute in case of a location change.
     * @param id The id of DatabaseObject
     */
    void onLocationChange(String id);

}
