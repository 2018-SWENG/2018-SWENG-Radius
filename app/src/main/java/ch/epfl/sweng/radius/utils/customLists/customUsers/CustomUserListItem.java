package ch.epfl.sweng.radius.utils.customLists.customUsers;

import ch.epfl.sweng.radius.R;
import ch.epfl.sweng.radius.database.User;
import ch.epfl.sweng.radius.utils.customLists.CustomListItem;

public class CustomUserListItem extends CustomListItem{

    private int profilePic;

    public CustomUserListItem(String itemId, String convId,String itemName){
        super(itemId,convId,itemName);
        this.profilePic = R.drawable.user_photo_default;
    }

    public int getFriendProfilePic() {
        return profilePic;
    }

    public String getUserId() {
        return super.getItemId();
    }

    public String getConvId() {
        return super.getConvId();
    }

    public String getUserName() {
        return super.getItemName();
    }

}
