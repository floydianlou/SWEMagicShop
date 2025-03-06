package BusinessLogic;

import DAOInterface.OrderDAO;
import DomainModel.Customer;
import DomainModel.Item;
import DomainModel.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CustomerOrderManager {

    OrderDAO orderDAO;

    public CustomerOrderManager() {
        // TODO don't know if something should be added...
    }

    public ArrayList<Order> viewAllOrders() {
        // OrderDAO orderDAO = new OrderDAO();
        return orderDAO.getAllOrders();
    }

    public ArrayList<Order> viewCustomerOrders(Customer customer) {
        // OrderDAO orderDAO = new OrderDAO();
        return orderDAO.getCustomerOrders(customer);
    }

    public int createOrder(Customer customer, CartManager cartManager, WalletManager walletManager, InventoryManager inventoryManager) {
        // TODO: deactivate "Checkout" button if cart is empty in GUI.
        // TODO: manage exceptions later when DAO is implemented
        if (cartManager.getCartItems().isEmpty()) {
            System.out.println("Your cart is empty, you can't make an order!");
            return 0;
        }

        int orderTotal = totalCost(cartManager.getCartItems());
        if (!walletManager.withdrawFunds(orderTotal, customer)) {
            System.out.println("You don't have enough funds to make an order!");
            return -2; }

        LocalDateTime orderDate = LocalDateTime.now();

        // OrderDAO orderDAO = new OrderDAO();
        int newOrderID = orderDAO.saveNewOrder(orderDate, orderTotal, customer.getPersonID(), cartManager.getCartItems());
        if (newOrderID == -1) {
            System.out.println("Your order could not be saved!");
            return newOrderID; }

        boolean inventoryUpdated = inventoryManager.updateInventory(cartManager.getCartItems(), customer);

        if (!inventoryUpdated) {
            System.out.println("Your order could not be saved to inventory!");
            orderDAO.deleteOrder(newOrderID);
            return -3;}

        System.out.println("Order successful!");
        cartManager.clearCart();
        return newOrderID;
    }

    private int totalCost(ArrayList<Item> cartItems) {
        int total = 0;
        for (Item item : cartItems) {
            total = total + item.getCopperValue();
        }
        return total;
    }

}