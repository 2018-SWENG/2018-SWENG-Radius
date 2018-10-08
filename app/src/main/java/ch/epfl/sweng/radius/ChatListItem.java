package ch.epfl.sweng.radius;

public class ChatListItem {
    private int image;
    private String name;

    public ChatListItem(int image, String name){
        this.image = image;
        this.name = name;
    }

    public int getImage() {
        return image;
    }
    public String getName() {
        return name;
    }
}
