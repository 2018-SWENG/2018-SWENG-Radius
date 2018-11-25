package ch.epfl.sweng.radius.utils.customLists.customTopics;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;

public class CustomTopicListAdapter extends CustomListAdapter {

    public CustomTopicListAdapter(List<CustomListItem> items, Context context) {
        super(items, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.e("CustomTopicListAdapter", "Items topics size :" + items.size());
        viewHolder.txtViewTitle.setText(items.get(position).getItemName());

    }

}
