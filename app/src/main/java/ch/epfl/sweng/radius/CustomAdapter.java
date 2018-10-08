package ch.epfl.sweng.radius;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * CustomAdapter class that extends the ArrayAdapter class
 * to work with the custom chat list view layout.
 *
 * @author  Pinar Ayaz
 */
public class CustomAdapter extends ArrayAdapter<ChatListItem> {


    List<ChatListItem> chatList;
    Context context;
    int resource;

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
        textViewName.setText(chatListItem.getName());

        final int clickedPic = chatListItem.getImage();
        final String clickedName = chatListItem.getName();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BrowseProfilesActivity.class);
                intent.putExtra("Clicked Picture", clickedPic);
                intent.putExtra("Clicked Name", clickedName);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
