package ch.epfl.sweng.radius.friendsList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.browseProfiles.CustomListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder>{

    private FriendsListItem[] items;
    Context context;

    public FriendsListAdapter(FriendsListItem[] items, Context context) {
        this.items = items;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FriendsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_list_item_layout, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.txtViewTitle.setText(items[position].getFriendName());
        viewHolder.imgViewIcon.setImageResource(items[position].getFriendProfilePic());

        FriendsListItem item = items[position];
        final int clickedPic = item.getFriendProfilePic();
        final String clickedName = item.getFriendName();

        new CustomListener(clickedPic, clickedName).setCustomOnClick(viewHolder.imgViewIcon, context);

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
        return items.length;
    }
}
