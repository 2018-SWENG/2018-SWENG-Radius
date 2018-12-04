package ch.epfl.sweng.radius.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.support.v4.app.NotificationCompat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class NotificationUtilityTest {

    private static NotificationUtility test;
    private NotificationManager mockedManager = Mockito.mock(NotificationManager.class);
    private NotificationCompat.Builder mockedBuilder = Mockito.mock(NotificationCompat.Builder.class);
    private PendingIntent mockedIntent = Mockito.mock(PendingIntent.class);
    @Before
    public void setUp() throws Exception {

        when(mockedBuilder.setSmallIcon(anyInt())).thenReturn(mockedBuilder);
        when(mockedBuilder.setTicker(any(CharSequence.class))).thenReturn(mockedBuilder);
        when(mockedBuilder.setPriority(anyInt())).thenReturn(mockedBuilder);
        when(mockedBuilder.setAutoCancel(anyBoolean())).thenReturn(mockedBuilder);
        when(mockedBuilder.setContentTitle(anyString())).thenReturn(mockedBuilder);
        when(mockedBuilder.setContentText(anyString())).thenReturn(mockedBuilder);
        when(mockedBuilder.setContentIntent(any(PendingIntent.class))).thenReturn(mockedBuilder);
        test = NotificationUtility.getInstance(mockedManager, mockedBuilder, mockedBuilder, mockedBuilder);

    }

    @Test
    public void getInstance() {
        NotificationUtility.getInstance(mockedManager, mockedBuilder, mockedBuilder, mockedBuilder);
        NotificationUtility.getInstance(null, null, null, null);
    }

    @Test
    public void resetUnseenMsg() {
        test.resetUnseenMsg();
    }

    @Test
    public void resetUnseenReq() {
        test.resetUnseenReq();
    }

    @Test
    public void clearSeenMsg() {
        NotificationUtility.clearSeenMsg(1);
    }

    @Test
    public void notifyNewMessage() {
        test.notifyNewMessage("0", "okok", mockedIntent);
    }

    @Test
    public void notifyNewFrienReq() {
        test.notifyNewFrienReq("0", "okok", mockedIntent);
    }

    @Test
    public void notifyNearFriend(){
        test.notifyFriendIsNear("0", "test friend", mockedIntent);
    }
}