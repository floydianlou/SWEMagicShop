package BusinessLogic;
import Exceptions.InventoryExceptions;
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

    public ArrayList<Order> viewCustomerOrders(Customer customer) {
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.getCustomerOrders(customer.getPersonID());
    }

    public int createOrder(Customer customer, CartManager cartManager, WalletManager walletManager, InventoryManager inventoryManager) {
        // TODO: deactivate "Checkout" button if cart is empty in GUI.
        // TODO: manage exceptions later when GUI implemented
        try {
        if (cartManager.getCartItems().isEmpty()) {
            throw new OrderExceptions.EmptyCartException("Your cart is empty, you can't make an order!");
        }

        int orderTotal = totalCost(cartManager.getCartItems());
        // commented for testing
//        if (!walletManager.withdrawFunds(orderTotal, customer)) {
//            throw new OrderExceptions.MissingFundsException ("You don't have enough funds to make an order!"); }

        OrderDAO orderDAO = new OrderDAO();
        int newOrderID = orderDAO.saveNewOrder(orderTotal, customer.getPersonID(), cartManager.getCartItems());

        boolean inventoryUpdated = inventoryManager.updateInventory(cartManager.getCartItems(), customer);

        if (!inventoryUpdated) {
            System.out.println("Your order could not be saved to inventory!");
            // orderDAO.deleteOrder(newOrderID);
        }

        System.out.println("Order successful!");
        cartManager.clearCart();
        return newOrderID; }
        catch (OrderExceptions.EmptyCartException | OrderExceptions.OrderSaveException |
               InventoryExceptions.InventoryUpdateException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0; // GUI SHOULD KNOW WHAT 0 MEANS
    }

    private int totalCost(ArrayList<Item> cartItems) {
        int total = 0;
        for (Item item : cartItems) {
            total = total + item.getCopperValue();
        }
        return total;
    }

}