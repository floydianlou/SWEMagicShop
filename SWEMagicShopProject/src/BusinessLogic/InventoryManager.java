package BusinessLogic;

import Exceptions.InventoryExceptions;
import ORM.InventoryDAO;
import DomainModel.Person;
import DomainModel.Customer;
import DomainModel.Item;

import java.util.ArrayList;

// would likely need to be created as soon as login is made just like cartManager and load all inventory items with constructor
public class InventoryManager {
    public ArrayList<Item> inventoryItems;
    public Customer customer;

    public InventoryManager(Customer customer) {
        this.customer = customer;
        this.inventoryItems = loadInventory(customer);
    }

    public ArrayList<Item> loadInventory(Customer customer) {
        // TODO NOT YET TESTED
        InventoryDAO inventoryDAO = new InventoryDAO();
        return inventoryDAO.getInventory(customer.getPersonID());
    }

    public void viewInventory(int ClientID) {
        // TODO
    }
}
