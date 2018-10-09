package ch.epfl.sweng.radius.message;

public class User {
    private String nickname;
    private String profileUrl;
    private long userId;

    public User(String nickname, long userId){
        this.nickname=nickname;
        this.userId=userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public long getUserId() {
        return userId;
    }
}
