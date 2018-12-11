package ch.epfl.sweng.radius.utils.customLists.customUsers;

import android.content.Context;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.util.List;

import ch.epfl.sweng.radius.database.MLocation;
import ch.epfl.sweng.radius.database.OthersInfo;
import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;

public class CustomUserListAdapter extends CustomListAdapter {

    public CustomUserListAdapter(List<CustomListItem> items, Context context) {
        super(items, context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        CustomListItem item = items.get(position);
        MLocation itemUser = OthersInfo.getInstance().getAllUserLocations().get(item.getItemId());

        viewHolder.txtViewTitle.setText(item.getItemName());
        viewHolder.txtViewStatus.setText(itemUser.getMessage());

        if(itemUser != null){
            if (!itemUser.getUrlProfilePhoto().isEmpty()) {
                Picasso.get().load(itemUser.getUrlProfilePhoto()).into(viewHolder.imgViewIcon);
            } else {
                viewHolder.imgViewIcon.setImageResource(items.get(position).getProfilePic());
            }
        }
        else{
            Log.e("CustomUserListAdapter", "Item ID not found in Users");
        }
        final String clickedId = item.getItemId();
        //Log.e("CustomUserListAdapter", "item.getItemId() :" + item.getItemId());
        CustomUserListListeners customListener = new CustomUserListListeners(item.getProfilePic(), clickedId,item.getItemName());
        customListener.setCustomOnClick(viewHolder.imgViewIcon, context);
        customListener.setCustomOnClick(viewHolder.txtViewTitle, context,clickedId,item.getConvId());
    }


}
