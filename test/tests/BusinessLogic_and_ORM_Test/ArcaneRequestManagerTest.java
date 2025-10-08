package tests.BusinessLogic_and_ORM_Test;

import BusinessLogic.AccountManager;
import BusinessLogic.ArcaneRequestManager;
import DomainModel.ArcaneRequest;
import DomainModel.Customer;
import DomainModel.Species;
import DAO.ConnectionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ArcaneRequestManagerTest {

    private ArcaneRequestManager arcaneManager;
    private AccountManager accountManager;
    private Customer customer;
    private int testCustomerID;

    @BeforeEach
    void setUp() throws SQLException {
        arcaneManager = new ArcaneRequestManager();
        accountManager = new AccountManager();
        ArrayList<Species> species = accountManager.getAllSpecies();

        // creo un customer di test
        accountManager.createCustomerAccount("Test", "Example", "example@gmail.com", "testuser", 67, "0909898789", species.get(0));
        customer = (Customer) accountManager.login("example@gmail.com", "testuser");
        testCustomerID = customer.getPersonID();

    }

    @AfterEach
    void tearDown() throws SQLException {
        // cancello le richieste di test e il customer
        try (Connection conn = ConnectionManager.getInstance().getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM \"ArcaneRequest\" WHERE customerid = ?")) {
                stmt.setInt(1, testCustomerID);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM \"Customer\" WHERE customerid = ?")) {
                stmt.setInt(1, testCustomerID);
                stmt.executeUpdate();
            }
        }finally {
            // chiudiamo la connessione statica
            ConnectionManager.getInstance().closeConnection();
        }
    }

    @Test
    void makeArcaneRequest_Success() {
        boolean result = arcaneManager.makeArcaneRequest(customer);
        assertTrue(result, "The arcane request should be successful.");
        assertEquals(1, arcaneManager.viewNumberOfArcaneRequests(testCustomerID), "There should be one arcane request in the system.");
    }

    @Test
    void makeArcaneRequest_CustomerIsArcane() {
       customer.setArcaneMember(true);
       boolean result = arcaneManager.makeArcaneRequest(customer);
       assertFalse(result, "The arcane request should fail because the customer is already an arcane member.");
    }

    @Test
    void makeArcaneRequest_Already5RequestMade() {
        for (int i = 0; i < 5; i++) {
            arcaneManager.makeArcaneRequest(customer);
        }
        boolean result = arcaneManager.makeArcaneRequest(customer);
        assertFalse(result, "The arcane request should fail because the customer has already made 5 requests.");
    }

    @Test
    void viewRequestsByCustomer() {
        arcaneManager.makeArcaneRequest(customer);
        ArrayList<ArcaneRequest> requests = arcaneManager.viewRequestsByCustomer(testCustomerID);

        assertNotNull(requests, "The list of requests should not be null.");
        assertEquals(1, requests.size(), "There should be exactly one request.");
        assertEquals(testCustomerID, requests.get(0).getCustomerID(), "The request should belong to the test customer.");
    }

    @Test
    void viewNumberOfArcaneRequests() {
        arcaneManager.makeArcaneRequest(customer);
        int numberOfRequests = arcaneManager.viewNumberOfArcaneRequests(testCustomerID);

        assertEquals(1, numberOfRequests, "There should be exactly one arcane request for the test customer.");
    }

    @Test
    void viewAllArcaneRequests() {
        arcaneManager.makeArcaneRequest(customer);
        ArrayList<ArcaneRequest> allRequests = arcaneManager.viewAllRequests();

        assertNotNull(allRequests, "The list of all requests should not be null.");
        assertTrue(allRequests.size() >= 1, "There should be at least one arcane request in the system.");
    }

    @Test
    void approveArcaneRequest() {
        arcaneManager.makeArcaneRequest(customer);
        ArrayList<ArcaneRequest> requests = arcaneManager.viewRequestsByCustomer(testCustomerID);
        ArcaneRequest request = requests.get(0);
        boolean result = arcaneManager.approveRequest(request);
        assertTrue(result, "The arcane request should be approved successfully.");

    }

    @Test
    void approveArcaneRequest_InvalidRequest() {
        ArcaneRequest request = null;
        boolean result = arcaneManager.approveRequest(request);
        assertFalse(result, "Request approval should fail for a null request.");
    }

    @Test
    void rejectArcaneRequest() {
        arcaneManager.makeArcaneRequest(customer);
        ArrayList<ArcaneRequest> requests = arcaneManager.viewRequestsByCustomer(testCustomerID);
        ArcaneRequest request = requests.get(0);
        boolean result = arcaneManager.rejectRequest(request);
        assertTrue(result, "The arcane request should be rejected successfully.");
    }
}
