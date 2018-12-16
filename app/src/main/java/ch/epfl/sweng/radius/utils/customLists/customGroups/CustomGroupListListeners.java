package ch.epfl.sweng.radius.utils.customLists.customGroups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.epfl.sweng.radius.messages.MessageListActivity;

public class CustomGroupListListeners {
    private static final int LOCATION_TYPE = 1;
    private String groupId;
    private String groupName;
    private String convId;


    public CustomGroupListListeners(String groupId, String groupName, String convId){
        this.groupId =  groupId;
        this.groupName =  groupName;
        this.convId =  convId;
    }

    public void setCustomOnClick(LinearLayout linearLayout, final Context context) {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MessageListActivity.class);
                Bundle b = new Bundle();
                b.putString("chatId", convId);
                b.putString("groupId", groupId);
                b.putString("groupName", groupName);
                b.putInt("locType", LOCATION_TYPE);
                intent.putExtras(b);
                context.startActivity(intent);

            }
        });


    }
}
