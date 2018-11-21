package ch.epfl.sweng.radius.utils.customLists.customGroups;

import android.content.Context;
import android.util.Log;

import java.util.List;

import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;

public class CustomGroupListAdapter extends CustomListAdapter{


    public CustomGroupListAdapter(List<CustomListItem> items, Context context) {
        super(items,context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Log.e("CustomGroupListAdapter", "Items groups size :" + items.size());

        viewHolder.txtViewTitle.setText(items.get(position).getItemName());

        CustomListItem item = items.get(position);
        final String groupId = item.getItemId();
        final String groupName = item.getItemName();
        final String convId = item.getConvId();
        CustomGroupListListeners customListener = new CustomGroupListListeners(groupId,groupName,convId);
        customListener.setCustomOnClick(viewHolder.txtViewTitle, context);
    }


}
