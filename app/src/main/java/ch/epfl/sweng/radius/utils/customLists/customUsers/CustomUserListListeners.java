package ch.epfl.sweng.radius.utils.customLists.customUsers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.epfl.sweng.radius.browseProfiles.BrowseProfilesBlockedActivity;
import ch.epfl.sweng.radius.browseProfiles.BrowseProfilesUnblockedActivity;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.ChatlogsUtil;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.messages.MessageListActivity;

public class CustomUserListListeners {
    private final static int LOCATION_TYPE = 0;
    private final Database database = Database.getInstance();
    private int clickedPic;
    private String clickedName;
    private String userId;


    public CustomUserListListeners(int clickedPic, String userId , String userName){
        this.clickedPic = clickedPic;
        this.clickedName = userName;
        this.userId = userId;
    }

    public void setCustomOnClick(ImageView imageView, final Context context) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                //If we are blocked by the user redirect to the "You are blocked" page.
                if (OthersInfo.getInstance().getUsers().get(userId).getBlockedUsers().contains(UserInfo.getInstance().getCurrentUser().getID())) {
                    intent = new Intent(context, BrowseProfilesBlockedActivity.class);
                } else {
                    intent = new Intent(context, BrowseProfilesUnblockedActivity.class);
                }

                intent.putExtra("Clicked Picture", clickedPic);
                intent.putExtra("Clicked Name", clickedName);
                intent.putExtra("UID", userId);
                context.startActivity(intent);
            }
        });
    }

    public void setCustomOnClick(LinearLayout linearLayout, final Context context, final String userId, final String convId) {
        Log.e("ChatlogsDebug", "Chat OnClick UserId is" + userId);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chatId = convId;
                ChatLogs chat = ChatlogsUtil.getInstance().getChat(convId, 0);
                Log.e("ChatlogsDebug", "Chat OnClick UserId is" + userId);
                if(chat == null){
                    chatId = ChatlogsUtil.getInstance().getNewChat(userId);
                    Log.e("ChatlogsDebug", "Chat was null" + chatId);

                }

                goToMessageActivity(context, chatId, userId);
                }
            });
        }

    private void goToMessageActivity(Context context, String chatId, String userId){
        Intent intent = new Intent(context, MessageListActivity.class);
        Bundle b = new Bundle();
        Log.e("RealTimeDebug", "gotToMessageActivity" + userId);

        b.putString("chatId", chatId);
        b.putString("otherId", userId);
        b.putInt("locType", LOCATION_TYPE);
        intent.putExtras(b); context.startActivity(intent);
    }

}
