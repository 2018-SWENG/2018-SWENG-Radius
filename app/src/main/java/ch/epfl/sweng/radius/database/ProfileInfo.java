package ch.epfl.sweng.radius.database;

public class ProfileInfo {

    private String nickname;
    private String status;
    private String spokenLanguages;

    public ProfileInfo(String userID){
        this.nickname = "New User " + userID;
        this.status = "Hi, I'm new to radius !";
        this.spokenLanguages = ""; // TODO : try to get app current Language
    }

    public String getNickname() {
        return nickname;
    }

    public String getSpokenLanguages() {
        return spokenLanguages;
    }

    public String getStatus() {
        return status;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSpokenLanguages(String spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
