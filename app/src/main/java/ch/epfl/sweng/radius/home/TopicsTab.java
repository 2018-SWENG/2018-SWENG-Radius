package ch.epfl.sweng.radius.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.radius.R;


public class TopicsTab extends Fragment {


    public TopicsTab() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.topics_tab, container, false);

        // Inflate the layout for this fragment
        return view;
    }
}