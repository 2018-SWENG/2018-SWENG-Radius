package ch.epfl.sweng.radius.database;

import android.util.Log;
import android.util.Pair;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
                        final CallBackDatabase callback) {
        HashMap<String, DatabaseObject> table = getTable(tableName);

        DatabaseObject ret = table.get(obj.getID());
        callback.onFinish(ret);
    }

    private ChatLogs getChat(){
        ChatLogs chat = new ChatLogs("13");
        chat.addMembersId("testUser1");
        chat.addMembersId("testUser4");
        chat.addMembersId("testUser3");
        chat.addMessage(new Message("testUser3", "helo", new Date()));
        chat.addMessage(new Message("testUser1", "aaa", new Date()));
        chat.addMessage(new Message("testUser4", "aaa", new Date()));
        chatLogsTable.put("13", chat);
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
                ret = new MLocation("testUser2"); break;
            case CHATLOGS:
                ret = getChat(); break;
            case USERS:
                ret = new User(); break;

        }
        return ret;
    }

    public void printDBtoJSON(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("./user-db.json"), usersTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mapper.writeValue(new File("./loc-db.json"), locationsTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mapper.writeValue(new File("./chat-db.json"), chatLogsTable);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void readAllTableOnce(Tables tableName, CallBackDatabase callback) {

        HashMap<String, DatabaseObject> table = getTable(tableName);
        ArrayList<DatabaseObject> objsRead = new ArrayList<DatabaseObject>(table.values());

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
    public void listenObjChild(DatabaseObject obj, Tables tableName, Pair<String, Class> child, CallBackDatabase callback) {
        HashMap<String, DatabaseObject> table = getTable(tableName);
        DatabaseObject curobj = table.get(obj.getID());
        Object ret = null;

        if(child.first.equals("messages"))
            ret = new Message("userTest3", "Helo", new Date());
        else if(child.first.equals("membersId")){
            ChatLogs curChat = (ChatLogs) curobj;
            if((curChat.getMembersId().size() > 0))
                ret = ((ChatLogs) curobj).getMembersId().get(((ChatLogs) curobj).getMembersId().size()-1);
            else
                ret = "userTest2";
        }
        callback.onFinish(ret);
    }


    public void fillDatabase(){
        if(currentUSer != null) return;

        currentUSer = new User("testUser1");
        currentUSer.addChat("testUser2", "10");
        currentUSer.addChat("testUser3", "11");
        currentUSer.addChat("testUser5", "12");
        ArrayList<String> blockedUser = new ArrayList<>();
        blockedUser.add("testUser3");currentUSer.setBlockedUsers(blockedUser);

        usersTable.put("testUser1", currentUSer);

        User temp = new User("testUser2");currentUSer.addFriendRequest(temp);

        temp.addFriendRequest(currentUSer);temp.addChat("testUser1", "10");
        usersTable.put("testUser2", temp);

        temp = new User("testUser3");currentUSer.addFriendRequest(temp);
        blockedUser.clear();blockedUser.add("testUser1");
        temp.setBlockedUsers(blockedUser);usersTable.put("testUser3", temp);

        temp = new User("testUser4");usersTable.put("testUser4", temp);
        temp = new User("testUser5");usersTable.put("testUser5", temp);
        // Define Current user
        fillLocationsTable();fillChatlogsTable();
    }

    private void fillFirstChat(){

        ChatLogs tempChat = new ChatLogs("10");
        tempChat.addMembersId("testUser1"); tempChat.addMembersId("testUser2");
        tempChat.addMessage(new Message("testUser1", "Hello There", new Date()));
        tempChat.addMessage(new Message("testUser2", "General", new Date()));
        tempChat.addMessage(new Message("testUser1", "Obi-Wan", new Date()));
        tempChat.addMessage(new Message("testUser2", "Kenobi", new Date()));
        chatLogsTable.put("10", tempChat);

    }

    private void fillSecondChat(){

        ChatLogs tempChat = new ChatLogs("11");
        tempChat.addMembersId("testUser3"); tempChat.addMembersId("testUser1");
        tempChat.addMessage(new Message("testUser1", "Hello There", new Date()));
        tempChat.addMessage(new Message("testUser3", "General Kenobi", new Date()));
        chatLogsTable.put("11", tempChat);

        tempChat = new ChatLogs("12");
        tempChat.addMembersId("testUser5"); tempChat.addMembersId("testUser1");
        tempChat.addMessage(new Message("testUser1", "Hello There", new Date()));
        tempChat.addMessage(new Message("testUser5", "General Kenobi", new Date()));
        chatLogsTable.put("12", tempChat);

    }

    private void fillTopicChats(){

        ChatLogs tempChat = new ChatLogs("MyTestTopic");
        tempChat.addMembersId("testUser1"); tempChat.addMembersId("testUser2");
        tempChat.addMembersId("testUser3"); tempChat.addMembersId("testUser4");
        tempChat.addMessage(new Message("testUser1", "Hello There", new Date()));
        chatLogsTable.put("MyTestTopic", tempChat);

        tempChat = new ChatLogs("MyTestTopic2");
        tempChat.addMembersId("testUser4"); tempChat.addMembersId("testUser3");
        tempChat.addMembersId("testUser2"); tempChat.addMembersId("testUser1");
        tempChat.addMessage(new Message("testUser4", "Hello", new Date()));
        chatLogsTable.put("MyTestTopic2", tempChat);

        tempChat = new ChatLogs("MyTestTopic5");
        tempChat.addMembersId("testUser5"); tempChat.addMembersId("testUser1");
        tempChat.addMessage(new Message("testUser5", "Goodbye.", new Date()));
        chatLogsTable.put("MyTestTopic5", tempChat);

    }

    private void fillChatlogsTable(){

        fillFirstChat(); fillSecondChat(); fillTopicChats();

        ChatLogs tempChat = new ChatLogs("testGroup");
        tempChat.addMembersId("testUser3"); tempChat.addMembersId("testUser4");
        tempChat.addMembersId("testUser1"); tempChat.addMembersId("testUser2");
        tempChat.addMessage(new Message("testUser3", "Howdihey", new Date()));
        chatLogsTable.put("testGroup", tempChat);

        tempChat = new ChatLogs("testGroup2");
        tempChat.addMembersId("testUser2"); tempChat.addMembersId("testUser4");
        tempChat.addMembersId("testUser3"); tempChat.addMembersId("testUser2");
        tempChat.addMessage(new Message("testUser1", "Howdi", new Date()));
        chatLogsTable.put("testGroup2", tempChat);

        tempChat = new ChatLogs("testGroup3");
        tempChat.addMembersId("testUser5"); tempChat.addMembersId("testUser4");
        tempChat.addMessage(new Message("testUser5", "Ok then", new Date()));
        chatLogsTable.put("testGroup3", tempChat);

    }

    private void fillLocationsTable(){

        currentLoc = new MLocation("testUser1", defaultLng, defaultLat);
        currentLoc.setUrlProfilePhoto("./app/src/androidTest/java/ch/epfl/sweng/radius/utils/default.png");
        currentLoc.setTitle("testUser1");
        currentLoc.setRadius(30000); currentLoc.setMessage("Being tested on");
        currentLoc.setInterests("Tests, mostly");locationsTable.put("testUser1", currentLoc);

        /** USERS **/
        MLocation temp = new MLocation("testUser2", defaultLng + 0.01, defaultLat + 0.01);
        temp.setUrlProfilePhoto("./app/src/androidTest/java/ch/epfl/sweng/radius/utils/default.png");
        temp.setTitle("testUser2"); temp.setMessage("Helping witht the tests !");
        locationsTable.put("testUser2", temp);

        temp = new MLocation("testUser3", defaultLng - 0.01, defaultLat - 0.01);
        temp.setTitle("testUser3"); temp.setMessage("Helping witht the tests too !");
        locationsTable.put("testUser3", temp);

        temp = new MLocation("testUser4", defaultLng - 0.01, defaultLat + 0.01);
        temp.setTitle("testUser4"); temp.setMessage("Not Helping witht the tests !");
        locationsTable.put("testUser4", temp);

        temp = new MLocation("testUser5", 0, 0);
        temp.setTitle("testUser5"); temp.setMessage("Far awayyyy");
        locationsTable.put("testUser5", temp);

        /** TOPICS **/
        MLocation tempTopic = new MLocation("MyTestTopic", defaultLng + 0.01, defaultLat - 0.02);
        tempTopic.setLocationType(2);tempTopic.setOwnerId("testUser1");
        tempTopic.setTitle("TopicTest !");locationsTable.put("MyTestTopic", tempTopic);

        tempTopic = new MLocation("MyTestTopic2", defaultLng, defaultLat);
        tempTopic.setLocationType(2);tempTopic.setOwnerId("testUser2");
        tempTopic.setTitle("TopicTest2 !");locationsTable.put("MyTestTopic2", tempTopic);

        tempTopic = new MLocation("MyTestTopic5", defaultLng + 10, defaultLat - 10);
        tempTopic.setLocationType(2);tempTopic.setOwnerId("testUser5");
        tempTopic.setTitle("TopicTest5 !");locationsTable.put("MyTestTopic5", tempTopic);

        /** GROUPS **/
        MLocation tempGroup = new MLocation("testGroup", defaultLng + 0.2, defaultLat + 0.1);
        tempGroup.setLocationType(1);tempGroup.setTitle("Test Group #1");
        tempGroup.setMessage("Come to Daddy");locationsTable.put("testGroup", tempGroup);

        tempGroup = new MLocation("testGroup2", defaultLng - 0.2, defaultLat + 0.1);
        tempGroup.setLocationType(1);tempGroup.setTitle("Test Group #2");
        tempGroup.setMessage("Come to Mommy");locationsTable.put("testGroup2", tempGroup);

        tempGroup = new MLocation("testGroup3", defaultLng + 10, defaultLat - 10);
        tempGroup.setLocationType(1);tempGroup.setTitle("Test Group #3");
        tempGroup.setMessage("Don't come I guess");locationsTable.put("testGroup", tempGroup);
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
