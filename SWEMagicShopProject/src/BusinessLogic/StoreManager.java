package BusinessLogic;

import DomainModel.Item;
import ORM.CategoryDAO;
import ORM.ItemDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class StoreManager {
    ItemDAO itemDAO;
    CategoryDAO categoryDAO;

    public StoreManager(ItemDAO itemDAO, CategoryDAO categoryDAO) {
        this.itemDAO = itemDAO;
        this.categoryDAO = categoryDAO;
    }

    public StoreManager() {
    }

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

    public boolean addProduct(String itemName, String description, String category, int copperValue, boolean isArcane, String imagePath) throws IllegalArgumentException {
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
        return itemDAO.createItem(itemName, description, category, copperValue, isArcane, imagePath);
    }

    public boolean updateProduct(Item updatedItem) throws IllegalArgumentException {
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
        return itemDAO.updateItem(updatedItem);
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

    //All In One search function
    public ArrayList<Item> searchProductsBy(String option, String search) throws IllegalArgumentException {
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
            case "arcane": {
                boolean searchArcane;
                if (search.equalsIgnoreCase("true")) {
                    searchArcane = true;
                } else if (search.equalsIgnoreCase("false")) {
                    searchArcane = false;
                } else {
                    throw new IllegalArgumentException("Invalid arcane value! Use 'true' or 'false'.");
                }
                return itemDAO.getItemsByArcane(searchArcane);
            }
            case "price":
                try {
                    String[] prices = search.split("-");
                    if (prices.length != 2) {
                        throw new IllegalArgumentException("Invalid price format! Use 'min-max'.");
                    }
                    int minPrice = Integer.parseInt(prices[0].trim());
                    int maxPrice = Integer.parseInt(prices[1].trim());

                    if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
                        throw new IllegalArgumentException("Invalid price range!");
                    }

                    return itemDAO.getItemsByPriceRange(minPrice, maxPrice);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Price range must be in numeric format 'min-max'.");
                }
            default: return itemDAO.getAllItems();
        }
    }

    public ArrayList<String> getAllCategories() {
        CategoryDAO categoryDAO= new CategoryDAO();
        ArrayList<String> categories = categoryDAO.viewAllCategories();
        return categories;
    }
}
