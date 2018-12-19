/**
 * This file contains the interface that specifies the
 * behavior of a DatabaseObject.
 * @author RADIUS
 * @version 1.0
 */

package ch.epfl.sweng.radius.database;

/**
 * The interface specifying the functionality of a DatabaseObject
 */
public interface DatabaseObject {

    /**
     * Retrieves the id of a DatabaseObject, each DatabaseObject must have one.
     * @return The id of the particular instance of DatabaseObject
     */
    String getID();

}
