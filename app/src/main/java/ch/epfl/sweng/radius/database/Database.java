/**
 * This file contains a Singleton Class to use Firebase or FakeFirebase
 * in test cases.
 * @author RADIUS
 * @version 1.0
 */

package ch.epfl.sweng.radius.database;

import android.support.v4.util.Pair;

import java.util.List;

/**
 * Singleton Class to use Firebase or FakeFirebase in case of testing.
 */
public abstract class Database {

    // The database singleton instance
    private static Database database = null;
    public static boolean DEBUG_MODE = false;

    // The tables we can access in the db
    public enum Tables{
        USERS("users", User.class),
        CHATLOGS("chatlogs", ChatLogs.class),
        LOCATIONS("locations", MLocation.class);

        private String name = "";
        private Class tableClass;

        //Constructor
        Tables(String name, Class tableClass){
            this.name = name;
            this.tableClass = tableClass;
        }

        public String toString(){
            return name;
        }

        public Class getTableClass(){
            return tableClass;
        }
    }

    /**
     * Modify the singleton instance of the DBUtility, with a FakeDatabase instance
     * Call this method only for testing purpose.
     */
    public static boolean activateDebugMode(){
        DEBUG_MODE = true;
        database = new FakeFirebaseUtility();
        return false;
    }

    /**
     * Clears the database by setting the particular instance to null.
     */
    public void clearDatabase(){
        database = null;
    }

    /**
     * Gets the singleton instance of the DBUtility
     * @return The singleton instance of the DBUtility
     */
    public static Database getInstance(){
        if(database == null)
            database = new FirebaseUtility();
        return database;
    }

    /**
     * Gets the current user id.
     * @return The current user id
     */
    public abstract String getCurrent_user_id();

    /**
     * Read an object, with the same id as obj, in the table "tableName" of the DB
     * and call the functions of callback once when done.
     * @param obj the list of ids of the objects we want to retrieve
     * @param tableName the name of the table we want to read
     * @param callback the functions we want to call when the reading is complete
     */
    public abstract void readObjOnce(final DatabaseObject obj,
                                     final Tables tableName,
                                     final CallBackDatabase callback);

    /**
     * Read an object, with the same id as obj, in the table "tableName" of the DB
     * and call the functions of callback each time the specified object is updated.
     * @param obj the list of ids of the objects we want to retrieve
     * @param tableName the name of the table we want to read
     * @param callback the functions we want to call when the reading is complete
     */
    public abstract void readObj(final DatabaseObject obj,
                                 final Tables tableName,
                                 final CallBackDatabase callback);

    /**
     * Read a list of objects with the ids mentioned in the table "tableName" of the DB
     * and call the functions of callback when done
     * @param ids the list of ids of the objects we want to retrieve
     * @param tableName the name of the table we want to read
     * @param callback the functions we want to call when the reading is complete
     */
    public abstract void readListObjOnce(final List<String> ids,
                            final Tables tableName,
                            final CallBackDatabase callback);

    /**
     * Read all the objects in the table "tableName" of the DB
     * and call the functions of callback when done
     * @param tableName the name of the table we want to read
     * @param callback the functions we want to call when the reading is complete
     */
    public abstract void readAllTableOnce(final Tables tableName,
                                         final CallBackDatabase callback);

    /**
     * Write/Update obj in the table mentioned of the DB
     * @param obj the obj to write in the DB
     * @param tableName the name of the table in which we want to store obj
     */
    public abstract void writeInstanceObj(final DatabaseObject obj, Tables tableName);


    /**
     * Append a child object to the object in the table mentioned in the DB
     * @param obj The obj to write in the DB
     * @param tablename The name of the table in which we want to store obj
     * @param childName The name of the child object to append
     * @param child The child object itself
     */
    public abstract void writeToInstanceChild(final DatabaseObject obj, Tables tablename,
                                              final String childName, final Object child);

    /**
     * Retrieve, listen a child object of the object in the table mentioned in the DB
     * @param obj The obj whose child will be listened.
     * @param tablename The name of the table in which we store obj
     * @param childName The name of the child object to listen
     * @param child The child object itself
     */
    public abstract void listenObjChild(final DatabaseObject obj,
                               final Tables tableName,
                               final Pair<String, Class> child,
                               final CallBackDatabase callback);

}
