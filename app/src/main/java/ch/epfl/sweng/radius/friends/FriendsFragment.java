package ch.epfl.sweng.radius.friends;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.DBObserver;
import ch.epfl.sweng.radius.utils.TabAdapter;


public class FriendsFragment extends Fragment{
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        adapter = new TabAdapter(this.getChildFragmentManager());
        adapter.addFragment(new FriendsTab(), "Friends");
        adapter.addFragment(new RequestsTab(), "Requests");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
