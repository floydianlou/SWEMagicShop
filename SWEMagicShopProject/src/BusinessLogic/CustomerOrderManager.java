package BusinessLogic;

import DAOInterface.OrderDAO;
import DomainModel.Customer;
import DomainModel.Order;

import java.util.ArrayList;

public class CustomerOrderManager {
    OrderDAO orderDAO;

    public ArrayList<Order> viewAllOrders() {
        // OrderDAO orderDAO = new OrderDAO;
        return orderDAO.getAllOrders();
    }

    public ArrayList<Order> viewCustomerOrders(Customer customer) {
        // OrderDAO orderDAO = new OrderDAO;
        return orderDAO.getCustomerOrders(customer);
    }

    public void createOrder() {
        // TODO: deactivate "Checkout" button if cart is empty in GUI
        
    }

}