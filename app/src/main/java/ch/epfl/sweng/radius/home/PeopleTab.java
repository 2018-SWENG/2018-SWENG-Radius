package ch.epfl.sweng.radius.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.radius.R;


public class PeopleTab extends Fragment {


    public PeopleTab() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.people_tab, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.peopleTab);
        //mock data for testing purposes
        PeopleListItem items[] = { new PeopleListItem("John Doe",R.drawable.image1),
                new PeopleListItem("Jane Doe",R.drawable.image2),
                new PeopleListItem("Alison Star",R.drawable.image3),
                new PeopleListItem("Mila Noon",R.drawable.image4),
                new PeopleListItem("David Doyle",R.drawable.image5)};

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        PeopleListAdapter adapter = new PeopleListAdapter(items, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Inflate the layout for this fragment
        return view;
    }
}
