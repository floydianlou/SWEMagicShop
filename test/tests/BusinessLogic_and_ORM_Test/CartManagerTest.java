package tests.BusinessLogic_and_ORM_Test;

import BusinessLogic.CartManager;
import DomainModel.Customer;
import DomainModel.Item;
import Exceptions.OrderExceptions.ItemNotInCartException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartManagerTest {

    private Customer testCustomer;
    private CartManager cartManager;
    private Item testItem1;
    private Item testItem2;

    @BeforeEach
    void setUp() {

        testCustomer = new Customer(999);

        // Creo il CartManager e ne faccio uno spy (ci permette di mockare metodi specifici)
        cartManager = Mockito.spy(new CartManager(testCustomer));

        testItem1 = new Item( 1, "Magic Card", "A powerful magic card", "Weapons", 2, false, 50);
        testItem2 = new Item( 2, "Health Potion", "Restores health", "Potions", 5, false, 10);

        // Mockiamo i metodi che toccano il filesystem
        doNothing().when(cartManager).saveCart();
        doReturn(new ArrayList<Item>()).when(cartManager).loadCartItems(anyString());
    }

    @AfterEach
    void tearDown() {
        if (cartManager != null) {
            cartManager.clearCart();
            cartManager.closeCartSession();  // ferma eventuale scheduler
        }
    }

    @Test
    void addItemToCart() {
        cartManager.addItemToCart(testItem1);
        assertEquals(1, cartManager.getCartItems().size());
        assertEquals(2, cartManager.getCartItems().get(0).getItemQuantity());
    }

    @Test
    void removeItemFromCart() throws ItemNotInCartException {
        cartManager.addItemToCart(testItem1);
        cartManager.removeItemFromCart(testItem1);
        assertTrue(cartManager.getCartItems().isEmpty());
    }

    @Test
    void removeItemFromCartThrows() {
        assertThrows(ItemNotInCartException.class, () -> cartManager.removeItemFromCart(testItem1));
    }

    @Test
    void increaseItemQuantity() {
        cartManager.addItemToCart(testItem1);
        cartManager.increaseItemQuantity(testItem1);
        assertEquals(3, cartManager.getCartItems().get(0).getItemQuantity());
    }

    @Test
    void reduceItemQuantity() {
        cartManager.addItemToCart(testItem1);
        cartManager.reduceItemQuantity(testItem1);
        assertEquals(1, cartManager.getCartItems().get(0).getItemQuantity());

        cartManager.reduceItemQuantity(testItem1);
        assertTrue(cartManager.getCartItems().isEmpty());
    }

    @Test
    void setItemQuantity() {
        cartManager.addItemToCart(testItem1);
        cartManager.setItemQuantity(testItem1, 10);
        assertEquals(10, cartManager.getCartItems().get(0).getItemQuantity());
    }

    @Test
    void clearCart() {
        cartManager.addItemToCart(testItem1);
        cartManager.addItemToCart(testItem2);
        cartManager.clearCart();
        assertTrue(cartManager.getCartItems().isEmpty());
    }
}

