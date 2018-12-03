package ch.epfl.sweng.radius.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.messages.MessageListActivity;

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
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        this.reqNotifBuilder = reqNotif
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Radius")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    public static NotificationUtility getInstance(NotificationManager nm, NotificationCompat.Builder msgNotif, NotificationCompat.Builder reqNotif){
        if(instance == null)
            instance = new NotificationUtility(nm, msgNotif, reqNotif);
        return instance;
    }

    public static void resetUnseenMsg(){
        unseenMsg = 0;
    }

    public static void resetUnseenReq(){
        unseenMsg = 0;
    }

    public static void clearSeenMsg(int num){
        unseenMsg -= num;
    }


    public void notifyNewMessage(String senderId, String content, PendingIntent pi) {

         msgNotifBuilder.setContentText("New Message from " + senderId + " : " + content + "...")
                .setTicker("Radius")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("Radius")
                .setContentIntent(pi)
                .setAutoCancel(true);
        unseenMsg++;
        notificationManager.notify(1, msgNotifBuilder.build());
    }

    public void notifyNewFrienReq(String userID, String userNickname) {
        unseenMsg++;
        reqNotifBuilder.setContentText("New Friend Request from "+ userNickname + " (" + userID+")");
        notificationManager.notify(2, reqNotifBuilder.build());
    }
}
