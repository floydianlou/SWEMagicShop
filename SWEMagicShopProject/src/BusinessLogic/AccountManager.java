package BusinessLogic;

import ORM.AccountDAO;
import DomainModel.Customer;
import DomainModel.Manager;
import DomainModel.Person;
import DomainModel.Species;

import java.sql.SQLException;
import java.util.ArrayList;

public class AccountManager {

    // TODO: after DAO, exceptions need to be managed, functions need to be re-checked.
    public Person login (String email, String password) {
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.loginPerson(email, password);
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
        accountDAO.createCustomerAccount(name, surname, email, password, age, phoneNumber, species);
    }

    public boolean createManagerAccount (String name, String surname, String email, String password) throws IllegalArgumentException {
        AccountDAO accountDAO = new AccountDAO();
        if (!Utilities.checkEmail(email))
            throw new IllegalArgumentException("Invalid email address!");
        if(!Utilities.checkPassword(password))
            throw new IllegalArgumentException("Password format is incorrect!");
        return accountDAO.createManagerAccount(name, surname, email, password);
    }

    // used by SuperUser only
    public ArrayList<Customer> showAllCustomers() {
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.viewAllCustomers();
    }

    // usage in ArcaneRequest viewing by SuperUser only
    public Customer getCustomerByID(int customerID) {
        if (customerID <= 0) {
            System.out.println("Invalid customer ID.");
            return null;
        }
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.getCustomerByID(customerID);
    }

    public boolean updateCustomerAccount(Customer updatedCustomer) {
        if (!Utilities.checkEmail(updatedCustomer.getEmail()))
            throw new IllegalArgumentException("Invalid email address!");
        if(!Utilities.checkPassword(updatedCustomer.getPassword()))
            throw new IllegalArgumentException("Password format is incorrect!");
        if (!Utilities.checkPhone(updatedCustomer.getPhoneNumber()))
            throw new IllegalArgumentException("Invalid phone number");
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.updateCustomerAccount(updatedCustomer);
        }

    public boolean updateManagerAccount (Manager updatedManager) {
        if (!Utilities.checkEmail(updatedManager.getEmail()))
            throw new IllegalArgumentException("Invalid email address!");
        if(!Utilities.checkPassword(updatedManager.getPassword()))
            throw new IllegalArgumentException("Password format is incorrect!");
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.updateManagerAccount(updatedManager);
    }

}