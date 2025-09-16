package BusinessLogic;

import Exceptions.InventoryExceptions;
import ORM.InventoryDAO;
import DomainModel.Customer;
import DomainModel.Item;
import java.util.ArrayList;

// would likely need to be created as soon as login is made just like cartManager and load all inventory items with constructor
public class InventoryManager {
    InventoryDAO inventoryDAO;

    public ArrayList<Item> inventoryItems;
    public Customer customer;

    public InventoryManager(Customer customer) {
        this.customer = customer;
        this.inventoryItems = loadInventory(customer);
    }

    public InventoryManager(InventoryDAO inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

    public InventoryManager() {
        this.inventoryDAO = new InventoryDAO();
    }

    public ArrayList<Item> loadInventory(Customer customer) {
        // TODO NOT YET TESTED
        return inventoryDAO.getInventory(customer.getPersonID());
    }

    public ArrayList<Item> viewInventory(Customer customer) {
        return inventoryDAO.getInventory(customer.getPersonID());
    }

    public boolean updateInventory(ArrayList<Item> cartItems, Customer customer) throws InventoryExceptions.InventoryUpdateException {
        return inventoryDAO.updateInventory(customer.getPersonID(), cartItems);
    }

}