package ch.epfl.sweng.radius.utils;

public class UserInfos {
    private static final long USER_ID = 1234;
    private static String username = "User_1";
    private static String chatWith = "";


    public static long getUserId() {
        return USER_ID;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserInfos.username = username;
    }

    public static String getChatWith() {
        return chatWith;
    }

    public static void setChatWith(String chatWith) {
        UserInfos.chatWith = chatWith;
    }


}
