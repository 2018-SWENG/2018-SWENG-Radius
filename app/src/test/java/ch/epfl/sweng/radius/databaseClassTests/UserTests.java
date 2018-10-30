package ch.epfl.sweng.radius.databaseClassTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.radius.database.User;

import static org.junit.Assert.assertTrue;

public class UserTests {

    private User user1, user2;
    private String user1Id;

    @Before
    public void setUp() {
        user1Id = "User1Id";
        user1 = new User( user1Id);
        user2 = new User();
    }

    @Test
    public void testUrlPhoto() {
        assertTrue(user1.getUrlProfilePhoto().equals(""));
    }

    @Test
    public void testSetUrlPhoto() {
        String urlPhotoAddress;
        urlPhotoAddress = "https://www.testurl.com/testpath/testfile";
        user1.setUrlProfilePhoto(urlPhotoAddress);
        assertTrue(user1.getUrlProfilePhoto().equals(urlPhotoAddress));
    }

    @Test
    public void testGetRadius() {
        int radius = user1.getRadius();
        assertTrue(radius == 50000);
    }

    @Test // might need to change it as we  change the user class
    public void testGetFriendsHandler() {
        assertTrue(user1.getFriendsHandler() == null);
    }

    @Test
    public void testGetProfileInfo() {
        assertTrue(user1.getProfileInfo() != null);
    }

    @Test
    public void testGetConvoFromUser() {
        String convo = user1.getConvFromUser(user1.getID());
        assertTrue(convo == null);
    }

    @Test
    public void testAddChat() {
        user1.addChat(user1Id, "User1ChatId");
        String convo = user1.getConvFromUser(user1.getID());
        assertTrue(convo != null);
    }

    @Test
    public void testLocation() {
        assertTrue(user1.getLocation() == null);
    }

    @Test
    public void testGetUserId() {
        assertTrue(user1.getID().equals(user1Id));
        assertTrue(!user2.getID().equals(""));
    }

    @Test
    public void testGetHidden() {
        assertTrue(!user1.getisHidden());
        assertTrue(!user2.getisHidden());
    }

    @Test
    public void testSetRadius() {
        int newRadius = 0;
        user1.setRadius(newRadius);
        assertTrue(user1.getRadius() == newRadius);
    }

    @Test
    public void testToggleHidden() {
        user1.toggleHidden();
        assertTrue(user1.getisHidden());
    }

    @Test
    public void testGetChatList() {
        assertTrue(user1.getChatList() != null);
    }

    @Test
    public void testGetReportList() {
        assertTrue(user1.getReportList() != null);
    }

    @Test
    public void testAddReport() {
        assertTrue(user1.getReportFromUser(user2.getID()) == null);
        user1.addReport(user2.getID(), "Spam");
        assertTrue(user1.getReportFromUser(user2.getID()) != null);
    }

    @After
    public void tearDown() {
        user1 = null;
        user1Id = null;
        user2 = null;
    }
}
