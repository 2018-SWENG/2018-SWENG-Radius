package ch.epfl.sweng.radius.utils;

import android.Manifest;
import android.support.test.rule.GrantPermissionRule;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class DatabaseObjectsTest {
    @Rule
    public final GrantPermissionRule mPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void testMessages() {
        String senderId = "1234";
        String contentMessage = "hello sswde dde";
        Date sendingTime = new Date();

        Message message = new Message(senderId, contentMessage, sendingTime);

        String senderIdCompare = message.getSenderId();
        String contentMessageCompare = message.getContentMessage();
        Date sendingTimeCompare = message.getSendingTime();

        assert(contentMessageCompare.equals(contentMessage));
        assert(senderIdCompare.equals(senderId));
        assert(sendingTimeCompare == sendingTime);
    }

    @Test
    public void testChatLogs() {
        ArrayList<String> usersIds = new ArrayList<String>();
        new ChatLogs(usersIds);

        ChatLogs chat = new ChatLogs();
        usersIds.add("1234");
        usersIds.add("4321");
        ChatLogs chatLogs = new ChatLogs(usersIds);
        ChatLogs chatLogs1 = new ChatLogs("12345");
        Message m = new Message(chatLogs.getMembersId().get(0), chatLogs.getMembersId().get(1), new Date());
        chatLogs.addMessage(m);
        chatLogs.addMembersId("56789");
        chatLogs.addMembersId("56789");
        // Test Ids generation
        Assert.assertNotNull(chatLogs.getID());
        // Test messages
        List allmessages = chatLogs.getMessages();
        List Nmessages = chatLogs.getLastNMessages(10);
        assert(allmessages.contains(m));
        assert(Nmessages.contains(m));

        assertFalse(m.equals(new User()));
        chatLogs.removeMessage(m);
        chatLogs.addMessage(m);
        chatLogs.removeMessage(0);
        chatLogs.setMessages(allmessages);
        String id = chatLogs.getChatLogsId();
        chatLogs.setChatLogsId(id);

    }

    @Test
    public void testUser() {
        // Test no duplicates friends requests
        User user = new User("1234");
        User user2 = new User("123");
        user.addFriendRequest(user2);
        user2.addFriendRequest(user);
        user.addFriendRequest(user2);
        assert(user.getFriendsRequests().size() == 1);

        String chat = user.getConvFromUser("Arthur");
        Log.e("Test", "Coucou");
        assert(chat.isEmpty());
        user.newChat("Arthur");
        List<String> blocked = user.getBlockedUsers();
        List<String> req = user.getFriendsRequests();
        String url = user.getUrlProfilePhoto();
        Map<String, String> chats = user.getChatList();

        assertEquals(50, user.getRadius());
        user.setID("Arthur");
        assertEquals("Arthur", user.getID());

        Map<String, String> userList = user.getChatList();
        user.setChatList(userList);

        userList = user.getReportList();
    }

    @Test
    public void testMaxChars(){

        User user = new User("1234");

        // Test status max characters
        String status = user.getStatus();
        try {
            user.setStatus("123456789012345678901234567890123456789012345678902345678901234567890");
            assert(false);
        } catch (Exception e){}
        assert(user.getStatus() == status);

        //Test interests max characters
        String interests = user.getInterests();
        try {
            user.setInterests("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678900");
            assert(false);
        } catch (Exception e){}
        assert(user.getInterests() == interests);
    }

    @Test
    public void testMLocation(){

        MLocation mLocation = new MLocation();
        MLocation mLocation1 = new MLocation("locTest");
        MLocation mLocation2 = new MLocation("locTest", new LatLng(2.0, 3.0));

        assertTrue(mLocation1.getID().equals("locTest"));
        assertTrue(mLocation2.getLatitude() == 2.0);

        mLocation.setMessage("Msg");
        mLocation.setTitle("Title");
        mLocation.setID("locTest");

        assertEquals("Msg", mLocation.getMessage());
        assertEquals("Title", mLocation.getTitle());

        mLocation.setLatitude(1.0);
        mLocation.setLongitude(4.0);
        assert(mLocation2.getLatitude() == 1.0);
        assert(mLocation2.getLongitude() == 4.0);

        assertTrue(mLocation.isVisible());
        mLocation.setVisibility(false);
        assertFalse(mLocation.isVisible());

    }

}
