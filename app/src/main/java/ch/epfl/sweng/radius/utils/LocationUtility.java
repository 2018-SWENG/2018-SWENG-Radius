package ch.epfl.sweng.radius.utils;

import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ch.epfl.sweng.radius.database.ChatLogs;
import ch.epfl.sweng.radius.database.DatabaseObject;
import ch.epfl.sweng.radius.database.Location;

public class LocationUtility {

    private Location myPos;
    // TODO Fix heritage
    private ArrayList<Location> otherPos;

    public LocationUtility(Location myPos){
        this.myPos = myPos;
        this.otherPos = new ArrayList<>();
    }

    public void fetchOtherLocations(){

        // Remove my own location
        for(int i = 0; i < otherPos.size(); i++){
            if(otherPos.get(i).getID().equals(myPos.getID()))
                otherPos.remove(i);
        }
    }

    public void writeLocation(){

    }

    public void updatePos(Location newPos){

        this.myPos = newPos;

    }

    public ArrayList<Location> getOtherPos() {
        return otherPos;
    }
}
