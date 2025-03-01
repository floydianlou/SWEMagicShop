package BusinessLogic;

import DAOInterface.InventoryDAO;
import DomainModel.Person;
import DomainModel.Customer;
import DomainModel.Item;
import java.util.List;

public class InventoryManager {
    public InventoryDAO inventoryDAO;
    public Item item;
    public Customer customer;

    public InventoryManager(InventoryDAO inventoryDAO, Item item, Customer customer) {
        this.inventoryDAO = inventoryDAO;
        this.item = item;
        this.customer = customer;

    }



    public void viewInventory(int ClientID) {


    }

    public void updateInventory(int ClientID, int ItemID, int Quantity) {
        //update dovrà vedere se nell'inventario è già presente un oggetto e nel caso aumentare solo la quantità
    }
}
