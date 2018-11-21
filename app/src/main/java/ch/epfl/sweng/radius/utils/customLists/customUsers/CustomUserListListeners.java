package ch.epfl.sweng.radius.utils.customLists.customUsers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.radius.browseProfiles.BrowseProfilesActivity;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.messages.MessageListActivity;

import ch.epfl.sweng.radius.database.User;

public class CustomUserListListeners {
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
                Intent intent = new Intent(context, BrowseProfilesActivity.class);
                intent.putExtra("Clicked Picture", clickedPic);
                intent.putExtra("Clicked Name", clickedName);
                intent.putExtra("UID", userId);
                context.startActivity(intent);
            }
        });
    }

    public void setCustomOnClick(TextView textView, final Context context, final String userId, final String convId) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("MessageList", "Conv ID is :" + convId);
                database.readListObjOnce(Arrays.asList(database.getCurrent_user_id(), userId),
                        Database.Tables.USERS, new CallBackDatabase() {
                            @Override
                            public void onFinish(Object value) {
                                ArrayList<User> users = (ArrayList<User>)value;
                                String chatId = convId;
                                if(convId.isEmpty()){ // If the conversation doesn't exist, it has to be created
                                    ArrayList<String> ids = new ArrayList();
                                    ids.add(userId); ids.add(database.getCurrent_user_id());
                                    chatId = new ChatLogs(ids).getID();
                                    users.get(0).addChat(users.get(1).getID(), chatId);
                                    users.get(1).addChat(users.get(0).getID(), chatId);
                                    // Update database entry for temp user with new chatLof
                                    database.writeInstanceObj(users.get(0), Database.Tables.USERS);
                                    database.writeInstanceObj(users.get(1), Database.Tables.USERS);
                                }
                                goToMessageActivity(context, chatId, userId);
                            }
                            @Override
                            public void onError(DatabaseError error) {Log.e("Firebase", error.getMessage());}
                        });
            }
        });
    }

    private void goToMessageActivity(Context context, String chatId, String userId){
        Intent intent = new Intent(context, MessageListActivity.class);
        Bundle b = new Bundle();
        b.putString("chatId", chatId);
        b.putString("otherId", userId);
        intent.putExtras(b); context.startActivity(intent);
    }

}
