package BusinessLogic;

import DAOInterface.AccountDAO;
import DomainModel.Customer;
import DomainModel.Person;
import DomainModel.Species;
import java.util.ArrayList;

public class AccountManager {
    Person person;
    AccountDAO accountDAO; // TO BE INCLUDED IN METHODS WHEN DAO IS IMPLEMENTED

    public AccountManager (Person person, AccountDAO accountDAO) {
        this.person = person;
        this.accountDAO = accountDAO;
    }

    // TODO: after DAO, exceptions need to be managed, functions need to be re-checked.
    // methods
    public Person login (String email, String password) {
        return accountDAO.loginPerson(email, password);
    }

    public void createCustomerAccount (String name, String surname, String email, String password, int age,
                                  int phoneNumber, Species species) {
        // WARNING: not exactly sure how to manage this Species object thing yet...so it's probably due to be changed soon.
        accountDAO.createCustomerAccount(name, surname, email, password, age, phoneNumber, species);
    }

    public void createManagerAccount (String name, String surname, String email, String password) {
        // WARNING: not exactly sure how to manage this Species object thing yet...so it's probably due to be changed soon.
        accountDAO.createManagerAccount(name, surname, email, password);
    }

    public ArrayList<Customer> showAllCustomers() {
        return accountDAO.viewAllCustomers();
    }

    public Customer viewSingleCustomer(int customerID) {
        return accountDAO.getCustomerByID(customerID);
    }

}