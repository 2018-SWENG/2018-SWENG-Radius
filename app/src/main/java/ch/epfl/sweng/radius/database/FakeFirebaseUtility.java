package ch.epfl.sweng.radius.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FakeFirebaseUtility extends Database {
    private User currentUSer;
    private HashMap<String, User> usersTable = new HashMap<>();
    private HashMap<String, ChatLogs> chatLogsTable = new HashMap<>();

    public FakeFirebaseUtility(){
        fillDatabase();
    }

    @Override
    public String getCurrent_user_id() {
        return currentUSer.getID();
    }

    @Override
    public void readObjOnce(final DatabaseObject obj,
                            final Tables tableName,
                            final CallBackDatabase callback) {
        DatabaseObject objRead = null;
        switch (tableName){
            case USERS:
                objRead = usersTable.get(obj.getID());
                break;
            case CHATLOGS:
                objRead = chatLogsTable.get(obj.getID());
                break;
        }

        if (objRead == null){
            writeInstanceObj(obj, tableName);
            callback.onFinish(obj);
        }
        else
            callback.onFinish(objRead);

    }

    @Override
    public void readObj(final DatabaseObject obj,
                        final Tables tableName,
                        final CallBackDatabase callback) {
    }

    @Override
    public void readListObjOnce(final List<String> ids,
                            final Tables tableName,
                            final CallBackDatabase callback) {
        ArrayList<DatabaseObject> objsRead = new ArrayList<>();
        switch (tableName){
            case USERS:
                for (String id:ids) {
                    User objRead = usersTable.get(id);
                    if(objRead != null)
                        objsRead.add(objRead);
                }
                break;
            case CHATLOGS:
                for (String id:ids) {
                    ChatLogs objRead = chatLogsTable.get(id);
                    if(objRead != null)
                        objsRead.add(objRead);
                }
                break;
        }

        callback.onFinish(objsRead);
    }

    @Override
    public void writeInstanceObj(final DatabaseObject obj, final Tables tableName){
        switch (tableName){
            case USERS:
                usersTable.put(obj.getID(), (User) obj);
                break;
            case CHATLOGS:
                chatLogsTable.put(obj.getID(), (ChatLogs) obj);
                break;
        }
    }

    private void fillDatabase(){
        // Define Current user
        currentUSer = new User("testUser1");

        // Fill the users table
        usersTable.put("testUser1", currentUSer);
        usersTable.put("testUser2", new User("testUser2"));
        usersTable.put("testUser3", new User("testUser3"));
        usersTable.put("testUser4", new User("testUser4"));

        // TODO: Fill the chatLogs table
    }
}
