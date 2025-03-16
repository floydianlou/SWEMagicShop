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
        ItemDAO itemDAO= new ItemDAO();
        return itemDAO.getAllItems();
    }

    public Item getProductByID(int itemID) {
        ItemDAO itemDAO= new ItemDAO();
        if(itemID <= 0){
            System.out.println("Invalid item ID!");
            return null;
        }
        return itemDAO.getItemByID(itemID);
    }

    private void addProduct(int itemID, String itemName, String description, String category, int copperValue, boolean isArcane) throws IllegalArgumentException {
        ItemDAO itemDAO= new ItemDAO();
        if (copperValue <= 0){
            throw new IllegalArgumentException("Price must be greater than zero!");
        }
        if (!Utilities.checkCategory(category)) {
            throw new IllegalArgumentException("Invalid category!");
        }
        itemDAO.createItem(itemID, itemName, description, category, copperValue, isArcane);
    }

    private void updateProduct(Item updatedItem) throws IllegalArgumentException {
        ItemDAO itemDAO= new ItemDAO();
        if (updatedItem.getCopperValue() <= 0){
            throw new IllegalArgumentException("Price must be greater than zero!");
        }
        if (!Utilities.checkCategory(updatedItem.getItemCategory())) {
            throw new IllegalArgumentException("Invalid category!");
        }
        itemDAO.updateItem(updatedItem);
    }

    //single function to do the searching in StoreManager
    public ArrayList<Item> searchProducsByName(String itemName) {
        ItemDAO itemDAO= new ItemDAO();
        return itemDAO.getItemsByName(itemName);
    }

    public ArrayList<Item> searchProductsByDescription(String itemDescription) {
        ItemDAO itemDAO= new ItemDAO();
        return itemDAO.getItemsByDescription(itemDescription);
    }

    public ArrayList<Item> searchProductsByCategory(String itemCategory) {
        if(!Utilities.checkCategory(itemCategory)){
            throw new IllegalArgumentException("Invalid category!");
        }
        ItemDAO itemDAO= new ItemDAO();
        return itemDAO.getItemsByCategory(itemCategory);
    }

    //All In One search function
    public ArrayList<Item> searchProductsBy(String option, String search) {
        ItemDAO itemDAO= new ItemDAO();
        switch (option) {
            case "name": return itemDAO.getItemsByName(search);
            case "description": return itemDAO.getItemsByDescription(search);
            case "category": {
                if(!Utilities.checkCategory(search)) {
                    throw new IllegalArgumentException("Invalid category!");
                }
                return itemDAO.getItemsByCategory(search);
            }
            default: return itemDAO.getAllItems();
        }
    }
}
