package ch.epfl.sweng.radius.database;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import ch.epfl.sweng.radius.utils.MapUtility;

public class UserFetchCallback implements CallBackDatabase {

    private MapUtility mapUtility;
    private int radius;

    public UserFetchCallback(double radius) {
        mapUtility = new MapUtility(radius);
        this.radius = (int) radius;

    }
    @Override
    public void onFinish(Object value) {
        for(MLocation loc : (ArrayList<MLocation>) value){
            if(mapUtility.isInRadius(loc, radius)) {
                recordLocationIfVisible(loc);
            }
        }
    }

    @Override
    public void onError(DatabaseError error) {
        Log.e("Firebase", error.getMessage());
    }

    private void recordLocationIfVisible(final MLocation location) {
        final Database database = Database.getInstance();
        database.readObjOnce(new User(location.getID()),
                Database.Tables.USERS, new CallBackDatabase() {
                    @Override
                    public void onFinish(Object value) {
                        if (((User) value).isVisible()) {
                            mapUtility.getOtherPos().put(location.getID(), location);
                        }
                    }
                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Firebase Error", error.getMessage());
                    }
                });
    }

}
