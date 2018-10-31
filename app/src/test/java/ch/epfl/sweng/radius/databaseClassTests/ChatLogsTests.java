package ch.epfl.sweng.radius.databaseClassTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Message;

import static org.junit.Assert.assertTrue;

public class ChatLogsTests {

    private ChatLogs chatLogs1, chatLogs2;
    private ArrayList<String> membersId;
    private String chatlogsId;

    @Before
    public void setUp() {
        membersId = new ArrayList<String>();
        membersId.add("UserID1");
        membersId.add("UserID2");
        chatlogsId = "1";

        chatLogs1 = new ChatLogs(membersId);
        chatLogs2 = new ChatLogs(chatLogs1);
    }

    @Test
    public void testGetMembersId() {
        assertTrue(chatLogs1.getMembersId() != null);
        assertTrue(chatLogs2.getMembersId() != null);
    }

    @Test
    public void testGetAllMessages() {
        assertTrue(chatLogs1.getAllMessages() != null);
        assertTrue(chatLogs2.getAllMessages() != null);
    }

    @Test
    public void testAddDeleteMessage() {
        Message message;
        message = new Message("UserID1", "Hello", new Date(0));
        chatLogs1.addMessage(message);
    }

    @Test
    public void testGetLastNMessages() {
        Message message1, message2, message3;
        message1 = new Message("UserID1", "Hello", new Date(0));
        message2 = new Message("UserID2", "Hello Back", new Date(0));
        message3 = new Message("UserID1", "Wanna hear a funny joke? Industrial Engineers hsasdasdfas", new Date(0));
        chatLogs1.addMessage(message1);
        chatLogs1.addMessage(message2);
        chatLogs1.addMessage(message3);

        assertTrue(chatLogs1.getLastNMessages(-3).size() == 3);
        assertTrue(chatLogs1.getLastNMessages(3).size() == 3);
    }

    @Test
    public void testAddMembersId() {
        chatLogs1.addMembersId("User_1");
        assertTrue(chatLogs1.getMembersId().contains("User_1"));
        assertTrue(!chatLogs1.getMembersId().contains("User_11"));
    }

    @Test
    public void testGetChatlogsId() {
        assertTrue(Integer.parseInt(chatLogs2.getChatLogsId()) >= 0);
    }

    @Test
    public void testGetId() {
        String chatlogs1Id = chatLogs1.getChatLogsId();
        assertTrue(chatLogs1.getID().equals(chatlogs1Id));
    }

    @After
    public void tearDown() {
        membersId = null;
        chatlogsId = null;

        chatLogs1 = null;
        chatLogs2 = null;
    }
}
