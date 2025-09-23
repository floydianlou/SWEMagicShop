package DomainModel;

public class Item {

    private int itemID;
    private String itemName;
    private String itemDescription;
    private String itemCategory;
    private int itemQuantity;
    private boolean isArcane;
    private int copperValue;
    private String imagePath;

    // contructor with attribute quantity for classes Cart and Inventory
    public Item(int itemID, String itemName, String itemDescription, String itemCategory, int itemQuantity, boolean isArcane, int copperValue, String imagePath) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemCategory = itemCategory;
        this.itemQuantity = itemQuantity;
        this.isArcane = isArcane;
        this.copperValue = copperValue;
        this.imagePath = imagePath;
    }

    // constructor without image path
    public Item(int itemID, String itemName, String itemDescription, String itemCategory, int itemQuantity, boolean isArcane, int copperValue) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemCategory = itemCategory;
        this.itemQuantity = itemQuantity;
        this.isArcane = isArcane;
        this.copperValue = copperValue;
    }

    // contructor without attribute quantity
    public Item(int itemID, String itemName, String itemDescription, String itemCategory, boolean isArcane, int CPprice, String imagePath) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemCategory = itemCategory;
        this.isArcane = isArcane;
        this.copperValue = CPprice;
        this.imagePath = imagePath;
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

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public boolean isArcane() {
        return isArcane;
    }

    public void setArcane(boolean arcane) {
        isArcane = arcane;
    }

    public int getCopperValue() {
        return copperValue;
    }

    public void setCopperValue(int copperValue) {
        this.copperValue = copperValue;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}