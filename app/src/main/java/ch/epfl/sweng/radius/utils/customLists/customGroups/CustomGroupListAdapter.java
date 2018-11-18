package ch.epfl.sweng.radius.utils.customLists.customGroups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustomGroupListAdapter extends RecyclerView.Adapter<CustomGroupListAdapter.ViewHolder>{

    private List<CustomGroupListItem> items;
    Context context;

    public CustomGroupListAdapter(List<CustomGroupListItem> items, Context context) {
        this.items = new ArrayList<>(items);
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_item_layout, null);

        // create ViewHolder
        CustomGroupListAdapter.ViewHolder viewHolder = new CustomGroupListAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Log.e("CustomGroupListAdapter", "Items groups size :" + items.size());

        viewHolder.txtViewTitle.setText(items.get(position).getGroupeName());

        CustomGroupListItem item = items.get(position);
        final String groupId = item.getGroupId();
        final String groupName = item.getGroupeName();
        final String convId = item.getConvId();
        CustomGroupListListeners customListener = new CustomGroupListListeners(groupId,groupName,convId);
        customListener.setCustomOnClick(viewHolder.txtViewTitle, context);
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

    public void setItems(List<CustomGroupListItem> items){
        this.items = new ArrayList<>(items);
    }
}
