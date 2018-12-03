package ch.epfl.sweng.radius.messages;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChatStateTest {

    private ChatState test;

    @Before
    public void setUp() throws Exception {
        test = new ChatState();
    }

    @Test
    public void allMethodsTest() {
        test.msgReceived();
        assertEquals(0, test.getUnreadMsg());
        test.leaveActivity();
        test.msgReceived();
        assertEquals(1, test.getUnreadMsg());
        assertFalse(test.isRunning());
        test.clear();
        assertTrue(test.isRunning());
        assertEquals(0, test.getUnreadMsg());
    }
}