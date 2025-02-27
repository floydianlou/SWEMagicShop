package DAOInterface;

import DomainModel.Item;
import DomainModel.Species;
import DomainModel.Person;
import DomainModel.Customer;
import java.util.List;

// JUST AN INTERFACE TO MAKE BUSINESS LOGIC FIRST !!


public interface InventoryDAO {


    public void viewInventory(int ClientID);
    public void updateInventory(int ClientID, int ItemID, int Quantity);


}


