package tests.BusinessLogic_and_ORM_Test;

import BusinessLogic.InventoryManager;
import Exceptions.InventoryExceptions;
import ORM.InventoryDAO;
import DomainModel.Customer;
import DomainModel.Item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class InventoryManagerTest {

    private InventoryManager inventoryManager;
    private InventoryDAO inventoryDAO;
    private Customer customer;

    @BeforeEach
    public void setUp() throws InventoryExceptions.InventoryUpdateException {
        inventoryDAO = mock(InventoryDAO.class);
        inventoryManager = new InventoryManager(inventoryDAO);
        customer = mock(Customer.class);

        // Mocking customer attributes
        when(customer.getPersonID()).thenReturn(1);

        // Mocking inventoryDAO methods
        when(inventoryDAO.getInventory(1)).thenReturn(new ArrayList<>());
        when(inventoryDAO.updateInventory(eq(1), ArgumentMatchers.<ArrayList<Item>>any())).thenReturn(true);
    }

    @AfterEach
    public void tearDown() {
        inventoryManager = null;
        inventoryDAO = null;
        customer = null;
    }

    @Test
    public void testViewInventory() {
        ArrayList<Item> inventory = inventoryManager.viewInventory(customer);
        assertEquals(0, inventory.size());
        verify(inventoryDAO, times(1)).getInventory(1);
    }

    @Test
    public void testUpdateInventory() throws Exception {
        ArrayList<Item> cart = new ArrayList<>();
        boolean result = inventoryManager.updateInventory(cart, customer);
        assertTrue(result);
        verify(inventoryDAO, times(1)).updateInventory(1, cart);
    }
}

