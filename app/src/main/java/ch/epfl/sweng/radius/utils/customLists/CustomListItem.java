package ch.epfl.sweng.radius.utils.customLists;

public abstract class CustomListItem {
    private String itemId;
    private String itemName;
    private String convId;

    public CustomListItem(String itemId, String convId,String itemName){
        this.itemId = itemId;
        this.itemName = itemName;
        this.convId = convId;
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
