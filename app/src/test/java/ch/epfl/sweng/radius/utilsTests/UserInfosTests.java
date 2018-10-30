package ch.epfl.sweng.radius.utilsTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.radius.utils.UserInfos;

import static org.junit.Assert.assertTrue;

public class UserInfosTests {

    private UserInfos userInfos = new UserInfos();

    @Test
    public void testGetUserId() {
        assertTrue(userInfos.getUserId() == "2");
    }

    @Test
    public void testGetUserName() {
        assertTrue(userInfos.getUsername() != null );
    }

    @Test
    public void testSetUserName() {
        String newUserName = "User_2";
        userInfos.setUsername(newUserName);
        assertTrue(userInfos.getUsername().equals(newUserName));
    }

    @Test
    public void testGetChatWith() {
        assertTrue(userInfos.getChatWith() != null);
    }

    @Test
    public  void testSetChatWith() {
        String newUserId = "User_2";
        userInfos.setChatWith(newUserId);
        assertTrue(userInfos.getChatWith().equals(newUserId));
    }
}
