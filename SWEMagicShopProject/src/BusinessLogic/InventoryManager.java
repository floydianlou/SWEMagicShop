package BusinessLogic;


import DAOInterface.InventoryDAO;
import DomainModel.Person;
import DomainModel.Customer;
import DomainModel.Item;
import java.util.List;

public class InventoryManager {
    private InventoryDAO inventoryDAO;
    private Item item;
    private Customer customer;

    public InventoryManager(InventoryDAO inventoryDAO, Item item, Customer customer) {
        this.inventoryDAO = inventoryDAO;
        this.item = item;
        this.customer = customer;

    }



    public List<Item> viewInventory(Customer customer) {

        List<Item> items = inventoryDAO.viewInventory(int clientID);
        return items;
    }

    public void updateInventory(Customer Customer, int ItemID, int Quantity) {
        //update dovrà vedere se nell'inventario è già presente un oggetto e nel caso aumentare solo la quantità
    }
}
