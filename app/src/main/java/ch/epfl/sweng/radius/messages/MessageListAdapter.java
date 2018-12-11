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

import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.CallBackDatabase;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.Message;
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
    private static HashMap<String, MLocation> usersHashMap;
    private static UserUtils userUtils = UserUtils.getInstance();

    private Context context;
    private List<Message> messages;
    private int flags;
    private List<String> membersIds;

    public MessageListAdapter(Context context, List<Message> messages, List<String> membersIds) {
        this.context = context;
        this.messages = messages;
        this.usersHashMap = new HashMap<>();
        this.membersIds = new ArrayList<>();
        flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE;

    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setMembersIds(final List<String> membersIds) {
        this.membersIds = membersIds;
        this.membersIds.remove(UserInfo.getInstance().getCurrentUser().getID());
        Database.getInstance().readListObjOnce(membersIds,
                Database.Tables.LOCATIONS,
                new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        for(MLocation loc : (ArrayList<MLocation>) value)
                            usersHashMap.put(loc.getID(), loc);
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
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
        }

        void bind(Message message) {
            messageText.setText(message.getContentMessage());


            // Format the stored timestamp into a readable String using method.
            timeText.setText(DateUtils.formatDateTime(context, message.getSendingTime().getTime(), flags));
            MLocation currentUser = usersHashMap.get(message.getSenderId());
            if (currentUser != null) {
                nameText.setText(currentUser.getTitle());
                setPicture(currentUser.getUrlProfilePhoto(),itemView);
            }


        }
    }

    private void setPicture(String currentUserUrl,View itemView){
        ImageView profileImage;
        profileImage = itemView.findViewById(R.id.avatar);

        if (currentUserUrl != null && !currentUserUrl.isEmpty()) {
            Picasso.get().load(currentUserUrl).into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.user_photo_default);
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
            //            setPicture(UserInfo.getInstance().getCurrentPosition().getUrlProfilePhoto(),itemView);
        }
    }
}
