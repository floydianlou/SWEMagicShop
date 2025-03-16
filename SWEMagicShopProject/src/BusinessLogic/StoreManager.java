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

    public StoreManager() {
        //ONLY FOR TESTING
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

    public boolean addProduct(int itemID, String itemName, String description, String category, int copperValue, boolean isArcane) throws IllegalArgumentException {
        ItemDAO itemDAO= new ItemDAO();
        if (copperValue <= 0){
            throw new IllegalArgumentException("Price must be greater than zero!");
        }
        if (!Utilities.checkCategory(category)) {
            throw new IllegalArgumentException("Invalid category!");
        }
        return itemDAO.createItem(itemID, itemName, description, category, copperValue, isArcane);
    }

    public boolean updateProduct(Item updatedItem) throws IllegalArgumentException {
        ItemDAO itemDAO= new ItemDAO();
        if (updatedItem.getCopperValue() <= 0){
            throw new IllegalArgumentException("Price must be greater than zero!");
        }
        if (!Utilities.checkCategory(updatedItem.getItemCategory())) {
            throw new IllegalArgumentException("Invalid category!");
        }

        return itemDAO.updateItem(updatedItem);
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

    public ArrayList<Item> searchProductsByArcane(String arcane) {
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

    public ArrayList<Item> searchProductsByPrice(int minPrice, int maxPrice) {
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            throw new IllegalArgumentException("Invalid price range!");
        }

        ItemDAO itemDAO = new ItemDAO();
        return itemDAO.getItemsByPriceRange(minPrice, maxPrice);
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
}
