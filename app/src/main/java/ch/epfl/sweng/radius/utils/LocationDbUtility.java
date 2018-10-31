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

public class LocationDbUtility {

    private FirebaseUtility fbUtil;
    private Location myPos;
    // TODO Fix heritage
    private ArrayList<DatabaseObject> otherPos;

    public LocationDbUtility(Location myPos){
        DatabaseReference db = null;
    //    FirebaseAttributes attr = new FirebaseAttributes(FirebaseDatabase.getInstance(),
    //                                                     FirebaseAuth.getInstance(),
    //                                                     db);
   //     this.fbUtil = new FirebaseUtility(attr, myPos, "userLocations");
        this.myPos = myPos;
        this.otherPos = new ArrayList<>();
    }

    public void fetchOtherLocations(){
        //   otherPos = fbUtil.readAllInstances();
        // Remove my own location
        for(int i = 0; i < otherPos.size(); i++){
            if(otherPos.get(i).getID().equals(myPos.getID()))
                otherPos.remove(i);
        }
    }

    public void writeLocation(){
        fbUtil.setInstance(myPos);
        fbUtil.writeInstanceObj();
    }

    public void updatePos(Location newPos){

        this.myPos = newPos;

        fbUtil.setInstance(myPos);
        fbUtil.writeInstanceObj();
    }

    public ArrayList<DatabaseObject> getOtherPos() {
        return otherPos;
    }
}
