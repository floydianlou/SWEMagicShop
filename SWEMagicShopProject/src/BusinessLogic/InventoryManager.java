package BusinessLogic;

import DAOInterface.InventoryDAO;
import DomainModel.Person;
import DomainModel.Customer;
import DomainModel.Item;

import java.util.ArrayList;
import java.util.List;

// would likely need to be created as soon as login is made just like cartManager and load all inventory items with constructor
public class InventoryManager {
    public InventoryDAO inventoryDAO;
    public ArrayList<Item> inventoryItems;
    public Customer customer;

    public InventoryManager(InventoryDAO inventoryDAO, Customer customer) {
        this.inventoryDAO = inventoryDAO;
        this.customer = customer;
        this.inventoryItems = loadInventory(customer);
    }

    public ArrayList<Item> loadInventory(Customer customer) {
        // [...]
        return inventoryItems;
        // TODO this function needs to be implemented
    }

    public void viewInventory(int ClientID) {
        // TODO
    }

    public boolean updateInventory(ArrayList<Item> cartItems, Customer customer) {
        // InventoryDAO inventoryDAO = new InventoryDAO();
        return inventoryDAO.updateInventory(customer, cartItems);
    }
}
