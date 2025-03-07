package DAOInterface;

import DomainModel.Customer;
import DomainModel.Item;
import DomainModel.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface OrderDAO {

    ArrayList<Order> getAllOrders();
    ArrayList<Order> getCustomerOrders(Customer customer);
    int saveNewOrder(LocalDateTime orderDate, int orderTotal, int customerID, ArrayList<Item> cartItems);
    void deleteOrder(int orderID);
}
