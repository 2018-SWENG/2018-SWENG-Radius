package ch.epfl.sweng.radius.utils.customLists.customTopics;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.Database;
import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.database.UserInfo;
import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;

public class CustomTopicListAdapter extends CustomListAdapter {

    private ArrayList<Integer> removableTopicPositions;

    private static final int TOPIC_ITEM = 1;
    private static final int TOPIC_CREATE_BUTTON = 2;
    private static final int REMOVABLE_TOPIC_ITEM = 3;

    public CustomTopicListAdapter(List<CustomListItem> items, Context context,
                                  ArrayList<Integer> removableTopicPositions) {
        super(items, context);
        this.removableTopicPositions = removableTopicPositions;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TOPIC_CREATE_BUTTON;
        } else if (removableTopicPositions.contains(position)) {
            return REMOVABLE_TOPIC_ITEM;
        }
        return TOPIC_ITEM;
    }

    @NonNull
    @Override
    public CustomListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TOPIC_CREATE_BUTTON) {
            return new TopicCreateButtonHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.create_topic, null));
        } else if (viewType == REMOVABLE_TOPIC_ITEM) {
            return new RemovableTopicItemHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.removable_topic_item_layout, null));
        }
        return new TopicItemHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic_item_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TOPIC_ITEM:
                TopicItemHolder topicItemHolder = (TopicItemHolder) viewHolder;
                Log.e("CustomTopicListAdapter", "Items topics size :" + items.size());
                topicItemHolder.textViewTitle.setText(items.get(position).getItemName());
                CustomListItem item = items.get(position);
                CustomTopicListListeners customListener = new CustomTopicListListeners(item.getItemId(),
                        item.getItemName(), item.getConvId());
                customListener.setCustomOnClick(topicItemHolder.textViewTitle, context);
                break;
            case TOPIC_CREATE_BUTTON:
                break;
            case REMOVABLE_TOPIC_ITEM:
                RemovableTopicItemHolder removableTopicItemHolder = (RemovableTopicItemHolder) viewHolder;
                Log.e("CustomTopicListAdapter", "Items topics size :" + items.size());
                removableTopicItemHolder.textViewTitle.setText(items.get(position).getItemName());
                removableTopicItemHolder.topicID = items.get(position).getItemId();
                item = items.get(position);
                customListener =  new CustomTopicListListeners(item.getItemId(),
                        item.getItemName(), item.getConvId());
                customListener.setCustomOnClick(removableTopicItemHolder.textViewTitle, context);
        }
    }

    public void setRemovableTopicPositions(ArrayList<Integer> removableTopicPositions) {
        this.removableTopicPositions = removableTopicPositions;
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class TopicItemHolder extends ViewHolder {
        TextView textViewTitle;

        TopicItemHolder(View itemLayoutView) {
            super(itemLayoutView);
            textViewTitle = itemLayoutView.findViewById(R.id.topicName);
        }
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class RemovableTopicItemHolder extends ViewHolder {
        TextView textViewTitle;
        Button removeTopicButton;
        String topicID;
        RemovableTopicItemHolder(View itemLayoutView) {
            super(itemLayoutView);
            textViewTitle = itemLayoutView.findViewById(R.id.topicName);
            removeTopicButton = itemLayoutView.findViewById(R.id.removeTopicButton);
            removeTopicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().
                            getReference(Database.Tables.LOCATIONS.toString())
                            .child(topicID).removeValue();
                    OthersInfo.getInstance().removeFromTable(OthersInfo.getInstance()
                            .getTopicsPos().get(topicID));
                }
            });
        }
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class TopicCreateButtonHolder extends ViewHolder {
        TextView textViewTitle;
        Button createTopicButton;

        TopicCreateButtonHolder(View itemLayoutView) {
            super(itemLayoutView);
            textViewTitle = itemLayoutView.findViewById(R.id.create_topic);

            createTopicButton = itemLayoutView.findViewById(R.id.create_topic_button);
            createTopicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createPrompt(view);
                }
            });
        }
    }

    private static void createPrompt(View view) {
        // get prompts.xml view
        LayoutInflater inflater = LayoutInflater.from(view.getContext());
        View promptsView = inflater.inflate(R.layout.topic_prompt, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String topicName = userInput.getText().toString();
                                topicName = topicName.replaceAll("[^A-Za-z0-9_ f]", "");

                                pushTopicToDatabase(topicName);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private static void pushTopicToDatabase(String topicName) {
        if (!topicName.isEmpty()) {
            MLocation newTopic = new MLocation(UUID.randomUUID().toString());

            // new topic is set by user location values
            newTopic.setLocationType(2);newTopic.setTitle(topicName); // topic type
            newTopic.setOwnerId(UserInfo.getInstance().getCurrentUser().getID());
            newTopic.setLatitude(UserInfo.getInstance().getCurrentPosition().getLatitude());
            newTopic.setLongitude(UserInfo.getInstance().getCurrentPosition().getLongitude());
            newTopic.setRadius(UserInfo.getInstance().getCurrentPosition().getRadius());
            Database.getInstance().writeInstanceObj(newTopic, Database.Tables.LOCATIONS);

            ChatLogs topicChatLog = new ChatLogs(topicName);
            topicChatLog.addMembersId(UserInfo.getInstance().getCurrentUser().getID()); // creator is a member
            Database.getInstance().writeInstanceObj(topicChatLog, Database.Tables.CHATLOGS);
        }
    }

}
