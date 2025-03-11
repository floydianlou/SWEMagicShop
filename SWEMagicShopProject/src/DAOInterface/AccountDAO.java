package DAOInterface;

import DomainModel.Manager;
import DomainModel.Person;
import DomainModel.Customer;
import DomainModel.Species;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AccountDAO {
    public boolean createCustomerAccount (String name, String surname, String email, String password, int age,
                                       String phoneNumber, Species species) throws SQLException;
    // this function should check for existent customer emails (maybe return boolean then??) then create account.
    public boolean createManagerAccount (String name, String surname, String email, String password);
    public Person loginPerson (String email, String password);
    public ArrayList<Customer> viewAllCustomers();
    public Customer getCustomerByID (int customerID); // should check if ID exists then return Customer object from data in database.

    public void updateCustomerAccount(Customer customer);
    public void updateManagerAccount(Manager manager);
    public boolean updateCustomerArcaneStatus(int customerID, boolean status);
}