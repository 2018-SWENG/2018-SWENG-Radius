package ch.epfl.sweng.radius.database;

public class UserUtils extends DBObservable{

    private static UserUtils userUtils = null;

    /**
     * Static singleton method to return the instance of UserUtils
     * @return userUtils: the singleton instance of UserUtils
     */
    public static UserUtils getInstance(){
        if (userUtils == null)
            userUtils = new UserUtils();
        return userUtils;
    }

    //Private constructor since UserUtils is singleton
    private UserUtils() {}

}
