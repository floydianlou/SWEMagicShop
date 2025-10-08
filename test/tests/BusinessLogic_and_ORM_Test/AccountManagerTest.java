package tests.BusinessLogic_and_ORM_Test;

import BusinessLogic.AccountManager;
import DomainModel.Customer;
import DomainModel.Manager;
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

public class AccountManagerTest {

    private AccountManager accountManager;
    private Customer customer;
    private Manager manager;
    private int customerID;
    private int managerID;

    @BeforeEach
    void setUp() throws SQLException {
        accountManager = new AccountManager();

        accountManager.createCustomerAccount("Test", "Customer", "example@gmail.com", "TestPass123!", 25, "4562395017", accountManager.getAllSpecies().get(0));
        customer = (Customer) accountManager.login("example@gmail.com", "TestPass123!");
        customerID = customer.getPersonID();

        accountManager.createManagerAccount("Test", "Manager", "manager.example@gmail.com", "ManagerPass123!");
        manager = (Manager) accountManager.login("manager.example@gmail.com", "ManagerPass123!");
        managerID = manager.getPersonID();
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection conn = ConnectionManager.getInstance().getConnection()) {
            // Cancello il customer di test
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM \"Customer\" WHERE customerid = ?")) {
                stmt.setInt(1, customerID);
                stmt.executeUpdate();
            }

            // Cancello il manager di test
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM \"Manager\" WHERE managerid = ?")) {
                stmt.setInt(1, managerID);
                stmt.executeUpdate();
            }
        } finally {
            ConnectionManager.getInstance().closeConnection();
        }
    }

    @Test
    void createCustomerAccount_Success() {
        assertNotNull(customer);
        assertEquals("Test", customer.getName());
    }

    @Test
    void loginCustomer_Success() {
        Customer loggedIn = (Customer) accountManager.login(customer.getEmail(), "TestPass123!");
        assertNotNull(loggedIn);
        assertEquals(customer.getEmail(), loggedIn.getEmail());
    }

    @Test
    void createManagerAccount_Success() {
        assertNotNull(manager);
        assertEquals("Test", manager.getName());
    }

    @Test
    void loginManager_Success() {
        Manager loggedIn = (Manager) accountManager.login(manager.getEmail(), "ManagerPass123!");
        assertNotNull(loggedIn);
        assertEquals(manager.getEmail(), loggedIn.getEmail());
    }

    @Test
    void createCustomerAccount_InvalidEmail() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                accountManager.createCustomerAccount("Test", "Fail", "invalidEmail", "TestPass123!", 25, "5891223478", accountManager.getAllSpecies().get(0))
        );
        assertTrue(ex.getMessage().contains("Invalid email address!"));
    }

    @Test
    void createCustomerAccount_InvalidAge() {
        Species species = accountManager.getAllSpecies().get(0);
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                accountManager.createCustomerAccount("Test", "Fail", "agefail@example.com", "TestPass123!", 5, "5891223478", species)
        );
        assertTrue(ex.getMessage().contains("Age invalid for selected species. Remember: you must be of age to create an account."));
    }

    @Test
    void createCustomerAccount_InvalidPhone() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                accountManager.createCustomerAccount("Test", "Fail", "phonefail@example.com", "TestPass123!", 25, "5", accountManager.getAllSpecies().get(0))
        );
        assertTrue(ex.getMessage().contains("Invalid phone number"));
    }

    @Test
    void createCustomerAccount_EmailAlreadyInUse(){
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                accountManager.createCustomerAccount("Test", "Customer", "example@gmail.com", "AnotherPass123!", 30, "2348952358", accountManager.getAllSpecies().get(0))
        );
        assertTrue(ex.getMessage().contains("This email is already in use."));
    }

    @Test
    void createCustomerAccount_PhoneAlreadyInUse(){
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                accountManager.createCustomerAccount("Test", "Customer", "phone.inuse@gmail.com", "TestPass123!", 25, "4562395017", accountManager.getAllSpecies().get(0))
                );
        assertTrue(ex.getMessage().contains("This phone number is already in use."));
    }

    @Test
    void createCustomerAccount_InvalidPassword(){
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                accountManager.createCustomerAccount("Test", "Customer", "invalid.password@gmail.com", "a#b", 25, "3452846385", accountManager.getAllSpecies().get(0))
                );
        assertTrue(ex.getMessage().contains("Password format is incorrect!"));
    }

    @Test
    void getAllSpecies_NotEmpty() {
        ArrayList<Species> speciesList = accountManager.getAllSpecies();
        assertNotNull(speciesList);
        assertTrue(speciesList.size() > 0);
    }

    @Test
    void getCustomerByID_Success() {
        Customer fetchedCustomer = accountManager.getCustomerByID(customerID);
        assertNotNull(fetchedCustomer);
        assertEquals(customer.getEmail(), fetchedCustomer.getEmail());
    }

    @Test
    void getCustomerByID_NotFound() {
        Customer fetchedCustomer = accountManager.getCustomerByID(-1);
        assertNull(fetchedCustomer);
    }

    @Test
    void updateCustomerAccount_Success() {
        Customer refreshed = accountManager.getCustomerByID(customerID);
        refreshed.setName("UpdatedName");
        accountManager.updateCustomerAccount(refreshed.getPersonID(), refreshed.getName(), refreshed.getSurname(), refreshed.getEmail(), refreshed.getPhoneNumber(), refreshed.getPassword());
        Customer updatedCustomer = accountManager.getCustomerByID(customerID);
        assertEquals("UpdatedName", updatedCustomer.getName());
    }

    @Test
    void updateCustomerAccount_InvalidPhone() {
        Customer refreshed = accountManager.getCustomerByID(customerID);
        refreshed.setPhoneNumber("123");
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                accountManager.updateCustomerAccount(refreshed.getPersonID(), refreshed.getName(), refreshed.getSurname(), refreshed.getEmail(), refreshed.getPhoneNumber(), refreshed.getPassword())
        );
        assertTrue(ex.getMessage().contains("Invalid phone number"));
    }

    @Test
    void updateCustomerAccount_InvalidEmail() {
        Customer refreshed = accountManager.getCustomerByID(customerID);
        refreshed.setEmail("invalidEmail");
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                accountManager.updateCustomerAccount(refreshed.getPersonID(), refreshed.getName(), refreshed.getSurname(), refreshed.getEmail(), refreshed.getPhoneNumber(), refreshed.getPassword())
        );
        assertTrue(ex.getMessage().contains("Invalid email address!"));
    }

    @Test
    void updateManagerAccount_Success() throws SQLException {
        manager.setName("UpdatedManager");
        accountManager.updateManagerAccount(manager);
        Manager updatedManager = (Manager) accountManager.login(manager.getEmail(), "ManagerPass123!");
        assertEquals("UpdatedManager", updatedManager.getName());
    }

}
