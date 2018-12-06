package ch.epfl.sweng.radius.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.FakeFirebaseUtility;

public class ChatLogDbUtilityTest {

    private ChatLogs chatLogs;

    @Before
    public void setUp() {
        Database.activateDebugMode();
        ((FakeFirebaseUtility)Database.getInstance()).fillDatabase();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void addMessage() {
    }

    @Test
    public void deleteMessage() {
    }

    @Test
    public void getMessage() {
    }

    @Test
    public void getChatLogs() {
    }

    @Test
    public void readChatLogs() {
    }

    @Test
    public void writeChatLogs() {
    }

    @Test
    public void writeChatLogs1() {
    }
}