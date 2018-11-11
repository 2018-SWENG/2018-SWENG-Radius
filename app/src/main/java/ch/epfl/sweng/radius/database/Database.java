package ch.epfl.sweng.radius.database;

import java.util.List;

/**
 * Singleton Class to use Firebase or FakeFirebase in case of testing
 */
public abstract class Database {
    // The database singleton instance
    static Database database = null;

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
    public static void activateDebugMode(){
        database = new FakeFirebaseUtility();
    }

    /**
     * Get the singleton instance of the DBUtility
     * @return the singleton instance of the DBUtility
     */
    public static Database getInstance(){
        if(database == null)
            return new FirebaseUtility();
        else
            return database;
    }

    /**
     * Get the current user id.
     * @return the current user id
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


}
