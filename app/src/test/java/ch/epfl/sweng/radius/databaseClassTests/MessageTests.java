package ch.epfl.sweng.radius.databaseClassTests;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import ch.epfl.sweng.radius.database.Message;

import static org.junit.Assert.assertTrue;

public class MessageTests {

    private Message message1, message2;
    private String senderId;
    private String content;
    private Date date;

    @Before
    public void setUp() {
        senderId = "User_1";
        content = "message body";
        date = new Date(0);
        message1 = new Message(senderId, content, date);
        message2 = new Message();
    }

    @Test
    public void testGetSenderId() {
        assertTrue(message1.getSenderId().equals(senderId));
        assertTrue(message2.getSenderId().equals("10"));
    }

    @Test
    public void testGetContentMessage() {
        assertTrue(message1.getContentMessage().equals(content));
        assertTrue(message2.getContentMessage() == null);
    }

    @Test
    public void testGetSendingTime() {
        assertTrue(message1.getSendingTime().equals(date));
        assertTrue(message2.getSendingTime() == null);
    }

    @Test
    public void testSetSenderId() {
        message2.setSenderId("User_2");
        assertTrue(message2.getSenderId().equals("User_2"));
    }

    @Test
    public void testSetContentMessage() {
        message2.setContentMessage("message2");
        assertTrue(message2.getContentMessage().equals("message2"));
    }

    @Test
    public void testSetSendingTime() {
        Date date2 = new Date(1500);
        message2.setSendingTime(date2);
        assertTrue(message2.getSendingTime().equals(date2));
    }
}
