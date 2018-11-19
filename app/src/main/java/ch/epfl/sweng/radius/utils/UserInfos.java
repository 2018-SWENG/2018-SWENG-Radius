package ch.epfl.sweng.radius.utils;

import ch.epfl.sweng.radius.database.User;

public class UserInfos {

    private static final String USER_ID = "1";

    private static User CURRENT_USER = null;

    public static String getUserId() {
        return USER_ID;
    }

    public static User getCurrentUser() {
        return CURRENT_USER;
    }

    public static void setCurrentUser(User currentUser) {
        CURRENT_USER = currentUser;
    }
}
