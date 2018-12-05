package ch.epfl.sweng.radius.utils.customLists.customGroups;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;
import ch.epfl.sweng.radius.R;

public class CustomGroupListAdapter extends CustomListAdapter{


    public CustomGroupListAdapter(List<CustomListItem> items, Context context) {
        super(items,context);
    }

    @NonNull
    @Override
    public CustomListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupItemHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_item_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        GroupItemHolder groupItemHolder = (GroupItemHolder) viewHolder;
        Log.e("CustomTopicListAdapter", "Items topics size :" + items.size());
        groupItemHolder.textViewTitle.setText(items.get(position).getItemName());
        CustomListItem item = items.get(position);
        CustomGroupListListeners customListener = new CustomGroupListListeners(item.getItemId(),
                item.getItemName(), item.getConvId());
        customListener.setCustomOnClick(groupItemHolder.textViewTitle, context);
    }

    // inner class to hold a reference to each item of RecyclerView
    static class GroupItemHolder extends ViewHolder {
        TextView textViewTitle;

        GroupItemHolder(View itemLayoutView) {
            super(itemLayoutView);
            textViewTitle = itemLayoutView.findViewById(R.id.groupName);
        }
    }

}
