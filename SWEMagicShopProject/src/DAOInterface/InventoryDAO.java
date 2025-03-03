package DAOInterface;

import DomainModel.Item;
import DomainModel.Species;
import DomainModel.Person;
import DomainModel.Customer;

import java.util.ArrayList;
import java.util.List;

// JUST AN INTERFACE TO MAKE BUSINESS LOGIC FIRST !!


public interface InventoryDAO {


    public void viewInventory(int ClientID);
    public boolean updateInventory(Customer customer, ArrayList<Item> orderItems);
    // this function should update rows affected and add new rows by itself, it would be highly inefficient for inventoryManager
    // to do this and pass to dao the entire inventory to be re-written


}


