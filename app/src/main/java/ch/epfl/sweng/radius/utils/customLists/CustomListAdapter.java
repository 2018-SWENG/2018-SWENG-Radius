package ch.epfl.sweng.radius.utils.customLists;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.R;
import de.hdodenhof.circleimageview.CircleImageView;


public abstract class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder> {

    protected List<CustomListItem> items;
    protected Context context;

    public CustomListAdapter(List<CustomListItem> items, Context context) {
        this.items = new ArrayList<>(items);
        this.context = context;
    }

    public CustomListAdapter(CustomListAdapter customListAdapter) {
        this.items = new ArrayList<>(customListAdapter.items);
        this.context = customListAdapter.context;
    }

    @NonNull
    @Override
    public CustomListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,  int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_item_layout, null);

        // create ViewHolder
        CustomListAdapter.ViewHolder viewHolder = new CustomListAdapter.ViewHolder(itemLayoutView);
        return viewHolder;    }

    public abstract void onBindViewHolder(@NonNull CustomListAdapter.ViewHolder viewHolder, int i);

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
        this.items = new ArrayList<>(items);
    }
}
