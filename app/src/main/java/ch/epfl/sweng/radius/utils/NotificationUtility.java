package ch.epfl.sweng.radius.utils;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import ch.epfl.sweng.radius.R;

public class NotificationUtility {

    private static int unseenMsg = 0;
    private static int unseenReq = 0;
    private Context context;
    private String channelID;

    public NotificationUtility(Context context, String chanelID){
        this.context = context;
        this.channelID = chanelID;


    }

    public void resetUnseenMsg(){
        unseenMsg = 0;
    }

    public void resetUnseenReq(){
        unseenMsg = 0;
    }

    public void notifyNewMessage(String chatID, String senderID) {
        unseenMsg++;
        NotificationCompat.Builder msgNotifBuilder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.fui_ic_mail_white_24dp)
                .setContentTitle("Radius Chat")
                .setContentText("New Message in "+ chatID + " from " + senderID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    public void notifyNewFrienReq(String userID, String userNickname) {
        unseenMsg++;
        NotificationCompat.Builder msgNotifBuilder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.fui_ic_mail_white_24dp)
                .setContentTitle("Radius")
                .setContentText("New Friend Request from "+ userNickname + " (" + userID+")")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }
}
