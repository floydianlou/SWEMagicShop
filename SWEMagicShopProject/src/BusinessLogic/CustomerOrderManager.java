package BusinessLogic;
import Exceptions.OrderExceptions;
import ORM.OrderDAO;
import DomainModel.Customer;
import DomainModel.Item;
import DomainModel.Order;
import java.util.ArrayList;

public class CustomerOrderManager {

    public CustomerOrderManager() {
    }

    public ArrayList<Order> viewAllOrders() {
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.viewAllOrders();
    }

    public ArrayList<Item> viewOrderItems(int orderID) {
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.viewOrderItems(orderID);
    }

    public ArrayList<Order> viewCustomerOrders(Customer customer) {
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.getCustomerOrders(customer.getPersonID());
    }

    public int createOrder(Customer customer, CartManager cartManager, WalletManager walletManager) throws OrderExceptions.OrderSaveException, OrderExceptions.EmptyCartException, OrderExceptions.MissingFundsException {
            if (cartManager.getCartItems().isEmpty()) {
                throw new OrderExceptions.EmptyCartException("Your cart is empty.");
            }

            int orderTotal = totalCost(cartManager.getCartItems());
            if (!walletManager.withdrawFunds(orderTotal, customer)) {
                throw new OrderExceptions.MissingFundsException("Not enough funds.");
            }

            OrderDAO orderDAO = new OrderDAO();
            int newOrderID = orderDAO.saveNewOrder(orderTotal, customer.getPersonID(), cartManager.getCartItems());

            System.out.println("Order successful!");
            cartManager.clearCart();
            return newOrderID;
    }

    private int totalCost(ArrayList<Item> cartItems) {
        int total = 0;
        for (Item item : cartItems) {
            total = total + item.getCopperValue()*item.getItemQuantity();
        }
        return total;
    }

}

