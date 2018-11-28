package ch.epfl.sweng.radius.messages;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.Message;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.database.UserUtils;

/**
 * Adapter for the RecyclerView that will store a list of message,
 * determine if a message is sent or received
 * and inflate the appropriate layout within the RecyclerView.
 */
public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static HashMap<String, User> usersHashMap;
    private static UserUtils userUtils = UserUtils.getInstance();

    private Context context;
    private List<Message> messages;
    private int flags;
    private List<String> membersIds;

    public MessageListAdapter(Context context, List<Message> messages,List<String> membersIds) {
        this.context = context;
        this.messages = messages;
        this.usersHashMap = new HashMap<>();
        this.membersIds = new ArrayList<>();
        flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE;

    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setMembersIds(List<String> membersIds) {
        this.membersIds = membersIds;
        this.usersHashMap = userUtils.getSpecificsUsers(membersIds);
    }

    public List<String> extractSenderId(List<Message> messages) {
        List<String> result = new ArrayList<>();
        for(Message m : messages){
            if(!result.contains(m.getSenderId())){
                result.add(m.getSenderId());
            }
        }
        return result;
    }
    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Log.e("message", "Updates view and message size is " + messages.size());

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }


    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(UserInfo.getInstance().getCurrentUser().getID())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        TextView nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            nameText = itemView.findViewById(R.id.text_message_name);
            profileImage = itemView.findViewById(R.id.avatar);
        }

        void bind(Message message) {
            messageText.setText(message.getContentMessage());


            // Format the stored timestamp into a readable String using method.
            timeText.setText(DateUtils.formatDateTime(context, message.getSendingTime().getTime(), flags));
            if(usersHashMap.get(message.getSenderId()) != null) {
                nameText.setText(usersHashMap.get(message.getSenderId()).getNickname());
            }

            // Insert the profile image from the URL into the ImageView.
            //Utils.displayRoundImageFromUrl(
            // context, message.getSenderId().getProfileUrl(), profileImage);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        void bind(Message message) {
            messageText.setText(message.getContentMessage());


            // Format the stored timestamp into a readable String using method.
            timeText.setText(DateUtils.formatDateTime(context, message.getSendingTime().getTime(), flags));

            // Insert the profile image from the URL into the ImageView.
            //Utils.displayRoundImageFromUrl(
            // context, message.getSenderId().getProfileUrl(), profileImage);
        }
    }
}
