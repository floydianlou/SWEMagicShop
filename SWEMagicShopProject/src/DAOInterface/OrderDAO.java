package DAOInterface;

import DomainModel.Customer;
import DomainModel.Order;

import java.util.ArrayList;

public interface OrderDAO {

    ArrayList<Order> getAllOrders();
    ArrayList<Order> getCustomerOrders(Customer customer);
}
