package tests.BusinessLogic_and_ORM_Test;

import BusinessLogic.StoreManager;
import DAO.ConnectionManager;
import DAO.ItemDAO;
import DomainModel.Item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class StoreManagerTest {

    private StoreManager storeManager;
    private ItemDAO itemDAO;
    private Item arcaneItem;
    private Item nonArcaneItem;

    @BeforeEach
    public void setUp() {
        itemDAO = mock(ItemDAO.class);
        storeManager = new StoreManager(itemDAO);


        // Mocking item attributes
        String itemName = "Magic Wand";
        String description = "A wand imbued with magical powers.";
        String category = "Weapons";
        int copperValue = 150;
        boolean isArcane = true;
        String imagePath = "/images/magic_wand.png";
        arcaneItem = new Item(1, itemName, description, category, isArcane, copperValue, imagePath);

        String itemName2 = "Iron Shield";
        String description2 = "A sturdy iron shield.";
        String category2 = "Armor";
        int copperValue2 = 100;
        boolean isArcane2 = false;
        String imagePath2 = "/images/iron_shield.png";
        nonArcaneItem = new Item(2, itemName2, description2, category2, isArcane2, copperValue2, imagePath2);

        // Mocking itemDAO methods

        when(itemDAO.getItemByID(1)).thenReturn(arcaneItem);
        when(itemDAO.getItemByID(2)).thenReturn(nonArcaneItem);
        when(itemDAO.getAllItems()).thenReturn(new ArrayList<Item>() {{
            add(arcaneItem);
            add(nonArcaneItem);
        }});
        when(itemDAO.getItemsByName("Magic Wand")).thenReturn(new ArrayList<Item>() {{
            add(arcaneItem);
        }});
        when(itemDAO.getItemsByCategory("Weapons")).thenReturn(new ArrayList<Item>() {{
            add(arcaneItem);
        }});
        when(itemDAO.getItemsByArcane(true)).thenReturn(new ArrayList<Item>() {{
            add(arcaneItem);
        }});
        when(itemDAO.getItemsByArcane(false)).thenReturn(new ArrayList<Item>() {{
            add(nonArcaneItem);
        }});
        when(itemDAO.getItemsByPriceRange(100, 200)).thenReturn(new ArrayList<Item>() {{
            add(arcaneItem);
            add(nonArcaneItem);
        }});
    }

    @AfterEach
    public void tearDown() {
        storeManager = null;
        itemDAO = null;
        arcaneItem = null;
        nonArcaneItem = null;

        // chiudiamo la connessione statica per sicurezza dato che con i metodi statici Utilities
        // checkCategory il categoryDAO fa accesso al database
        try {
            ConnectionManager.getInstance().closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddItem() throws SQLException {
        String itemName = "Magic Wand";
        String description = "A wand imbued with magical powers.";
        String category = "Weapons";
        int copperValue = 150;
        boolean isArcane = true;
        String imagePath = "/images/magic_wand.png";

        storeManager.addProduct(itemName, description, category, copperValue, isArcane, imagePath);

        verify(itemDAO, times(1)).createItem(itemName, description, category, copperValue, isArcane, imagePath);
    }

    @Test
    public void testAddItem_InvalidPrice() throws SQLException {
        String itemName = "Magic Wand";
        String description = "A wand imbued with magical powers.";
        String category = "Weapons";
        int copperValue = 0; // Invalid price
        boolean isArcane = true;
        String imagePath = "/images/magic_wand.png";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            storeManager.addProduct(itemName, description, category, copperValue, isArcane, imagePath);
        });

        assertEquals("Price must be greater than zero!", exception.getMessage());
        verify(itemDAO, never()).createItem(itemName, description, category, copperValue, isArcane, imagePath);
    }

    @Test
    public void testAddItem_InvalidCategory() throws SQLException {
        String itemName = "Magic Wand";
        String description = "A wand imbued with magical powers.";
        String category = "InvalidCategory"; // Invalid category
        int copperValue = 150;
        boolean isArcane = true;
        String imagePath = "/images/magic_wand.png";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            storeManager.addProduct(itemName, description, category, copperValue, isArcane, imagePath);
        });

        assertEquals("Invalid category!", exception.getMessage());
        verify(itemDAO, never()).createItem(itemName, description, category, copperValue, isArcane, imagePath);
    }


    @Test
    public void testUpdateItem() {
        arcaneItem.setCopperValue(200);
        storeManager.updateProduct(arcaneItem);
        verify(itemDAO, times(1)).updateItem(arcaneItem);
    }

    @Test
    public void testGetProductById() {
        Item result = storeManager.getProductByID(1);
        assertEquals(arcaneItem, result);
        verify(itemDAO, times(1)).getItemByID(1);
    }

    @Test
    public void testListProducts() {
        ArrayList<Item> result = storeManager.listProducts();
        assertEquals(2, result.size());
        assertEquals(arcaneItem, result.get(0));
        assertEquals(nonArcaneItem, result.get(1));
        verify(itemDAO, times(1)).getAllItems();
    }

    @Test
    public void testSearchProductsByName() {
        ArrayList<Item> result = storeManager.searchProductsByName("Magic Wand");
        assertEquals(1, result.size());
        assertEquals(arcaneItem, result.get(0));
        verify(itemDAO, times(1)).getItemsByName("Magic Wand");
    }

    @Test
    public void testSearchProductsByCategory() {
        ArrayList<Item> result = storeManager.searchProductsByCategory("Weapons");
        assertEquals(1, result.size());
        assertEquals(arcaneItem, result.get(0));
        verify(itemDAO, times(1)).getItemsByCategory("Weapons");
    }

    @Test
    public void testSearchProductsByArcane_True() {
        ArrayList<Item> result = storeManager.searchProductsByArcane("true");
        assertEquals(1, result.size());
        assertEquals(arcaneItem, result.get(0));
        verify(itemDAO, times(1)).getItemsByArcane(true);
    }

    @Test
    public void testSearchProductsByArcane_False() {
        ArrayList<Item> result = storeManager.searchProductsByArcane("false");
        assertEquals(1, result.size());
        assertEquals(nonArcaneItem, result.get(0));
        verify(itemDAO, times(1)).getItemsByArcane(false);
    }

    @Test
    public void testSearchProductsByPrice() {
        int minPrice = 100;
        int maxPrice = 200;

        ArrayList<Item> result = storeManager.searchProductsByPrice(minPrice, maxPrice);

        assertEquals(2, result.size());
        assertEquals(arcaneItem, result.get(0));
        assertEquals(nonArcaneItem, result.get(1));
        verify(itemDAO, times(1)).getItemsByPriceRange(minPrice, maxPrice);
    }

    @Test
    public void testSearchProductsByPrice_InvalidRange() {
        int minPrice = 200;
        int maxPrice = 100;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            storeManager.searchProductsByPrice(minPrice, maxPrice);
        });

        assertEquals("Invalid price range!", exception.getMessage());
        verify(itemDAO, never()).getItemsByPriceRange(anyInt(), anyInt());
    }

}
