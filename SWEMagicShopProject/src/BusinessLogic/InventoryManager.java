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

    public ArrayList<Item> viewInventory(int clientID) {
        if (customer == null || customer.getPersonID() != clientID) {

            return new ArrayList<>();
        }
        return new ArrayList<>(inventoryItems);
    }


    public boolean updateInventory(ArrayList<Item> cartItems, Customer customer) throws InventoryExceptions.InventoryUpdateException {
        InventoryDAO inventoryDAO = new InventoryDAO();
        return inventoryDAO.updateInventory(customer.getPersonID(), cartItems);
    }
}
