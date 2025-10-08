package BusinessLogic;

import Exceptions.OrderExceptions;
import DAO.OrderDAO;
import DomainModel.Customer;
import DomainModel.Item;
import DomainModel.Order;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;

public class CustomerOrderManager {

    private static final long statusToShipped = 20;
    private static final long statusToDelivered = 50;
    private static final ZoneId ZONE = ZoneId.of("Europe/Rome");
    private static final DateTimeFormatter PG_TS = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 1, 6, true).optionalEnd()
            .toFormatter();


    public CustomerOrderManager() {}

    public ArrayList<Order> viewAllOrders() {
        OrderDAO orderDAO = new OrderDAO();
        ArrayList<Order> orders = orderDAO.viewAllOrders();
        for (Order order : orders) {
            checkOrderStatus(order);
        }
        return orders;
    }

    public ArrayList<Item> viewOrderItems(int orderID) {
        OrderDAO orderDAO = new OrderDAO();
        return orderDAO.viewOrderItems(orderID);
    }

    public ArrayList<Order> viewCustomerOrders(Customer customer) {
        OrderDAO orderDAO = new OrderDAO();
        ArrayList<Order> orders = orderDAO.getCustomerOrders(customer.getPersonID());
        for (Order order : orders) {
            checkOrderStatus(order);
        }
        return orders;
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

    public void checkOrderStatus(Order order) {
        long now = Instant.now().getEpochSecond();
        long created = LocalDateTime.parse(order.getOrderDate(), PG_TS)
                .atZone(ZONE).toEpochSecond();
        long elapsed = now - created;

        if ("Delivered".equalsIgnoreCase(order.getOrderStatus())) return;

        OrderDAO orderDao = new OrderDAO();

        if ("In progress".equalsIgnoreCase(order.getOrderStatus()) && elapsed >= statusToShipped) {
            order.setOrderStatus("Shipping");
            orderDao.updateOrderStatus(order.getOrderID(), 2);
        } else if ("Shipping".equalsIgnoreCase(order.getOrderStatus()) && elapsed >= statusToDelivered) {
            order.setOrderStatus("Delivered");
            orderDao.updateOrderStatus(order.getOrderID(),3);
        } else {
            return;
        }
    }

}