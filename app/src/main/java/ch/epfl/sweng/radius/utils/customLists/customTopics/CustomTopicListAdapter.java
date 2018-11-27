package ch.epfl.sweng.radius.utils.customLists.customTopics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;
import ch.epfl.sweng.radius.utils.customLists.customGroups.CustomGroupListListeners;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustomTopicListAdapter extends CustomListAdapter {

    public CustomTopicListAdapter(List<CustomListItem> items, Context context) {
        super(items, context);
        items.add(0, new CustomListItem("Dummy","Dummy","Dummy"));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 2;
        }
        return 1;
    }

    @NonNull
    @Override
    public CustomListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 2) {
            return new ViewHolder2(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.create_topic, null));
        }
        return new ViewHolder1(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_item_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case 1:
                ViewHolder1 viewHolder1 = (ViewHolder1) viewHolder;
                Log.e("CustomTopicListAdapter", "Items topics size :" + items.size());
                viewHolder1.textViewTitle.setText(items.get(position).getItemName());
                CustomListItem item = items.get(position);
                CustomGroupListListeners customListener = new CustomGroupListListeners(item.getItemId(),
                        item.getItemName(), item.getConvId());
                customListener.setCustomOnClick(viewHolder1.textViewTitle, context);
                break;
            case 2:
        }
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder1 extends ViewHolder {

        TextView textViewTitle;
        ImageView imgViewIcon;

        ViewHolder1(View itemLayoutView) {
            super(itemLayoutView);
            textViewTitle = itemLayoutView.findViewById(R.id.username);
            imgViewIcon = (CircleImageView) itemLayoutView.findViewById(R.id.profile_picture);
        }
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder2 extends ViewHolder {

        TextView textViewTitle;
        Button createTopicButton;

        ViewHolder2(View itemLayoutView) {
            super(itemLayoutView);
            textViewTitle = itemLayoutView.findViewById(R.id.create_topic);
            createTopicButton = itemLayoutView.findViewById(R.id.create_topic_button);
        }
    }

}
