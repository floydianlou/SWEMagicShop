package tests.DomainModelTest;

import DomainModel.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testFullConstructor() {
        Order order = new Order(1, "Pending", 500, "2025-09-14", 100, "John Doe", "john@example.com");
        assertEquals(1, order.getOrderID());
        assertEquals("Pending", order.getOrderStatus());
        assertEquals(500, order.getTotalCP());
        assertEquals("2025-09-14", order.getOrderDate());
        assertEquals(100, order.getCustomerID());
        assertEquals("John Doe", order.getCustomerName());
        assertEquals("john@example.com", order.getCustomerEmail());
    }

    @Test
    void testMinimalConstructor() {
        Order order = new Order(2, "Shipped", 300, "2025-09-15");
        assertEquals(2, order.getOrderID());
        assertEquals("Shipped", order.getOrderStatus());
        assertEquals(300, order.getTotalCP());
        assertEquals("2025-09-15", order.getOrderDate());
        assertEquals(0, order.getCustomerID()); // default int
        assertNull(order.getCustomerName());
        assertNull(order.getCustomerEmail());
    }

    @Test
    void testSettersAndGetters() {
        Order order = new Order(3, "Delivered", 200, "2025-09-16");

        order.setOrderID(10);
        order.setOrderStatus("Cancelled");
        order.setTotalCP(100);

        assertEquals(10, order.getOrderID());
        assertEquals("Cancelled", order.getOrderStatus());
        assertEquals(100, order.getTotalCP());
    }
}
