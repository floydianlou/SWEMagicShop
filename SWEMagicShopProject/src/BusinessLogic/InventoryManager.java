package BusinessLogic;

import Exceptions.InventoryExceptions;
import DAO.InventoryDAO;
import DomainModel.Customer;
import DomainModel.Item;
import java.util.ArrayList;

public class InventoryManager {
    InventoryDAO inventoryDAO;

    public ArrayList<Item> inventoryItems;
    public Customer customer;

    public InventoryManager(InventoryDAO inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
    }

    public InventoryManager() {
        this.inventoryDAO = new InventoryDAO();
    }

    public ArrayList<Item> viewInventory(Customer customer) {
        return inventoryDAO.getInventory(customer.getPersonID());
    }

    public boolean updateInventory(ArrayList<Item> cartItems, Customer customer) throws InventoryExceptions.InventoryUpdateException {
        return inventoryDAO.updateInventory(customer.getPersonID(), cartItems);
    }

}