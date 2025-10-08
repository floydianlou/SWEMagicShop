package BusinessLogic;

import DAO.AccountDAO;
import DomainModel.Customer;
import DomainModel.Manager;
import DomainModel.Person;
import DomainModel.Species;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountManager {
    public Person login (String email, String password) {
        AccountDAO accountDAO = new AccountDAO();
        try {
            return accountDAO.loginPerson(email, password); }
        catch (SQLException e) {
            System.err.println("SQL error while logging Person in : " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
            throw new RuntimeException("A database error occurred.");
        }
    }

    public void createCustomerAccount (String name, String surname, String email, String password, int age,
                                  String phoneNumber, Species species) throws IllegalArgumentException, SQLException {
        AccountDAO accountDAO = new AccountDAO();
        if (!Utilities.checkEmail(email))
            throw new IllegalArgumentException("Invalid email address!");
        if(!Utilities.checkPassword(password))
            throw new IllegalArgumentException("Password format is incorrect!");
        if (!Utilities.checkAgeLimit(species, age))
            throw new IllegalArgumentException("Age invalid for selected species. Remember: you must be of age to create an account.");
        if (!Utilities.checkPhone(phoneNumber))
            throw new IllegalArgumentException("Invalid phone number");
        try {
            accountDAO.createCustomerAccount(name, surname, email, password, age, phoneNumber, species);
        } catch (SQLException e) {
        throw new IllegalArgumentException(e.getMessage()); }
    }

    public boolean createManagerAccount (String name, String surname, String email, String password) throws IllegalArgumentException, SQLException {
        AccountDAO accountDAO = new AccountDAO();
        if (!Utilities.checkEmail(email))
            throw new IllegalArgumentException("Invalid email address!");
        if(!Utilities.checkPassword(password))
            throw new IllegalArgumentException("Password format is incorrect!");
        return accountDAO.createManagerAccount(name, surname, email, password);
    }

    public ArrayList<Customer> showAllCustomers() {
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.viewAllCustomers();
    }

    public Customer getCustomerByID(int customerID) {
        if (customerID <= 0) {
            System.out.println("Invalid customer ID.");
            return null;
        }
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.getCustomerByID(customerID);
    }

    public Customer updateCustomerAccount(int customerID, String name, String surname,
                                          String email, String phone, String password) throws IllegalArgumentException{
        if (!Utilities.checkEmail(email))
            throw new IllegalArgumentException("Invalid email address!");
        if(!Utilities.checkPassword(password))
            throw new IllegalArgumentException("Password format is incorrect!");
        if (!Utilities.checkPhone(phone))
            throw new IllegalArgumentException("Invalid phone number!");
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.updateCustomerAccount(customerID, name, surname, email, phone, password);
    }

    public void updateManagerAccount(Manager updatedManager) throws SQLException {
        if (!Utilities.checkEmail(updatedManager.getEmail()))
            throw new IllegalArgumentException("Invalid email address!");
        if (!Utilities.checkPassword(updatedManager.getPassword()))
            throw new IllegalArgumentException("Password format is incorrect!");

        new AccountDAO().updateManagerAccount(updatedManager);
    }

    public ArrayList<Species> getAllSpecies () {
        AccountDAO accountDAO = new AccountDAO();
        try {
            return accountDAO.getAllSpecies(); }
        catch (SQLException e) {
            System.err.println("SQL error while gathering Species: " + e.getMessage());
            if (e.getMessage().contains("failed to connect")) {
                throw new RuntimeException("Database is offline, please try again later.");
            }
            throw new RuntimeException("A database error occured.");
        }
    }
}