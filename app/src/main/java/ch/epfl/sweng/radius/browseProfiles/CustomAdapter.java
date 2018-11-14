package ch.epfl.sweng.radius.browseProfiles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.User;

/**
 * CustomAdapter class that extends the ArrayAdapter class
 * to work with the custom chat list view layout.
 */
public class CustomAdapter extends ArrayAdapter<ChatListItem> {
    List<ChatListItem> chatList;
    Context context;
    int resource;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param context Parameter 1.
     * @param resource Parameter 2.
     * @param chatList Parameter 3.
     */

    public CustomAdapter(Context context, int resource, List<ChatListItem> chatList) {
        super(context, resource, chatList);
        this.context = context;
        this.resource = resource;
        this.chatList = chatList;
    }

    //this will return the ListView Item as a View
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null, false);

        ImageView imageView = view.findViewById(R.id.profilePic);
        TextView textViewName = view.findViewById(R.id.username);

        ChatListItem chatListItem = chatList.get(position);

        imageView.setImageDrawable(context.getResources().getDrawable(chatListItem.getImage()));
        textViewName.setText(chatListItem.getUser().getNickname());

        final int clickedPic = chatListItem.getImage();
        final User clickedUser = chatListItem.getUser();

        new CustomListener(clickedPic, clickedUser).setCustomOnClick(imageView, context);
        return view;
    }
}
