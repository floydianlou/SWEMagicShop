package tests.BusinessLogic_and_ORM_Test;

import BusinessLogic.*;
import DomainModel.*;
import Exceptions.OrderExceptions;
import DAO.ConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerOrderManagerTest {

    private CustomerOrderManager customerOrderManager;
    private AccountManager accountManager;
    private WalletManager walletManager;
    private StoreManager storeManager;
    private CartManager cartManager;
    private Customer customer;
    private ArrayList<Species> species;
    private Item item;
    private int orderID;
    private Wallet wallet;
    private ArrayList<Item> orderItems;

    @BeforeEach
    void setUp() throws SQLException {
        customerOrderManager = new CustomerOrderManager();
        storeManager = new StoreManager();
        accountManager = new AccountManager();
        walletManager = new WalletManager();
        species = accountManager.getAllSpecies();
        orderItems = new ArrayList<Item>();

        // prendo un item dal database
        item = storeManager.getProductByID(1);
        item.setItemQuantity(2); // setto la quantità a 2
        item.setCopperValue(50); // setto il valore in CP a 50

        // creo il mio customer che fa un ordine
        accountManager.createCustomerAccount("Test", "Example", "example@gmail.com", "testuser", 67, "0909898789", species.get(0));
        customer = (Customer) accountManager.login("example@gmail.com", "testuser");
        cartManager = new CartManager(customer);
        walletManager.addFunds(2,5,0, customer); // aggiungo fondi al wallet del customer 250 CP

        cartManager.addItemToCart(item); // aggiungo 2 oggetti di tipo item al carrello costo tolate 100 CP

    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection conn = ConnectionManager.getInstance().getConnection()) {

            // 1. cancello gli OrderItems legati all'ordine di test
            if (orderID != 0) {
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM \"OrderItems\" WHERE orderid = ?")) {
                    stmt.setInt(1, orderID);
                    stmt.executeUpdate();
                }

                // 2. cancello l'ordine stesso
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM \"Order\" WHERE orderid = ?")) {
                    stmt.setInt(1, orderID);
                    stmt.executeUpdate();
                }
            }

            // 3. cancello l'inventario del customer di test
            if (customer != null) {
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM \"Inventory\" WHERE customerid = ?")) {
                    stmt.setInt(1, customer.getPersonID());
                    stmt.executeUpdate();
                }

                // 4. cancello il customer di test
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM \"Customer\" WHERE customerid = ?")) {
                    stmt.setInt(1, customer.getPersonID());
                    stmt.executeUpdate();
                }
            }
        }finally {
            // chiudiamo la connessione statica
            ConnectionManager.getInstance().closeConnection();
        }
    }


    @Test
    void createOrderTest() {
        orderID = customerOrderManager.createOrder(customer, cartManager, walletManager);
        orderItems = customerOrderManager.viewOrderItems(orderID);
        wallet = walletManager.getWalletByCustomer(customer);
        assertNotNull(orderID);
        assertEquals(1, customerOrderManager.viewOrderItems(orderID).size()); // controllo che ci sia un solo tipo di item nel mio ordine
        assertEquals(2, customerOrderManager.viewOrderItems(orderID).get(0).getItemQuantity()); // controllo che la quantità dell'item sia 2
        assertEquals(150, wallet.getCPbalance()); // controllo che il wallet del customer sia stato scalato di 100 CP
    }

    @Test
    void cartIsEmptyTest() {
        cartManager.clearCart();

        Exception exception = assertThrows(OrderExceptions.EmptyCartException.class, () -> {
            orderID = customerOrderManager.createOrder(customer, cartManager, walletManager);
        });

        assertEquals("Your cart is empty.", exception.getMessage());
    }

    @Test
    void notEnoughFundsTest() throws SQLException {

        // svuoto il wallet del customer
        walletManager.withdrawFunds(250, customer); // tolgo 250 CP

        Exception exception = assertThrows(OrderExceptions.MissingFundsException.class, () -> {
            orderID = customerOrderManager.createOrder(customer, cartManager, walletManager);
        });

        assertEquals("Not enough funds.", exception.getMessage());
    }
}
