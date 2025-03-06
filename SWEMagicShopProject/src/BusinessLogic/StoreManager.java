package BusinessLogic;

import DomainModel.Item;
import DAOInterface.ItemDAO;

import java.util.ArrayList;

public class StoreManager {
    ItemDAO itemDAO; //TODO: TO BE INCLUDED IN METHODS WHEN DAO IS IMPLEMENTED, CAN'T BE IMPLEMENTED NOW SINCE WE'RE USING AN INTERFACE

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

    public ArrayList<Item> searchProducs(String query) {
        // ItemDao itemDAO= new ItemDao();
        return itemDAO.searchProductByQuery(query);
    }

    private void addProduct(int itemID, String itemName, String description, String category, int copperValue, boolean isArcane) {
        // ItemDao itemDAO= new ItemDao();
        itemDAO.createItem(itemID, itemName, description, category, copperValue, isArcane);
    }

    private void updateProduct(int itemID, String itemName, String description, String category, int copperValue, boolean isArcane) {
        // ItemDao itemDAO= new ItemDao();
        itemDAO.updateItem(itemID, itemName, description, category, copperValue, isArcane);
    }

}
