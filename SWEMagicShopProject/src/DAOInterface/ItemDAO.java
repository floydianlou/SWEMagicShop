package DAOInterface;

import DomainModel.Item;

import java.util.ArrayList;

public interface ItemDAO {

    public Item getItemByID(int ID);
    public Item getItemByName(String itemName);
    public Item getItemByDescription(String itemDescription);
    public void createItem(int itemID, String itemName, String description, String category, int copperValue, boolean isArcane);
    public void updateItem(int itemID, String itemName, String description, String category, int copperValue, boolean isArcane);
    public ArrayList<Item> getAllItems();
    public ArrayList<Item> searchProductByQuery(String query);
}
