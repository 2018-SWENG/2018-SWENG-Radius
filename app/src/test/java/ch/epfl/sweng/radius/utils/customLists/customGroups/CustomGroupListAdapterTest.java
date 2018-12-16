package ch.epfl.sweng.radius.utils.customLists.customGroups;

import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private LinearLayout mockedLinearLayout = Mockito.mock(LinearLayout.class);
    private ImageView mockedImg = Mockito.mock(ImageView.class);
    private CustomGroupListAdapter customGroupListAdapter;
    ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();

    @Before
    public void setUp() throws Exception {
        list.add(new CustomListItem("1","0", "3"));
        mockedView.titleAndStatus = mockedLinearLayout;
        mockedView.imgViewIcon = mockedImg;
    }

    @Test
    public void onBindViewHolder() {
        customGroupListAdapter = new CustomGroupListAdapter(list, MapUtilityTest.context);
        if(mockedView!=null) {
            customGroupListAdapter.onBindViewHolder(mockedView, 0);
        }
    }
}