package ch.epfl.sweng.radius.database;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.Field;

public class FakeFirebaseUtility extends Database {
    private User currentUSer;
    private MLocation currentLoc;
    private HashMap<String, User> usersTable = new HashMap<>();
    private HashMap<String, ChatLogs> chatLogsTable = new HashMap<>();
    private HashMap<String, MLocation> locationsTable = new HashMap<>();

    private final double defaultLat = 46.5360698;
    private final double defaultLng = 6.5681216000000004;


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
            case LOCATIONS:
                objRead = locationsTable.get((obj.getID()));
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
                        final CallBackDatabase callback,
                        String listenerID) {
        HashMap<String, DatabaseObject> table = getTable(tableName);

        DatabaseObject ret = table.get(obj.getID());
        callback.onFinish(ret);
    }

    private ChatLogs getChat(){
        ChatLogs chat = new ChatLogs("1");
        chat.addMembersId("usertTest1");
        chat.addMembersId("usertTest2");
        chat.addMembersId("usertTest0");
        chat.addMessage(new Message("usertTest0", "helo", new Date()));
        chat.addMessage(new Message("usertTest1", "aaa", new Date()));
        chat.addMessage(new Message("as", "aaa", new Date()));
        chatLogsTable.put("1", chat);
        return chat;
    }

    @Override
    public void readListObjOnce(final List<String> ids,
                            final Tables tableName,
                            final CallBackDatabase callback) {
        ArrayList<DatabaseObject> objsRead = new ArrayList<>();

        HashMap<String, DatabaseObject> table = getTable(tableName);

        for (String id:ids) {
            DatabaseObject objRead = table.get(id);

            if(objRead != null)
                objsRead.add(objRead);
        }

        if(objsRead.isEmpty())
            objsRead.add(getNewEl(tableName));

        callback.onFinish(objsRead);
    }

    private DatabaseObject getNewEl(Tables tableName) {

        DatabaseObject ret = null;
        switch (tableName){
            case LOCATIONS:
                ret = new MLocation();
                break;
            case CHATLOGS:
                ret = getChat();
                break;
            case USERS:
                ret = new User();
                break;

        }
        return ret;
    }

    @Override
    public void readAllTableOnce(Tables tableName, CallBackDatabase callback) {

        HashMap<String, DatabaseObject> table = getTable(tableName);
        ArrayList<DatabaseObject> objsRead = new ArrayList<DatabaseObject>(table.values());

        Log.w("Map Test", "Size of objReads " + objsRead.size());

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
            case LOCATIONS:
                locationsTable.put(obj.getID(), (MLocation) obj);
                break;
        }
    }

    @Override
    public void stopListening(String listenerID, Tables tableName) {

    }

    @Override
    public void listenObjChild(DatabaseObject obj, Tables tableName, Pair<String, Class> child, CallBackDatabase callback) {
        HashMap<String, DatabaseObject> table = getTable(tableName);
        Field f1 = null;
        DatabaseObject ret = table.get(obj.getID());
        try {
            f1 = ret.getClass().getField(child.first);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        // To fix if used elsewhere
        callback.onFinish(new Message());
    }


    public void fillDatabase(){
        if(currentUSer != null) return;
        // Define Current user
        currentUSer = new User("testUser1");

        // Fill the users table
        usersTable.put("testUser1", currentUSer);
        usersTable.put("testUser2", new User("testUser2"));
        usersTable.put("testUser3", new User("testUser3"));
        usersTable.put("testUser4", new User("testUser4"));

        usersTable.get("testUser1").addChat("testUser2", "chatid1234");
        usersTable.get("testUser1").addChat("testUser3", "chatid1234");

        // TODO: Fill the chatLogs table
        currentLoc = new MLocation("testUser1", defaultLng, defaultLat);
        fillLocationsTable();

        ChatLogs chat = new ChatLogs("0");
        ArrayList<String> users = new ArrayList<String>();
        chat.addMembersId("usertTest1");
        chat.addMembersId("testUser3");
        chat.addMessage(new Message("usertTest1", "fff", new Date()));
        chat.addMessage(new Message("usertTest2", "aaa", new Date()));
        chat.addMessage(new Message("testUser1", "aaa", new Date()));
        chat.addMessage(new Message("as", "aaa", new Date()));
        chatLogsTable.put("0", chat);
    }

    private void fillLocationsTable(){
        locationsTable.put("testUser1", currentLoc);
        locationsTable.put("testUser2", new MLocation("testUser2", defaultLng + 0.01,
                defaultLat + 0.01));
        locationsTable.put("testUser3", new MLocation("testUser3", defaultLng - 0.02,
                defaultLat + 0.02));
        locationsTable.put("testUser4", new MLocation("testUser4",
                defaultLng - 0.01, defaultLat - 0.01));

        // Fill the group locations
        MLocation EPFL = new MLocation("EPFL",
                defaultLng + 0.5,
                defaultLat - 0.5);
        EPFL.setIsGroupLocation(1); // set EPFL as group location
        locationsTable.put(EPFL.getID(), EPFL);
        MLocation UNIL = new MLocation("UNIL",
                defaultLng + 1.5,
                defaultLat - 1.5);
        UNIL.setIsGroupLocation(1); // set UNIL as group location
        locationsTable.put(UNIL.getID(), UNIL);
    }

    private HashMap<String,DatabaseObject> getTable(Tables tableName){

        return (HashMap<String, DatabaseObject>) (tableName == Tables.USERS ? usersTable :
                        tableName == Tables.CHATLOGS ? chatLogsTable : locationsTable);
    }
    @Override
    public  void writeToInstanceChild(final DatabaseObject obj, Tables tablename,
                                              final String childName, final Object child){

    }
}
