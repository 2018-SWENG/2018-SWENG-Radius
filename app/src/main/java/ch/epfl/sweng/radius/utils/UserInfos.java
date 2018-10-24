package ch.epfl.sweng.radius.utils;

public class UserInfos {

    private static final String USER_ID = "1";
    private static String USER_USERNAME = "User_1";
    private static String chatWith = "";

    public static String getUserId() {
        return USER_ID;
    }

    public static String getUsername() {
        return USER_USERNAME;
    }

    public static void setUsername(String username) {
        UserInfos.USER_USERNAME = username;
    }

    public static String getChatWith() {
        return chatWith;
    }

    public static void setChatWith(String chatWith) {
        UserInfos.chatWith = chatWith;
    }


}
