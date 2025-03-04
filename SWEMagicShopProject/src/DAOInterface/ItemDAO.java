package DAOInterface;

import DomainModel.Item;

import java.util.ArrayList;

public interface ItemDAO {

    public Item getItemByID(int ID);
    public Item getItemByName(String itemName);
    public Item getItemByDescription(String itemDescription);
    public void createItem(Item item);
    public void updateItem(Item item);
    public ArrayList<Item> getAllItems();
    public ArrayList<Item> searchProductByQuery(String query);
}
