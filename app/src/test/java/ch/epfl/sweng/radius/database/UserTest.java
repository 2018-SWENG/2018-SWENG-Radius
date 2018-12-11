package ch.epfl.sweng.radius.database;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class UserTest {

    private User testUser;
    private User testFriend;

    @Before
    public void setUp() {
        Database.activateDebugMode();
        testUser = new User("testUser");
        testFriend = new User("testFriend");
    }


    @Test
    public void testFriendship() {
        assertTrue(testUser.getFriends().isEmpty());
        assertTrue(testUser.getFriendsInvitations().isEmpty());
        testUser.addFriendRequest(testFriend);


        assertTrue(testUser.getFriends().isEmpty());
        assertTrue(testUser.getFriendsInvitations().isEmpty());
        testFriend.addFriendRequest(testUser);

        assertFalse(testUser.getFriends().isEmpty());
        assertTrue(testUser.getFriendsInvitations().isEmpty());
        testUser.removeFriend(testFriend);

        assertTrue(testUser.getFriends().isEmpty());
        assertTrue(testUser.getFriendsInvitations().isEmpty());

        List<String> blocked = new ArrayList<>();
        blocked.add(testFriend.getID());
        testUser.setBlockedUsers(blocked);

    }
}
