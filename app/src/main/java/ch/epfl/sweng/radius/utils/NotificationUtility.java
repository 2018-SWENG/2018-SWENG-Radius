package ch.epfl.sweng.radius.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import ch.epfl.sweng.radius.R;

public class NotificationUtility{

    private static int unseenMsg = 0;
    private static int unseenReq = 0;
    private Context context;
    private String channelID;
    private  NotificationManager notificationManager;
    private  NotificationCompat.Builder msgNotifBuilder;
    private NotificationCompat.Builder reqNotifBuilder;
    private static NotificationUtility instance;


    public NotificationUtility(NotificationManager nm, NotificationCompat.Builder msgNotif, NotificationCompat.Builder reqNotif){
        this.notificationManager = nm;

        this.msgNotifBuilder = msgNotif
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Radius Chat")
                .setTicker("Radius")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        this.reqNotifBuilder = reqNotif
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Radius")
                .setTicker("Radius")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
    }

    public static NotificationUtility getInstance(NotificationManager nm, NotificationCompat.Builder msgNotif, NotificationCompat.Builder reqNotif){
        if(instance == null)
            instance = new NotificationUtility(nm, msgNotif, reqNotif);
        return instance;
    }

    public void resetUnseenMsg(){
        unseenMsg = 0;
    }

    public void resetUnseenReq(){
        unseenMsg = 0;
    }

    public static void clearSeenMsg(int num){
        if(unseenMsg >= num) unseenMsg -= num;
    }


    public void notifyNewMessage(String senderId, String content, PendingIntent pi) {

         msgNotifBuilder.setContentText("New Message from " + senderId + " : " + content + "...")
                 .setContentIntent(pi)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("Radius Chat");
        unseenMsg++;
        notificationManager.notify(1, msgNotifBuilder.build());
    }

    public void notifyNewFrienReq(String userID, String userNickname, PendingIntent pi) {
        unseenMsg++;
        reqNotifBuilder.setContentText("New Friend Request from "+ userNickname + " (" + userID+")")
                .setSmallIcon(android.R.drawable.alert_dark_frame)
                .setContentTitle("Radius Friend Request")
                .setContentIntent(pi);
        notificationManager.notify(2, reqNotifBuilder.build());
    }
}
