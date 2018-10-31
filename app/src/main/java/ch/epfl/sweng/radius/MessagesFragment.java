package ch.epfl.sweng.radius;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.radius.browseProfiles.ChatListItem;
import ch.epfl.sweng.radius.browseProfiles.CustomAdapter;
import ch.epfl.sweng.radius.message.MessageListActivity;


public class MessagesFragment extends Fragment {
    List<ChatListItem> chatList;
    ListView listView;

    public MessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessagesFragment newInstance(String param1, String param2) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        chatList = new ArrayList<>();
        listView = view.findViewById(R.id.listView);

        chatList.add(new ChatListItem(R.drawable.image1, "john doe"));
        chatList.add(new ChatListItem(R.drawable.image2, "jane doe"));
        chatList.add(new ChatListItem(R.drawable.image3, "alison star"));
        chatList.add(new ChatListItem(R.drawable.image4, "mila noon"));
        chatList.add(new ChatListItem(R.drawable.image5, "david doyle"));

        CustomAdapter adapter = new CustomAdapter(getActivity(), R.layout.chat_list_view, chatList);
        listView.setAdapter(adapter);

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object objecty = listView.getItemAtPosition(position);
                    /*
                    write you handling code like...
                    String st = "sdcard/";
                    File f = new File(st+o.toString());
                    // do whatever u want to do with 'f' File object
                    */

                Intent intent = new Intent(getActivity(), MessageListActivity.class);
                Bundle b = new Bundle();
                b.putString("otherUserId", "theOtherUserId");
                b.putString("chatId", "theChatId");
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);

            }
        });

        return view;
    }
}
