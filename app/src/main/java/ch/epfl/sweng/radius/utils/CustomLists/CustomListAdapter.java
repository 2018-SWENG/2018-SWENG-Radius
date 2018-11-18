package ch.epfl.sweng.radius.utils.CustomLists;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.radius.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder>{

    private List<CustomListItem> items;
    Context context;

    public CustomListAdapter(List<CustomListItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_item_layout, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        //TODO Remove this log from CustomListAdapter
        Log.e("PeopleTab", "Items users size :" + items.size());

        viewHolder.txtViewTitle.setText(items.get(position).getItemUser().getNickname());
        viewHolder.imgViewIcon.setImageResource(items.get(position).getFriendProfilePic());

        CustomListItem item = items.get(position);
        final int clickedPic = item.getFriendProfilePic();
        final String clickedId = item.getUserId();
        final String clickedConv = item.getConvId();
        CustomListListeners customListener = new CustomListListeners(clickedPic, item.getItemUser());
        customListener.setCustomOnClick(viewHolder.imgViewIcon, context);
        customListener.setCustomOnClick(viewHolder.txtViewTitle, context,clickedId,clickedConv);
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle;
        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = itemLayoutView.findViewById(R.id.username);
            imgViewIcon = (CircleImageView) itemLayoutView.findViewById(R.id.profile_picture);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<CustomListItem> items){
        this.items=items;
    }
}
