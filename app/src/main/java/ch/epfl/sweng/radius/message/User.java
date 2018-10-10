package ch.epfl.sweng.radius.message;

public class User {
    private String nickname;
    private long userId;

    public User(String nickname, long userId) {
        this.nickname = nickname;
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public long getUserId() {
        return userId;
    }
}
