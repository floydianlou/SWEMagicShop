package tests.DomainModelTest;

import DomainModel.Item;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void testFullConstructor() {
        Item item = new Item(1, "Sword", "Sharp blade", "Weapons", 5, true, 100, "sword.png");
        assertEquals(1, item.getItemID());
        assertEquals("Sword", item.getItemName());
        assertEquals("Sharp blade", item.getItemDescription());
        assertEquals("Weapons", item.getItemCategory());
        assertEquals(5, item.getItemQuantity());
        assertTrue(item.isArcane());
        assertEquals(100, item.getCopperValue());
        assertEquals("sword.png", item.getImagePath());
    }

    @Test
    void testSettersAndGetters() {
        Item item = new Item(1, "Sword", "Sharp blade", "Weapons", 5, true, 100, "sword.png");

        item.setItemID(10);
        item.setItemName("Axe");
        item.setItemDescription("Heavy axe");
        item.setItemCategory("Weapons");
        item.setItemQuantity(7);
        item.setArcane(false);
        item.setCopperValue(120);
        item.setImagePath("axe.png");

        assertEquals(10, item.getItemID());
        assertEquals("Axe", item.getItemName());
        assertEquals("Heavy axe", item.getItemDescription());
        assertEquals("Weapons", item.getItemCategory());
        assertEquals(7, item.getItemQuantity());
        assertFalse(item.isArcane());
        assertEquals(120, item.getCopperValue());
        assertEquals("axe.png", item.getImagePath());
    }

}