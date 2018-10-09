package ch.epfl.sweng.radius;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

public class MessageListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<BaseMessage> mMessageList;

    public MessageListAdapter(Context context, List<BaseMessage> messageList) {
        mContext = context;
        mMessageList = messageList;
    }
}
