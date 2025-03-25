package GUI.controller;
import DomainModel.Item;

public class ItemViewManager {
    private static ItemViewManager instance;

    private Item productSelected;

    private ItemViewManager() {}

    public static ItemViewManager getInstance() {
        if (instance == null) {
            instance = new ItemViewManager();
        }
        return instance;
    }

    public void setProductSelected(Item productSelected) { this.productSelected = productSelected; }
    public Item getProductSelected() { return productSelected; }
    public void clearProductSelected() { productSelected = null; }

}