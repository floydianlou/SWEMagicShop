package tests.DomainModelTest;

import DomainModel.ArcaneRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArcaneRequestTest {

    @Test
    void testFullConstructor() {
        ArcaneRequest request = new ArcaneRequest(1, "Pending", 100, "John Doe", "2024-10-01");
        assertEquals(1, request.getRequestID());
        assertEquals("Pending", request.getRequestStatus());
        assertEquals(100, request.getCustomerID());
        assertEquals("John Doe", request.getCustomerName());
    }

    @Test
    void testConstructorWithOnlyCustomerID() {
        ArcaneRequest request = new ArcaneRequest(200);
        assertEquals(200, request.getCustomerID());
        assertEquals(0, request.getRequestID());
        assertNull(request.getRequestStatus());
        assertNull(request.getCustomerName());
    }

    @Test
    void testSettersAndGetters() {
        ArcaneRequest request = new ArcaneRequest(1, "Pending", 100, "John Doe", "2024-10-01");

        request.setRequestID(10);
        request.setCustomerID(200);
        request.setRequestStatus("Approved");

        assertEquals(10, request.getRequestID());
        assertEquals(200, request.getCustomerID());
        assertEquals("Approved", request.getRequestStatus());
    }

}
