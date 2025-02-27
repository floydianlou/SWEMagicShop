package BusinessLogic;

import DAOInterface.AccountDAO;
import DomainModel.Customer;
import DomainModel.Manager;
import DomainModel.Person;
import DomainModel.Species;
import java.util.ArrayList;

public class AccountManager {
    Person person;
    AccountDAO accountDAO; //TODO: TO BE INCLUDED IN METHODS WHEN DAO IS IMPLEMENTED, CAN'T BE IMPLEMENTED NOW SINCE WE'RE USING AN INTERFACE

    public AccountManager (Person person, AccountDAO accountDAO) {
        this.person = person;
        this.accountDAO = accountDAO;
    }

    // TODO: after DAO, exceptions need to be managed, functions need to be re-checked.
    public Person login (String email, String password) {
        return accountDAO.loginPerson(email, password);
    }

    public void createCustomerAccount (String name, String surname, String email, String password, int age,
                                  String phoneNumber, Species species) throws IllegalArgumentException {
        // AccountDAO accountDAO = new AccountDAO();
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

    public void createManagerAccount (String name, String surname, String email, String password) throws IllegalArgumentException {
        // AccountDAO accountDAO = new AccountDAO();
        if (!Utilities.checkEmail(email))
            throw new IllegalArgumentException("Invalid email address!");
        if(!Utilities.checkPassword(password))
            throw new IllegalArgumentException("Password format is incorrect!");
        accountDAO.createManagerAccount(name, surname, email, password);
    }

    // used by SuperUser only
    public ArrayList<Customer> showAllCustomers() {
        // AccountDAO accountDAO = new AccountDAO();
        return accountDAO.viewAllCustomers();
    }

    // usage in ArcaneRequest viewing by SuperUser only
    public Customer viewSingleCustomer(int customerID) {
        // AccountDAO accountDAO = new AccountDAO();
        return accountDAO.getCustomerByID(customerID);
    }

    public void updateCustomerAccount(Customer updatedCustomer) {
        // data will be checked by GUI, which will update the customer object and pass it to business logic
        // AccountDAO accountDAO = new AccountDAO();
        accountDAO.updateCustomerAccount(updatedCustomer);
        }

    public void updateManagerAccount (Manager updatedManager) {
        // data will be checked by GUI, which will update the manager object and pass it to business logic
        // AccountDAO accountDAO = new AccountDAO();
        accountDAO.updateManagerAccount(updatedManager);
    }

}