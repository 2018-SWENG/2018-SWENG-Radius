package ch.epfl.sweng.radius.utils.customLists.customGroups;

public class CustomGroupListItem {
    private String groupId;
    private String groupeName;
    private String convId;

    public CustomGroupListItem(String groupId, String groupeName, String convId){
        this.groupId = groupId;
        this.groupeName = groupeName;
        this.convId = convId;
    }

   public String getGroupId() { return groupId; }

    public String getConvId() {
        return convId;
    }

    public String getGroupeName() {
        return groupeName;
    }
}
