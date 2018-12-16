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

public class CustomGroupListAdapterTest {
    private CustomListAdapter.ViewHolder mockedView = Mockito.mock(CustomListAdapter.ViewHolder.class);
    private TextView mockedText = Mockito.mock(TextView.class);
    private ImageView mockedImg = Mockito.mock(ImageView.class);
    private CustomGroupListAdapter customGroupListAdapter;
    ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();

    @Before
    public void setUp() throws Exception {
        list.add(new CustomListItem("1","0", "3"));
        mockedView.txtViewTitle = mockedText;
        mockedView.imgViewIcon = mockedImg;
    }

    @Test
    public void onBindViewHolder() {
        customGroupListAdapter = new CustomGroupListAdapter(list, MapUtilityTest.context);
        customGroupListAdapter.onBindViewHolder(mockedView, 0);
    }
}