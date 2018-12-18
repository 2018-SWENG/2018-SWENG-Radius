package ch.epfl.sweng.radius.database;

import java.util.HashMap;
import java.util.Timer;

public class UserUtils extends DBObservable{
    private final Timer timer = new Timer(true);

    private static UserUtils userUtils = null;
    private final int REFRESH_PERIOD = 10; // in seconds
    private static final Database database = Database.getInstance();

    private static final HashMap<String, MLocation> users = new HashMap<>();

    public static UserUtils getInstance(){
        if (userUtils == null)
            userUtils = new UserUtils();
        return userUtils;
    }

    private UserUtils() {}

}
