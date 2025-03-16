package BusinessLogic;

import DomainModel.Item;
import ORM.ItemDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class StoreManager {
    ItemDAO itemDAO;

    public StoreManager(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    public ArrayList<Item> listProducts() {
        // ItemDao itemDAO= new ItemDao();
        return itemDAO.getAllItems();
    }

    public Item getProductByID(int itemID) {
        // ItemDao itemDAO= new ItemDao();
        return itemDAO.getItemByID(itemID);
    }

    //public ArrayList<Item> searchProducs(String query) {
        // ItemDao itemDAO= new ItemDao();
        //return itemDAO.searchProductByQuery(query);
    //}

    private void addProduct(int itemID, String itemName, String description, String category, int copperValue, boolean isArcane) throws IllegalArgumentException {
        // ItemDao itemDAO= new ItemDao();
        if (copperValue <= 0){
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (!Utilities.validCategory(category)) {
            throw new IllegalArgumentException("Invalid category");
        }
        itemDAO.createItem(itemID, itemName, description, category, copperValue, isArcane);
    }

    private void updateProduct(Item updatedItem) throws IllegalArgumentException {
        // ItemDao itemDAO= new ItemDao();
        if (updatedItem.getCopperValue() <= 0){
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (!Utilities.checkCategory(updatedItem.getItemCategory())) {
            throw new IllegalArgumentException("Invalid category");
        }
        itemDAO.updateItem(updatedItem);
    }

}
