package ch.epfl.sweng.radius;

import android.Manifest;
import android.support.test.rule.GrantPermissionRule;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;


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
        try{
            new ChatLogs(usersIds);
            assert(false);
        }catch (Exception e){

        }
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
        Assert.assertNotNull(chatLogs.getChatLogsId());

        // Test messages
        List allmessages = chatLogs.getAllMessages();
        List Nmessages = chatLogs.getLastNMessages(10);
        assert(allmessages.contains(m));
        assert(Nmessages.contains(m));
    }

    @Test
    public void testUser() {
        // Test no duplicates friends requests
        User user = new User("1234");
        user.addFriendRequest("123");
        user.addFriendRequest("123");
        assert(user.getFriendsRequests().size() == 1);

        // Test status max characters
        String status = user.getStatus();
        try {
            user.setStatus("123456789012345678901234567890123456789012345678902345678901234567890");
            assert(false);
        } catch (Exception e){}
        assert(user.getStatus() == status);
    }

}
