package DAOInterface;

import DomainModel.Customer;
import DomainModel.Item;
import DomainModel.Order;
import Exceptions.OrderExceptions;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface OrderDAO {

    ArrayList<Order> viewAllOrders();
    ArrayList<Order> getCustomerOrders(int customerID);
    int saveNewOrder(int orderTotal, int customerID, ArrayList<Item> cartItems) throws OrderExceptions.OrderSaveException;
    void deleteOrder(int orderID);
}
