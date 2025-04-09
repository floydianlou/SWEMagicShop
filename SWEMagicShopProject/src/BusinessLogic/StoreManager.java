package BusinessLogic;

import DomainModel.Item;
import ORM.CategoryDAO;
import ORM.ItemDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class StoreManager {

    public StoreManager() {}

    public ArrayList<Item> listProducts() {
        ItemDAO itemDAO= new ItemDAO();
        return itemDAO.getAllItems();
    }

    public Item getProductByID(int itemID) throws IllegalArgumentException {
        ItemDAO itemDAO= new ItemDAO();
        if(itemID <= 0){
            throw new IllegalArgumentException("Invalid item ID!");
        }
        return itemDAO.getItemByID(itemID);
    }

    public void addProduct(String itemName, String description, String category, int copperValue, boolean isArcane, String imagePath) throws IllegalArgumentException, SQLException {
        ItemDAO itemDAO= new ItemDAO();
        if (copperValue <= 0){
            throw new IllegalArgumentException("Price must be greater than zero!");
        }
        if (!Utilities.checkCategory(category)) {
            throw new IllegalArgumentException("Invalid category!");
        }
        if(Utilities.checkBooleanArcane(isArcane) == 0){
            throw new IllegalArgumentException("Arcane must be true or false!");
        }
        itemDAO.createItem(itemName, description, category, copperValue, isArcane, imagePath);
    }

    public void updateProduct(Item updatedItem) throws IllegalArgumentException {
        ItemDAO itemDAO= new ItemDAO();
        if (updatedItem.getCopperValue() <= 0){
            throw new IllegalArgumentException("Price must be greater than zero!");
        }
        if (!Utilities.checkCategory(updatedItem.getItemCategory())) {
            throw new IllegalArgumentException("Invalid category!");
        }
        if(Utilities.checkBooleanArcane(updatedItem.isArcane()) == 0){
            throw new IllegalArgumentException("Arcane must be true or false!");
        }
        itemDAO.updateItem(updatedItem);
    }

    //single function to do the searching in StoreManager
    public ArrayList<Item> searchProductsByName(String itemName) {
        ItemDAO itemDAO= new ItemDAO();
        return itemDAO.getItemsByName(itemName);
    }

    public ArrayList<Item> searchProductsByNameNonArcane(String itemName) {
        ItemDAO itemDAO= new ItemDAO();
        return itemDAO.getItemsByNameNonArcane(itemName);
    }

    public ArrayList<Item> searchProductsByDescription(String itemDescription) {
        ItemDAO itemDAO= new ItemDAO();
        return itemDAO.getItemsByDescription(itemDescription);
    }

    public ArrayList<Item> searchProductsByDescriptionNonArcane(String itemName) {
        ItemDAO itemDAO= new ItemDAO();
        return itemDAO.getItemsByDescriptionNonArcane(itemName);
    }

    public ArrayList<Item> searchProductsByCategory(String itemCategory) throws IllegalArgumentException {
        if(!Utilities.checkCategory(itemCategory)){
            throw new IllegalArgumentException("Invalid category!");
        }
        ItemDAO itemDAO= new ItemDAO();
        return itemDAO.getItemsByCategory(itemCategory);
    }

    public ArrayList<Item> searchProductsByCategoryNonArcane(String itemCategory) throws IllegalArgumentException {
        if(!Utilities.checkCategory(itemCategory)){
            throw new IllegalArgumentException("Invalid category!");
        }
        ItemDAO itemDAO= new ItemDAO();
        return itemDAO.getItemsByCategoryNonArcane(itemCategory);
    }

    public ArrayList<Item> searchProductsByArcane(String arcane) throws IllegalArgumentException {
        ItemDAO itemDAO = new ItemDAO();
        boolean isArcane;

        if (arcane.equalsIgnoreCase("true")) {
            isArcane = true;
        } else if (arcane.equalsIgnoreCase("false")) {
            isArcane = false;
        } else {
            throw new IllegalArgumentException("Invalid arcane value! Use 'true' or 'false'.");
        }

        return itemDAO.getItemsByArcane(isArcane);
    }

    public ArrayList<Item> searchProductsByPrice(int minPrice, int maxPrice) throws IllegalArgumentException {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new IllegalArgumentException("Invalid price range!");
        }

        ItemDAO itemDAO = new ItemDAO();
        return itemDAO.getItemsByPriceRange(minPrice, maxPrice);
    }

    public ArrayList<Item> searchProductsByPriceNonArcane(int minPrice, int maxPrice) throws IllegalArgumentException {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new IllegalArgumentException("Invalid price range!");
        }

        ItemDAO itemDAO = new ItemDAO();
        return itemDAO.getItemsByPriceRangeNonArcane(minPrice, maxPrice);
    }

    public ArrayList<String> getAllCategories() {
        CategoryDAO categoryDAO= new CategoryDAO();
        ArrayList<String> categories = categoryDAO.viewAllCategories();
        return categories;
    }
}