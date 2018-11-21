package ch.epfl.sweng.radius.utils.customLists.customUsers;

import android.content.Context;
import android.util.Log;

import java.util.List;

import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;

public class CustomUserListAdapter extends CustomListAdapter {


    public CustomUserListAdapter(List<CustomListItem> items, Context context) {
        super(items, context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Log.e("CustomUserListAdapter", "Items users size :" + items.size());

        viewHolder.txtViewTitle.setText(items.get(position).getItemName());
        viewHolder.imgViewIcon.setImageResource(items.get(position).getProfilePic());

        CustomListItem item = items.get(position);
        final int clickedPic = item.getProfilePic();
        final String clickedId = item.getItemId();
        final String clickedConv = item.getConvId();
        CustomUserListListeners customListener = new CustomUserListListeners(clickedPic, clickedId,item.getItemName());
        customListener.setCustomOnClick(viewHolder.imgViewIcon, context);
        customListener.setCustomOnClick(viewHolder.txtViewTitle, context,clickedId,clickedConv);
    }


}
