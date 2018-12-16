package ch.epfl.sweng.radius.utils.customLists.customGroups;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.util.List;

import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;
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



        CustomListItem item = items.get(position);
        final String groupId = item.getItemId();
        final String groupName = item.getItemName();
        final String convId = item.getConvId();

        MLocation itemGroup = OthersInfo.getInstance().getGroupsPos().get(item.getItemId());

        if (viewHolder.imgViewIcon != null) {
            if(itemGroup.getLocationType() == 1) { //is a group
                if(itemGroup != null) {
                    if (!itemGroup.getUrlProfilePhoto().isEmpty()) {
                        Picasso.get().load(itemGroup.getUrlProfilePhoto()).into(viewHolder.imgViewIcon);
                    } else {
                        viewHolder.imgViewIcon.setImageResource(items.get(position).getProfilePic());
                    }
                }
            }
        }
        if (viewHolder.txtViewTitle != null) {
            viewHolder.txtViewTitle.setText(groupName);
        }
        if (viewHolder.txtViewStatus != null) {
            viewHolder.txtViewStatus.setText("");
        }

        CustomGroupListListeners customListener = new CustomGroupListListeners(groupId,groupName,convId);
        customListener.setCustomOnClick(viewHolder.txtViewTitle, context);
    }

}
