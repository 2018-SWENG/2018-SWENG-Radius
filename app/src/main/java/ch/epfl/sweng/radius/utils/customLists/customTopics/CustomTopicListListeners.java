package ch.epfl.sweng.radius.utils.customLists.customTopics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ch.epfl.sweng.radius.messages.MessageListActivity;

public class CustomTopicListListeners {
    private static final int LOCATION_TYPE = 2;
    private String topicId;
    private String topicName;
    private String convId;

    public CustomTopicListListeners(String topicId, String topicName, String convId){
        this.topicId = topicId;
        this.topicName = topicName;
        this.convId = convId;
    }

    public void setCustomOnClick(TextView textView, final Context context) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("chatId", convId);
                bundle.putString("topicId", topicId);
                bundle.putString("topicName", topicName);
                bundle.putInt("locType", LOCATION_TYPE);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }
}
