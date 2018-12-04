package ch.epfl.sweng.radius.utils.customLists;

import android.util.Log;
import android.widget.Button;

import ch.epfl.sweng.radius.R;

public class CustomListItem {
    private String itemId;
    private String itemName;
    private String convId;
    private int profilePic;
    private boolean isRemoveButtonVisible = false;

    public CustomListItem(String itemId, String convId,String itemName){
      //  Log.e("MessageList", "Param of item "+ itemId + " " + convId + " " + itemName);
        this.itemId = itemId;
        Log.e("CustomListItem: ", itemId);
        this.itemName = itemName;
        this.convId = convId;
        this.profilePic = R.drawable.user_photo_default; // Getting the Profile pictures here.
    }

    public void setRemoveButtonVisibility(boolean isVisible) {
        isRemoveButtonVisible = isVisible;
    }

    public boolean getRemoveButtonVisibility() {
        return isRemoveButtonVisible;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public String getItemId() {
        return itemId;
    }

    public String getConvId() {
        return convId;
    }

    public String getItemName() {
        return itemName;
    }
}
