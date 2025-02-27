package DomainModel;

public class Item {

    private int itemID;
    private String itemName;
    private String itemDescription;
    private String itemCategory;
    private int itemQty;
    private boolean isArcane;
    private int itemCP;
    private int itemSP;
    private int itemGP;

    public Item(int itemID, String itemName, String itemDescription, String itemCategory, int itemQty, boolean isArcane, int itemCP, int itemSP, int itemGP) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemCategory = itemCategory;
        this.itemQty = itemQty;
        this.isArcane = isArcane;
        this.itemCP = itemCP;
        this.itemSP = itemSP;
        this.itemGP = itemGP;
    }

    public String getItemData(){
        return "ItemID: " + itemID + "\n ItemName: " + itemName + "\n ItemDescription: " + itemDescription + "\n ItemCategory: " + itemCategory + "\n ItemQuantity: " + itemQty + "\n GP: " + itemGP + "\n SP: " + itemSP + "\n CP: " + itemCP + "\n IsArcane: " + isArcane;
    }

    //GETTER AND SETTER

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public boolean isArcane() {
        return isArcane;
    }

    public void setArcane(boolean arcane) {
        isArcane = arcane;
    }

    public int getItemCP() {
        return itemCP;
    }

    public void setItemCP(int itemCP) {
        this.itemCP = itemCP;
    }

    public int getItemSP() {
        return itemSP;
    }

    public void setItemSP(int itemSP) {
        this.itemSP = itemSP;
    }

    public int getItemGP() {
        return itemGP;
    }

    public void setItemGP(int itemGP) {
        this.itemGP = itemGP;
    }

}
