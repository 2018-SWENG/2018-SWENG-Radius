package ch.epfl.sweng.radius.utils.customLists.customGroups;

import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import ch.epfl.sweng.radius.utils.MapUtilityTest;
import ch.epfl.sweng.radius.utils.customLists.CustomListAdapter;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;
import ch.epfl.sweng.radius.utils.customLists.customTopics.CustomTopicListAdapter;

public class CustomTopicListAdapterTest {
    private CustomListAdapter.ViewHolder mockedView = Mockito.mock(CustomListAdapter.ViewHolder.class);
    private TextView mockedText = Mockito.mock(TextView.class);
    private ImageView mockedImg = Mockito.mock(ImageView.class);
    private CustomTopicListAdapter test;
    ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();

    @Before
    public void setUp() throws Exception {
        list.add(new CustomListItem("1","0", "3"));
        mockedView.txtViewTitle = mockedText;
        mockedView.imgViewIcon = mockedImg;
    }

    @Test
    public void onBindViewHolder() {
        test = new CustomTopicListAdapter(list, MapUtilityTest.context, new ArrayList<Integer>());
        test.onBindViewHolder(mockedView, 0);
    }

}
