package ch.epfl.sweng.radius.utils.customLists.customGroups;

import ch.epfl.sweng.radius.utils.customLists.CustomListItem;

public class CustomGroupListItem extends CustomListItem{

    public CustomGroupListItem(String groupId, String groupeName, String convId){
        super(groupId,convId,groupeName);
    }

   public String getGroupId() { return super.getItemId(); }

    public String getConvId() {
        return super.getConvId();
    }

    public String getGroupeName() {
        return super.getItemName();
    }
}
